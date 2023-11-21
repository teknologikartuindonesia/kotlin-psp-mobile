package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.invoice.InvoicePaymentDto
import id.co.pspmobile.data.network.invoice.InvoiceResDto
import id.co.pspmobile.databinding.BottomSheetPaymentInvoiceBinding
import id.co.pspmobile.databinding.BottomSheetPaymentSuccessInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.invoice.InvoiceViewModel


@AndroidEntryPoint
class BottomSheetPaymentSuccessInvoice(
    private val userName: String,
    private val invoicePayment: InvoicePaymentDto,
    private val invoice: InvoiceDto,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentSuccessInvoiceBinding
    private val viewModel: InvoiceViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetPaymentSuccessInvoiceBinding.inflate(inflater)

        binding.apply {
            tvInvoiceName.text = invoicePayment.inquiryResponseDto.title
            tvParentName.text = invoice.callerName
            tvStudentName.text = invoice.callerName
            tvPaid.text = formatCurrency(invoicePayment.amount)
            tvPayDate.text =
                formatDateTime(invoicePayment.inquiryResponseDto.createDate!!, "dd-MM-yyyy HH:mm")
            tvCreateDate.text = "Tanggal " + formatDateTime(
                invoicePayment.inquiryResponseDto.createDate!!,
                "dd MMMM yyyy HH:mm"
            )
            var kekurangan = invoicePayment.inquiryResponseDto.amount -
                    (invoicePayment.amount + invoicePayment.inquiryResponseDto.paidAmount)
            tvMinus.text = formatCurrency(kekurangan)

            if (invoicePayment.inquiryResponseDto.partialMethod) {
                tvType.text = "CREDIT"
            } else {
                tvType.text = "CASH"
            }

            if ((invoicePayment.inquiryResponseDto.amount - invoicePayment.amount).toInt() == 0) {
                tvStatus.text = "Terbayar"
            } else {
                tvStatus.text = "Terbayar Sebagian"

            }
            tvAmount.text =
                formatCurrency(invoicePayment.inquiryResponseDto.amount - invoicePayment.inquiryResponseDto.paidAmount)

            btnDownload.setOnClickListener {
                //
            }

            btnClose.setOnClickListener {
                dismiss()

            }

        }

        return binding.root
    }

}