package id.co.pspmobile.ui.invoice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
        var balance = viewModel.getBalanceUser().balance

        binding.apply {
            edNominal.addTextChangedListener(NumberTextWatcher(binding.edNominal));
            btnPay.setBackgroundColor(resources.getColor(R.color.grey_font))

            val unpaidAmount = invoice.amount - invoice.paidAmount
            edNominal.setText("0")
            val inputNominal = edNominal.text.toString().trim().replace(".", "")
                .replace(",", "").toInt()
            btnPay.isEnabled = false

            edNominal.addTextChangedListener {
                try {
                    val edValue =
                        it.toString().trim().replace(".", "").replace(",", "").toInt()

                    if (unpaidAmount >= 10000) {
                        btnPay.setBackgroundColor(resources.getColor(R.color.grey_font))
                        btnPay.isEnabled = false

                        if (edValue.toInt()
                            > balance.toInt()
                        ) {
                            alertNominal.text = resources.getString(R.string.insufficient_balance)
                            alertNominal.visibility = View.VISIBLE
                        } else if (edValue.toInt()
                            > unpaidAmount
                        ) {
                            alertNominal.text =
                                resources.getString(R.string.input_amount_cant_more_than_unpaid)
                            alertNominal.visibility = View.VISIBLE
                        } else if (edValue.toInt() < 10000) {
                            alertNominal.text =
                                resources.getString(R.string.minimum_payment)
                            alertNominal.visibility = View.VISIBLE
                        } else {
                            alertNominal.visibility = View.GONE
                            btnPay.setBackgroundColor(resources.getColor(R.color.primary))
                            btnPay.isEnabled = true
                            Log.d("partialMethod", "validation is true")
                        }

                    }

                } catch (e: Exception) {

                }
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
                        tvShowAll.text = getString(R.string.show_less)
                        tvInvoiceDescription.text = invoice.description
                    }

                    else -> {
                        tvShowAll.text = getString(R.string.show_all)
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
                tvType.text = getString(R.string.credit)
                if ((invoice.amount - invoice.paidAmount).toInt() == 0) {
                    tvStatus.text = getString(R.string.paid)
                } else {
                    if (invoice.paidAmount.toInt() == 0) {
                        tvStatus.text = getString(R.string.unpaid)
                    } else {
                        tvStatus.text = getString(R.string.partially_paid)
                    }

                }

                if (unpaidAmount < 10000) {
                    btnPay.setBackgroundColor(resources.getColor(R.color.primary))
                    btnPay.isEnabled = true
                    edNominal.setText(unpaidAmount.toDouble().toString())
                    edNominal.isEnabled = false

                    if (inputNominal.toInt() > balance.toInt()
                    ) {
                        alertNominal.text =
                            resources.getString(R.string.insufficient_balance)
                        alertNominal.visibility = View.VISIBLE
                    } else if (inputNominal.toInt()
                        > unpaidAmount
                    ) {
                        alertNominal.text =
                            resources.getString(R.string.input_amount_cant_more_than_unpaid)
                        alertNominal.visibility = View.VISIBLE
                    } else {
                        Log.d("partialMethod", "validation is true")
                    }
                }

            } else {
                // is not partial
                edNominal.visibility = View.GONE
                tvDetailTag.visibility = View.GONE
                tvType.text = getString(R.string.cash)

                if (unpaidAmount.toInt() == 0) {
                    tvStatus.text = getString(R.string.paid)
                } else {
                    if (invoice.paidAmount.toInt() == 0) {
                        tvStatus.text = getString(R.string.unpaid)
                    }
                }

                btnPay.setBackgroundColor(resources.getColor(R.color.grey_font))
                btnPay.isEnabled = false
                if (unpaidAmount.toInt() > balance
                ) {
                    alertNominal.text =
                        resources.getString(R.string.note) + " " + resources.getString(R.string.insufficient_balance)
                    alertNominal.visibility = View.VISIBLE
                } else {
                    btnPay.setBackgroundColor(resources.getColor(R.color.primary))
                    btnPay.isEnabled = true
                    Log.d("nonPartialMethod", "validation is true")
                }
            }

            tvAmount.text = Utils.formatCurrency(invoice.amount)

            detailInvoiceAdapter.setDetail(invoice.detail)
            rvDetailInvoice.setHasFixedSize(true)
            rvDetailInvoice.layoutManager = layoutManager
            rvDetailInvoice.adapter = detailInvoiceAdapter

            btnPay.setOnClickListener {
                if (invoice.partialMethod) {
                    OpenCustomDialog(
                        resources.getString(R.string.confirmation),
                        resources.getString(R.string.are_you_sure_to_pay),
                        "invoice",
                        "partial"
                    )
                } else {
                    OpenCustomDialog(
                        resources.getString(R.string.confirmation),
                        resources.getString(R.string.are_you_sure_to_pay),
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
            invoice.amount - invoice.paidAmount,
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