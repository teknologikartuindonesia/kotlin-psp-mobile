package id.co.pspmobile.ui.invoice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.paging.LOGGER
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.responses.activebroadcast.ContentX
import id.co.pspmobile.databinding.ActivityInvoiceBinding
import id.co.pspmobile.databinding.ActivityInvoicePaymentBinding
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.ui.NumberTextWatcher
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.customDialog.CustomDialogFragment
import id.co.pspmobile.ui.invoice.fragment.BottomSheetPaymentSuccessInvoice
import id.co.pspmobile.ui.invoice.fragment.DetailInvoiceAdapter
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

@AndroidEntryPoint
class InvoicePaymentActivity : AppCompatActivity() {
    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var binding: ActivityInvoicePaymentBinding
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter
    private lateinit var invoice: InvoiceDto
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = LinearLayoutManager(this)

        detailInvoiceAdapter = DetailInvoiceAdapter()

        val intent = intent?.extras
        val c = intent?.getString("content")
        invoice = c?.let { Gson().fromJson(it, InvoiceDto::class.java) }!!

        binding.apply {
            edNominal.addTextChangedListener(NumberTextWatcher(binding.edNominal));
            edNominal.addTextChangedListener {
//                Toast.makeText(requireContext(), edNominal.text.toString().trim().replace(".", "").replace(",", ""), Toast.LENGTH_SHORT).show()
            }

            tvInvoiceName.text = invoice?.title
            tvInvoiceNameDetail.text = invoice?.title
            if (invoice!!.showDetail) {
                rvDetailInvoice.visibility = View.VISIBLE
            } else {
                rvDetailInvoice.visibility = View.GONE
            }
            if (invoice?.callerName == viewModel.getUserData().user.name) {
                parentNameContainer.visibility = View.GONE
            } else {
                parentNameContainer.visibility = View.VISIBLE
            }
            if (invoice.description == "") {
                tvInvoiceDescription.visibility = View.GONE
                tvShowAll.visibility = View.GONE
            }
            if (invoice.description?.length!! <= 100) tvShowAll.visibility = View.GONE

            tvShowAll.text = resources.getString(android.R.string.cut)
            tvInvoiceDescription.text = Utils.subString(invoice.description!!, 0, 100) + "..."
            tvShowAll.setOnClickListener {
                when (tvShowAll.text) {
                    "Tampilkan Semua" -> {
                        tvShowAll.text = "Tampilkan lebih sedikit"
                        tvInvoiceDescription.text = invoice.description
                    }

                    else -> {
                        tvShowAll.text = "Tampilkan Semua"
                        tvInvoiceDescription.text =
                            invoice.description!!.length.toString().subSequence(0, 100)
                    }
                }

            }

            tvParentName.text = viewModel.getUserData().user.name
            tvStudentName.text = invoice.callerName
            tvDate.text = invoice.invoiceDate
            tvDueDate.text = invoice.dueDate
            tvPaid.text = Utils.formatCurrency(invoice.paidAmount)
            tvMinus.text = Utils.formatCurrency(invoice.amount - invoice.paidAmount)
            if (invoice.partialMethod) {
                containerNominal.visibility = View.VISIBLE

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
                containerNominal.visibility = View.GONE
                when (viewModel.getLanguage().toString()) {
                    "en" -> tvType.text = "CASH"
                    else -> tvType.text = "TUNAI"
                }

                when (viewModel.getLanguage().toString()) {
                    "en" -> tvStatus.text = "Paid Off"
                    else -> tvStatus.text = "Lunas"
                }
            }
            tvAmount.text = Utils.formatCurrency(invoice.amount)

            Log.e("te", invoice.detail.toString())
            for (i in invoice.detail){

            }
            detailInvoiceAdapter.setDetail(invoice.detail)
            rvDetailInvoice.setHasFixedSize(true)
            rvDetailInvoice.layoutManager = layoutManager
            rvDetailInvoice.adapter = detailInvoiceAdapter


            btnPay.setOnClickListener {
                if (invoice.partialMethod) {
                    if (edNominal.text.toString().trim().replace(".", "")
                            .replace(",", "").toInt() < 10000
                    ) {
                        alertNominal.visibility = View.VISIBLE
                    } else {
                        alertNominal.visibility = View.GONE
                        OpenCustomDialog(
                            "Konfirmasi",
                            "Apakah anda yakin akan melakukan pembayaran?",
                            "invoice",
                            "partial"
                        )
                    }

                } else {
                    OpenCustomDialog(
                        "Konfirmasi",
                        "Apakah anda yakin akan melakukan pembayaran?",
                        "invoice",
                        "cash"
                    )

                }

            }

            viewModel.paymentInvoiceResponse.observe(this@InvoicePaymentActivity) {
                when (it is Resource.Loading) {
                    true -> showLottieLoader()
                    else -> hideLottieLoader()
                }
                if (it is Resource.Success) {

                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
                        BottomSheetPaymentSuccessInvoice("", it.value, invoice)
                    bottomSheetDialogFragment.setCancelable(false);
                    bottomSheetDialogFragment.show(
                        supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                } else if (it is Resource.Failure) {
                    handleApiError(binding.root, it)
                }
            }

            binding.btnBack.setOnClickListener {
                finish()
            }

            //Declare LocalBroadcastManager
            LocalBroadcastManager.getInstance(this@InvoicePaymentActivity)
                .registerReceiver(mMessageReceiver, IntentFilter("custom-dialog"))

            //Declare LocalBroadcastManager
            LocalBroadcastManager.getInstance(this@InvoicePaymentActivity)
                .registerReceiver(finishMessageReceiver, IntentFilter("finish-activity"))

        }
    }

    fun paymentPartial(invoice: InvoiceDto) {
        viewModel.paymentInvoice(
            Utils.parseDouble(
                binding.edNominal.text.toString().trim().replace(".", "").replace(",", "")
            ),
            invoice.invoiceId!!
        )
    }

    fun paymentCash(invoice: InvoiceDto) {
        viewModel.paymentInvoice(
            invoice.amount,
            invoice.invoiceId!!
        )
    }

    //Call Custom Dialog With Custom Text
    fun OpenCustomDialog(title: String?, subTitle: String?, feature: String?, type: String?) {
        val dialogFragment = CustomDialogFragment()
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("subtitle", subTitle)
        bundle.putString("feature", feature)
        bundle.putString("type", type)
        dialogFragment.setArguments(bundle)
        dialogFragment.getView()?.setElevation(10f);

        dialogFragment.show(supportFragmentManager, "myDialog")
    }

    //Listen from LocalBroadcastManager custom dialog
    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val feature = intent.getStringExtra("feature")
            val type = intent.getStringExtra("type")
            if (feature == "invoice" && type == "partial") {
                paymentPartial(invoice)

            } else {
                paymentCash(invoice)
            }

        }
    }

    //Listen from LocalBroadcastManager payment Success
    private val finishMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val finish = intent.getStringExtra("finish")
            if (finish == "finish") {
                val intent = Intent("reload-invoice")
                intent.putExtra("reload", "reload")
                LocalBroadcastManager.getInstance(this@InvoicePaymentActivity).sendBroadcast(intent)
                finish()
            }
        }
    }


    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(supportFragmentManager, "lottieLoaderDialog")

    }

    private fun hideLottieLoader() {
        val loaderDialogFragment =
            supportFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }
}