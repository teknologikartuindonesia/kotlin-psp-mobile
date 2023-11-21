package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import javax.annotation.Nullable


@AndroidEntryPoint
class BottomSheetPaymentSuccessInvoice(
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
            } else {
                tvType.text = "CASH"
            }
            tvStatus.text = invoice.status
            tvAmount.text = formatCurrency(invoice.amount)


            btnPay.setOnClickListener {
                viewModel.paymentInvoice(
                    binding.nominal.text.toString().toDouble(),
                    invoice.invoiceId!!
                )
                dismiss()
                
            }


        }

        return binding.root
    }

}