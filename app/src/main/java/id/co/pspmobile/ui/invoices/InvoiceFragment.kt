package id.co.pspmobile.ui.invoices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.tki.internal.ui.education.InvoiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.invoices.InvoicesRequestDto
import id.co.pspmobile.databinding.FragmentInvoiceBinding
import id.co.pspmobile.ui.main.InvoiceViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InvoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private lateinit var binding : FragmentInvoiceBinding
    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var invoiceAdapter: InvoiceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceBinding.inflate(layoutInflater)

        viewModel.invoice.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val listInvoiceResDto = it.value
                with(binding) {
                    invoiceAdapter.setInvoiceList(listInvoiceResDto,viewModel.getBaseUrl())
                    binding.apply {
                        rvInvoice.setHasFixedSize(true)
                        rvInvoice.adapter = invoiceAdapter
                        rvInvoice.getLayoutManager()?.onRestoreInstanceState(viewModel.recyclerViewState)
                    }
                }
            }
        }

        invoiceAdapter = InvoiceAdapter()

        viewModel.getDataInvoice()

        return inflater.inflate(R.layout.fragment_invoice, container, false)

    }

}