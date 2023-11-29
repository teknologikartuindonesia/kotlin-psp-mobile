package id.co.pspmobile.ui.invoice.fragment

import android.R
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.ui.NumberTextWatcher
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.parseDouble
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.customDialog.CustomDialogFragment
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment


@AndroidEntryPoint
class BottomSheetPaymentInvoice(
    private val userName: String,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentInvoiceBinding
    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetPaymentInvoiceBinding.inflate(inflater)
        detailInvoiceAdapter = DetailInvoiceAdapter()

        binding.apply {
            edNominal.addTextChangedListener(NumberTextWatcher(binding.edNominal));
            edNominal.addTextChangedListener {
//                Toast.makeText(requireContext(), edNominal.text.toString().trim().replace(".", "").replace(",", ""), Toast.LENGTH_SHORT).show()
            }

            tvInvoiceName.text = invoice.title
            tvInvoiceNameDetail.text = invoice.title
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

            if (invoice.description == "") {
                tvInvoiceDescription.visibility = View.GONE
                tvShowAll.visibility = View.GONE
            }
            if (invoice.description?.length!! <= 100) tvShowAll.visibility = View.GONE

            tvShowAll.text = "Tampilkan Semua"
            tvInvoiceDescription.text = Utils.subString(invoice.description, 0, 100) + "..."
            tvShowAll.setOnClickListener {
                when (tvShowAll.text) {
                    "Tampilkan Semua" -> {
                        tvShowAll.text = "Tampilkan lebih sedikit"
                        tvInvoiceDescription.text = invoice.description
                    }

                    else -> {
                        tvShowAll.text = "Tampilkan Semua"
                        tvInvoiceDescription.text =
                            invoice.description.length.toString().subSequence(0, 100)
                    }
                }

            }

            tvParentName.text = viewModel.getUserData().user.name
            tvStudentName.text = invoice.callerName
            tvDate.text = invoice.invoiceDate
            tvDueDate.text = invoice.dueDate
            tvPaid.text = formatCurrency(invoice.paidAmount)
            tvMinus.text = formatCurrency(invoice.amount - invoice.paidAmount)
            if (invoice.partialMethod) {
                when (viewModel.getLanguage().toString()) {
                    "en" -> tvType.text = "CREDIT"
                    else -> tvType.text = "KREDIT"
                }
                containerNominal.visibility = View.VISIBLE
            } else {
                when (viewModel.getLanguage().toString()) {
                    "en" -> tvType.text = "CASH"
                    else -> tvType.text = "TUNAI"
                }
                tvType.text = "CASH"
                containerNominal.visibility = View.GONE
            }
            tvStatus.text = invoice.status
            tvAmount.text = formatCurrency(invoice.amount)

            detailInvoiceAdapter.setDetail(invoice.detail)
            rvDetailInvoice.setHasFixedSize(true)
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

            viewModel.paymentInvoiceResponse.observe(viewLifecycleOwner) {
                when (it is Resource.Loading) {
                    true -> showLottieLoader()
                    else -> hideLottieLoader()
                }
                if (it is Resource.Success) {
                    dismiss()
                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
                        BottomSheetPaymentSuccessInvoice("", it.value, invoice)
                    bottomSheetDialogFragment.show(
                        (requireActivity()).supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                } else if (it is Resource.Failure) {
                }
            }

        }

        //Declare LocalBroadcastManager
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mMessageReceiver, IntentFilter("custom-dialog"))

        return binding.root
    }

    fun paymentPartial() {
        viewModel.paymentInvoice(
            parseDouble(
                binding.edNominal.text.toString().trim().replace(".", "").replace(",", "")
            ),
            invoice.invoiceId!!
        )
    }

    fun paymentCash() {
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

        dialogFragment.show(parentFragmentManager, "myDialog")
    }

    //Listen from LocalBroadcastManager custom dialog
    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val feature = intent.getStringExtra("feature")
            val type = intent.getStringExtra("type")
            if (feature == "invoice" && type == "partial") {
                paymentPartial()

            } else {
                paymentCash()
            }

        }
    }

    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(parentFragmentManager, "lottieLoaderDialog")

    }

    private fun hideLottieLoader() {
        val loaderDialogFragment =
            parentFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }
}