package id.co.pspmobile.ui.invoice.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentHistoryInvoiceBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.InvoiceViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private val viewModel: InvoiceViewModel by activityViewModels()

    private lateinit var binding: FragmentHistoryInvoiceBinding
    private lateinit var historyAdapter: HistoryAdapter
    private var page: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.paidInvoiceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                historyAdapter.setInvoices(it.value.content)
                binding.apply {
                    rvInvoice.setHasFixedSize(true)
                    rvInvoice.adapter = historyAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvInvoice, it)
            }
        }

        historyAdapter = HistoryAdapter()
        historyAdapter.setOnDetailClickListener() { invoice ->
            val bottomSheetDetailInvoice = BottomSheetDetailInvoice("User Name", invoice)
            bottomSheetDetailInvoice.show(childFragmentManager, tag)
        }

        viewModel.getPaidInvoice(page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryInvoiceBinding.inflate(inflater)
        return binding.root
    }
}