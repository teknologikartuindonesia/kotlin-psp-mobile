package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


@AndroidEntryPoint
class BottomSheetReceiptFragment(
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetReceiptBinding
    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter

    private lateinit var layoutManager: LinearLayoutManager

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBottomSheetReceiptBinding.inflate(inflater)
        layoutManager = LinearLayoutManager(requireContext())
        detailInvoiceAdapter = DetailInvoiceAdapter()

        Log.e("TAG", invoice.toString())
        binding.apply {
            tvInvoiceName.text = invoice.title
            tvParentName.text = invoice.callerName
            tvStudentName.text = invoice.callerName
            tvDescription.text = invoice.description
            tvPaid.text = "Rp " + formatCurrency(invoice.paidAmount)
            tvPayDate.text =
                formatDateTime(invoice.createDate!!, "dd-MM-yyyy HH:mm")
            tvCreateDate.text = "Tanggal " + formatDateTime(
                invoice.createDate!!,
                "dd MMMM yyyy HH:mm"
            )
            var kekurangan = (invoice.amount - invoice.paidAmount)
            tvMinus.text = "Rp " + formatCurrency(kekurangan)

            if (invoice!!.showDetail) {
                rvDetailInvoice.visibility = View.VISIBLE
            } else {
                rvDetailInvoice.visibility = View.GONE
            }

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
            tvAmount.text = "Rp " + formatCurrency(invoice.amount)

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


        }
        when (arguments?.getString("key_data")) {
            "download" -> {
                Handler().postDelayed({
                    val image = getBitmapFromUiView(binding.basePanel)
                    saveBitmapImage(image!!)
                    dismiss()
                }, 1000)
            }

            "share" -> {
                Log.e("r", "share:open")
                Handler().postDelayed({
                    getBitmapUriFromView(requireContext(), binding.basePanel)?.let { it1 ->
                        shareApp(
                            it1
                        )
                    }
                    dismiss()
                }, 1000)
            }

        }
        binding.tvStatusPayment.setOnClickListener {
            Log.d(
                "TAG",
                getBitmapUriFromView(requireActivity(), binding.parentNameContainer).toString()
            )
        }

        Log.e("r", invoice.detail.toString())
        detailInvoiceAdapter.setDetail(invoice.detail)

        binding.rvDetailInvoice.setHasFixedSize(true)
        binding.rvDetailInvoice.layoutManager = layoutManager
        binding.rvDetailInvoice.adapter = detailInvoiceAdapter


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

    fun getBitmapUriFromView(context: Context, view: View): Uri? {
        // Get the Bitmap from the View
        val bitmap = getBitmapFromView(view)

        val file = saveBitmapToFile(context, bitmap)
        // Obtain the URI from the saved file
        return file?.let { getUriFromFile(context, it) }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
        val fileName = "receipt_invoice_" + UUID.randomUUID().toString() + ".png"
        val directory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "YourAppFolder")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getUriFromFile(context: Context, file: File): Uri? {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/YourAppFolder"
                )
            }

            val contentResolver: ContentResolver = context.contentResolver
            val uri: Uri? =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                outputStream?.use { output -> file.inputStream().copyTo(output) }
            }

            uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
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

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    //    private fun shareApp() {
//        val bitmapUri = getBitmapFromView(binding.basePanel)
//        val uri = getImageUri(bitmapUri)
//        val share = Intent.createChooser(Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, getString(R.string.invoice))
//            putExtra(Intent.EXTRA_STREAM, bitmapUri)
//            data = uri
//            type = "image/jpeg"
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        }, null)
//    }
    private fun shareApp(bitmapUri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.type = "image/jpeg"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(Intent.createChooser(intent, "Bagikan bukti pembayaran"))
    }
}