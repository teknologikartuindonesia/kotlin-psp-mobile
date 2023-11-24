package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.BottomSheetDetailInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class BottomSheetDetailInvoice(
    private val userName: String,
    private val invoice: InvoiceDto
) : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetDetailInvoiceBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDetailInvoiceBinding.inflate(inflater)

        binding.apply {
            tvInvoiceName.text = invoice.title
            tvParentName.text = userName
            tvStudentName.text = invoice.callerName
            tvDate.text = invoice.invoiceDate
            tvDueDate.text = invoice.dueDate
            if (invoice.partialMethod) {
                tvType.text = "CREDIT"
            } else {
                tvType.text = "CASH"
            }
            tvStatus.text = invoice.status
            tvAmount.text = formatCurrency(invoice.amount)

            if (invoice.detail.isEmpty()) {
                tvDetailTag.visible(false)
            } else {
                var detailInvoice = ""
                for (detail in invoice.detail) {
                    detailInvoice += if (detailInvoice.isEmpty()) {
                        "<table><tr>"
                    } else {
                        "<tr>"
                    }
                    detailInvoice += "<td><font color='#000000'>" + detail.title + "</font></td>" +
                            "<td>&emsp; : &nbsp;</td>" +
                            "<td><font color='#000000'>" + formatCurrency(detail.amount) + "</font></td></tr>"
                }
                if (!detailInvoice.isEmpty()) {
                    detailInvoice += "</table>"
                }
                tvDetail.text = Html.fromHtml(detailInvoice, Html.FROM_HTML_MODE_LEGACY)
            }

        }

        return binding.root
    }

}