package id.co.pspmobile.ui.invoice.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentInvoiceBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.InvoiceViewModel

@AndroidEntryPoint
class InvoiceFragment() : Fragment() {

    private val viewModel: InvoiceViewModel by activityViewModels()

    private lateinit var binding: FragmentInvoiceBinding
    private lateinit var invoiceAdapter: InvoiceAdapter
    private var page: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.unpaidInvoiceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                invoiceAdapter.setInvoices(it.value.content)
                binding.apply {
                    rvInvoice.setHasFixedSize(true)
                    rvInvoice.adapter = invoiceAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvInvoice, it)
            }
        }

        invoiceAdapter = InvoiceAdapter()
        invoiceAdapter.setOnPayClickListener { invoice ->
            AlertDialog.Builder(requireContext())
                .setMessage(invoice.title)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        viewModel.getUnpaidInvoice(page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceBinding.inflate(inflater)
        return binding.root
    }

}