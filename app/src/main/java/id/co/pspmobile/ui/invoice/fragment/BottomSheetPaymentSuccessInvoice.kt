package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.invoice.InvoicePaymentDto
import id.co.pspmobile.data.network.invoice.InvoiceResDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.databinding.BottomSheetPaymentSuccessInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@AndroidEntryPoint
class BottomSheetPaymentSuccessInvoice(
    private val userName: String,
    private val invoicePayment: InvoicePaymentDto,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentSuccessInvoiceBinding
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter

    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetPaymentSuccessInvoiceBinding.inflate(inflater)
        layoutManager = LinearLayoutManager(requireContext())
        detailInvoiceAdapter = DetailInvoiceAdapter()

        binding.apply {
            tvInvoiceName.text = invoicePayment.inquiryResponseDto.title
            tvParentName.text = viewModel.getUserData().user.name
            tvStudentName.text = invoice.callerName
            tvAmount.text = formatCurrency(invoicePayment.amount)
            tvPayDate.text =
                formatDateTime(invoicePayment.inquiryResponseDto.createDate!!, "dd-MM-yyyy HH:mm")
            tvCreateDate.text = "Tanggal " + formatDateTime(
                invoicePayment.dateTime.toString(),
                "dd MMMM yyyy HH:mm"
            )
            var kekurangan = invoicePayment.inquiryResponseDto.amount -
                    (invoicePayment.amount + invoicePayment.inquiryResponseDto.paidAmount)
            tvMinus.text = formatCurrency(kekurangan)

            if (invoice.callerName == viewModel.getUserData().user.name) {
                parentNameContainer.visibility = View.GONE
            } else {
                parentNameContainer.visibility = View.VISIBLE
            }

            if (invoice.partialMethod) {
                when (viewModel.getLanguage().toString()) {
                    "en" -> tvType.text = "CREDIT"
                    else -> tvType.text = "KREDIT"
                }
                if ((invoicePayment.inquiryResponseDto.amount - invoicePayment.inquiryResponseDto.paidAmount).toInt() == 0) {
                    when (viewModel.getLanguage().toString()) {
                        "en" -> tvStatus.text = "Paid Off"
                        else -> tvStatus.text = "Lunas"
                    }
                } else {
                    when (viewModel.getLanguage().toString()) {
                        "en" -> tvStatus.text = "Partially Paid"
                        else -> tvStatus.text = "Terbayar Sebagian"
                    }
                }
            } else {
                when (viewModel.getLanguage().toString()) {
                    "en" -> tvType.text = "CASH"
                    else -> tvType.text = "TUNAI"
                }

                when (viewModel.getLanguage().toString()) {
                    "en" -> tvStatus.text = "Paid Off"
                    else -> tvStatus.text = "Lunas"
                }
            }
            tvPaid.text =
                formatCurrency(invoicePayment.inquiryResponseDto.amount)

            Log.e("r", invoice.detail.toString())
            detailInvoiceAdapter.setDetail(invoice.detail)
            rvDetailInvoice.setHasFixedSize(true)
            rvDetailInvoice.layoutManager = layoutManager
            rvDetailInvoice.adapter = detailInvoiceAdapter

            btnDownload.setOnClickListener {
                btnDownload.visibility = View.GONE
                btnClose.visibility = View.GONE
                val image = getBitmapFromUiView(basePanel)
                saveBitmapImage(image!!)
                btnDownload.visibility = View.VISIBLE
                btnClose.visibility = View.VISIBLE
            }

            btnClose.setOnClickListener {
                dismiss()
                val args = arguments
                val intent = Intent("finish-activity")
                intent.putExtra("finish", "finish")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            Log.e("r", invoice.detail.toString())
            detailInvoiceAdapter.setDetail(invoice.detail)
            rvDetailInvoice.setHasFixedSize(true)
            rvDetailInvoice.layoutManager = layoutManager
            rvDetailInvoice.adapter = detailInvoiceAdapter

        }



        return binding.root
    }

    private fun getBitmapFromUiView(view: View?): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = view?.let {
            Bitmap.createBitmap(
                requireView().width,
                it.height,
                Bitmap.Config.ARGB_8888
            )
        }
        //Bind a canvas to it
        val canvas = returnedBitmap?.let { Canvas(it) }
        //Get the view's background
        val bgDrawable = view?.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            if (canvas != null) {
                bgDrawable.draw(canvas)
            }
        } else {
            //does not have background drawable, then draw white background on the canvas
            if (canvas != null) {
                canvas.drawColor(Color.WHITE)
            }
        }
        // draw the view on the canvas
        view?.draw(canvas!!)

        //return the bitmap
        return returnedBitmap!!
    }

    private fun saveBitmapImage(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/" + getString(R.string.app_name)
            )
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            if (uri != null) {
                try {
                    val outputStream = requireContext().contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e("TAG", "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    requireContext().contentResolver.update(uri, values, null, null)

                    Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("TAG", "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory().toString() + '/' + getString(
                    R.string.app_name
                )
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e("TAG", "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )

                Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("TAG", "saveBitmapImage: ", e)
            }
        }
    }

}