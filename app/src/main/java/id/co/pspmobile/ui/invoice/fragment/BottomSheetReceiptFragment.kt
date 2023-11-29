package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.invoice.InvoicePaymentDto
import id.co.pspmobile.data.network.invoice.InvoiceResDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.databinding.BottomSheetPaymentSuccessInvoiceBinding
import id.co.pspmobile.databinding.FragmentBottomSheetReceiptBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@AndroidEntryPoint
class BottomSheetReceiptFragment(
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetReceiptBinding
    private val viewModel: InvoiceViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBottomSheetReceiptBinding.inflate(inflater)


        Log.e("TAG", invoice.toString())
        binding.apply {
            tvInvoiceName.text = invoice.title
            tvParentName.text = invoice.callerName
            tvStudentName.text = invoice.callerName
            tvDescription.text = invoice.description
            tvPaid.text = "Rp "+formatCurrency(invoice.paidAmount)
            tvPayDate.text =
                formatDateTime(invoice.createDate!!, "dd-MM-yyyy HH:mm")
            tvCreateDate.text = "Tanggal " + formatDateTime(
                invoice.createDate!!,
                "dd MMMM yyyy HH:mm"
            )
            var kekurangan = (invoice.amount - invoice.paidAmount)
            tvMinus.text = "Rp "+formatCurrency(kekurangan)

            if (invoice.partialMethod) {
                tvType.text = "CREDIT"
            } else {
                tvType.text = "CASH"
            }

            if ((invoice.amount - invoice.amount).toInt() == 0) {
                tvStatus.text = "Terbayar"
            } else {
                tvStatus.text = "Terbayar Sebagian"

            }
            tvAmount.text = "Rp "+ formatCurrency(invoice.amount)

            if (invoice.description == "") {
                tvDescription.visibility = View.GONE
                descriptionContainer.visibility = View.GONE
            }

            if (invoice.callerName == viewModel.getUserData().user.name) {
                parentNameContainer.visibility = View.GONE
            } else {
                parentNameContainer.visibility = View.VISIBLE

            }
            tvParentName.text = viewModel.getUserData().user.name
            Handler().postDelayed({
                val image = getBitmapFromUiView(basePanel)
                saveBitmapImage(image!!)
                dismiss()
            }, 600)
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