package id.co.pspmobile.ui.invoice.fragment

import android.R.attr.data
import android.R.attr.fragment
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
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.BottomSheetDetailInvoiceBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.subString
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


@AndroidEntryPoint
class BottomSheetDetailInvoice(
    private val userName: String,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDetailInvoiceBinding
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter
    private val viewModel: InvoiceViewModel by viewModels()

    private lateinit var layoutManager: LinearLayoutManager

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDetailInvoiceBinding.inflate(inflater)
        layoutManager = LinearLayoutManager(requireContext())

        detailInvoiceAdapter = DetailInvoiceAdapter()

        binding.apply {

            try {
                tvInvoiceName.text = invoice.title
                tvParentName.text = userName
                tvStudentName.text = invoice.callerName
                tvDate.text = invoice.invoiceDate
                tvDueDate.text = invoice.dueDate

                tvReceiptAmount.text = "Rp " + formatCurrency(invoice.amount)
                tvReceiptCreateDate.text = invoice.createDate.toString()
                tvReceiptInvoiceName.text = invoice.title
                tvReceiptPaid.text = "Rp " + formatCurrency(invoice.paidAmount)

                if (invoice.callerName == viewModel.getUserData().user.name) {
                    receiptParentNameContainer.visibility = View.GONE
                } else {
                    receiptParentNameContainer.visibility = View.VISIBLE

                }
                tvReceiptParentName.text = viewModel.getUserData().user.name
                tvReceiptPayDate.text = invoice.dueDate.toString()
                tvReceiptStatus.text = invoice.status
                tvReceiptStudentName.text = invoice.callerName
                tvReceiptType.text = invoice.status

                if (invoice.showDetail) {
                    rvDetailInvoice.visibility = View.VISIBLE
                } else {
                    rvDetailInvoice.visibility = View.GONE
                }

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
                    if ((invoice.amount - invoice.paidAmount).toInt() == 0) {
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
                tvParentName.text = viewModel.getUserData().user.name

                tvAmount.text = formatCurrency(invoice.amount)

                if (invoice.description == "") {
                    tvInvoiceDescription.visibility = View.GONE
                    tvShowAll.visibility = View.GONE
                }
                if (invoice.description?.length!! <= 100) tvShowAll.visibility = View.GONE

                tvShowAll.text = "Tampilkan Semua"
                tvInvoiceDescription.text = subString(invoice.description, 0, 100) + "..."
                tvShowAll.setOnClickListener {
                    when (tvShowAll.text) {
                        "Tampilkan Semua" -> {
                            tvShowAll.text = "Tampilkan lebih sedikit"
                            tvInvoiceDescription.text = invoice.description
                        }

                        else -> {
                            tvShowAll.text = "Tampilkan Semua"
                            tvInvoiceDescription.text =
                                subString(invoice.description, 0, 100) + " ..."
                        }
                    }

                }

                Log.e("r", invoice.detail.toString())
                detailInvoiceAdapter.setDetail(invoice.detail)
                rvDetailInvoice.setHasFixedSize(true)
                rvDetailInvoice.layoutManager = layoutManager
                rvDetailInvoice.adapter = detailInvoiceAdapter

                btnDownload.setOnClickListener {
                    val args = Bundle()
                    args.putString("key_data", "download")
                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
                        BottomSheetReceiptFragment(invoice)
                    bottomSheetDialogFragment.arguments = args
                    bottomSheetDialogFragment.show(
                        (requireActivity()).supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                }
                btnShare.setOnClickListener {
                    getBitmapUriFromView(requireContext(),binding.basePanel)?.let { it1 ->
                        shareApp(
                            it1
                        )
                    }
//                    val args = Bundle()
//                    args.putString("key_data", "share")
//                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
//                        BottomSheetReceiptFragment(invoice)
//                    bottomSheetDialogFragment.arguments = args
//                    bottomSheetDialogFragment.show(
//                        (requireActivity()).supportFragmentManager,
//                        bottomSheetDialogFragment.tag
//                    )
                }
            } catch (e: Exception) {
                Log.e("TAG", "onCreateView: ", e)
            }

        }

        return binding.root
    }

    fun getBitmapUriFromView(context: Context, view: View): Uri? {
        // Get the Bitmap from the View
        val bitmap = getBitmapFromView(view)

        val file = saveBitmapToFile(context, bitmap)
        // Obtain the URI from the saved file
        return file?.let { getUriFromFile(context, it) }
    }

    // Function to save a Bitmap to a temporary file
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
        val fileName = "receipt_invoice_" + UUID.randomUUID().toString() + ".png"
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "YourAppFolder")

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
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/YourAppFolder")
            }

            val contentResolver: ContentResolver = context.contentResolver
            val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

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
    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    private fun shareApp(bitmapUri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.type = "image/jpeg"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(Intent.createChooser(intent, "Bagikan bukti pembayaran"))
    }
}