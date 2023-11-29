package id.co.pspmobile.ui.invoice.fragment

import android.R.attr.data
import android.R.attr.fragment
import android.annotation.SuppressLint
import android.content.ContentValues
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
import java.io.OutputStream


@AndroidEntryPoint
class BottomSheetDetailInvoice(
    private val userName: String,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDetailInvoiceBinding
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter
    private val viewModel: InvoiceViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDetailInvoiceBinding.inflate(inflater)
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
                } else {
                    when (viewModel.getLanguage().toString()) {
                        "en" -> tvType.text = "CASH"
                        else -> tvType.text = "TUNAI"
                    }
                    tvType.text = "CASH"
                }
                tvParentName.text = viewModel.getUserData().user.name
                tvStatus.text = invoice.status
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

                detailInvoiceAdapter.setDetail(invoice.detail)
                rvDetailInvoice.setHasFixedSize(true)
                rvDetailInvoice.adapter = detailInvoiceAdapter
                btnDownload.setOnClickListener {
                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
                        BottomSheetReceiptFragment(invoice)
                    bottomSheetDialogFragment.show(
                        (requireActivity()).supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                }
                btnShare.setOnClickListener {
                    btnShare.visibility=View.GONE
                    btnDownload.visibility=View.GONE
                    val image = getBitmapFromView(binding.basePanel)
                    shareApp(image)
                    btnShare.visibility=View.VISIBLE
                    btnDownload.visibility=View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("TAG", "onCreateView: ", e)
            }

        }

        return binding.root
    }

    private fun getBitmapFromView(view: View): Uri {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        //Define a bitmap with the same size as the view
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap

        var context = requireContext()
        val dir = File(context.getExternalFilesDir(null).toString(), "tmpScreenshoot")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val uri = null;
        try {
            val gpxfile = File(dir, "transactionId" + ".jpg")
            val fos = FileOutputStream(gpxfile)
            returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Log.d("url", gpxfile.absolutePath)
            return Uri.parse("file://" + gpxfile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace();
        }
        throw java.lang.Exception("Error:")
    }

    private fun loadBitmapFromView(v: View): Bitmap? {
        val specWidth = View.MeasureSpec.makeMeasureSpec(2000 /* any */, View.MeasureSpec.EXACTLY)
        v.measure(specWidth, specWidth)
        val questionWidth = v.measuredWidth
        val b = Bitmap.createBitmap(questionWidth, questionWidth, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.drawColor(Color.WHITE)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    /**Get Bitmap from any UI View
     * @param view any UI view to get Bitmap of
     * @return returnedBitmap the bitmap of the required UI View */
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


    /**Save Bitmap To Gallery
     * @param bitmap The bitmap to be saved in Storage/Gallery*/
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

    private fun shareApp(bitmapUri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.type = "image/jpeg"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(Intent.createChooser(intent, "Bagikan bukti pembayaran"))
    }
}