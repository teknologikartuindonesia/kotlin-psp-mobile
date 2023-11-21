package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import javax.annotation.Nullable


@AndroidEntryPoint
class BottomSheetPaymentInvoice(
    private val userName: String,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentInvoiceBinding
    private val viewModel: InvoiceViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetPaymentInvoiceBinding.inflate(inflater)

        binding.apply {
            tvInvoiceName.text = invoice.title
            tvParentName.text = invoice.callerName
            tvStudentName.text = invoice.callerName
            tvDate.text = invoice.invoiceDate
            tvDueDate.text = invoice.dueDate
            tvPaid.text = formatCurrency(invoice.paidAmount)
            tvMinus.text = formatCurrency(invoice.amount - invoice.paidAmount)
            if (invoice.partialMethod) {
                tvType.text = "CREDIT"
                containerNominal.visibility = View.VISIBLE
            } else {
                tvType.text = "CASH"
                containerNominal.visibility = View.GONE
            }
            tvStatus.text = invoice.status
            tvAmount.text = formatCurrency(invoice.amount)


            btnPay.setOnClickListener {
                if (invoice.partialMethod){
                    viewModel.paymentInvoice(
                        binding.nominal.text.toString().toDouble(),
                        invoice.invoiceId!!
                    )
                }else{
                    viewModel.paymentInvoice(
                        invoice.amount,
                        invoice.invoiceId!!
                    )
                }


            }

            viewModel.paymentInvoiceResponse.observe(viewLifecycleOwner) {
                binding.progressbar.visible(it is Resource.Loading)
                if (it is Resource.Success) {
                    dismiss()
                    val bottomSheetDialogFragment: BottomSheetDialogFragment = BottomSheetPaymentSuccessInvoice("",it.value,invoice)
                    bottomSheetDialogFragment.show(
                        (requireActivity()).supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                } else if (it is Resource.Failure) {
                }
            }


        }

        return binding.root
    }

}