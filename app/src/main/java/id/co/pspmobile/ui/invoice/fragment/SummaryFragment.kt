package id.co.pspmobile.ui.invoice.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentSummaryInvoiceBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.InvoiceViewModel

@AndroidEntryPoint
class SummaryFragment : Fragment() {
    private val viewModel: InvoiceViewModel by activityViewModels()

    private lateinit var binding: FragmentSummaryInvoiceBinding
    private lateinit var summaryAdapter: SummaryAdapter
    private var page: Int = 0
    private var currentSize: Int = 0
    private var size: Int = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.allInvoiceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                summaryAdapter.setInvoices(it.value.content)
                currentSize = currentSizeinvoice(it.value.content.size)

                binding.btnPrevious.isEnabled = page != 0
                binding.btnNext.isEnabled = currentSize == size

                binding.apply {
                    rvInvoice.setHasFixedSize(true)
                    rvInvoice.adapter = summaryAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvInvoice, it)
            }
        }

        summaryAdapter = SummaryAdapter()
        viewModel.getAllInvoice(page)

        binding.btnPrevious.setOnClickListener {
            page--
            viewModel.getAllInvoice(page)
        }
        binding.btnNext.setOnClickListener {
            if (currentSize == size) {
                page++
                viewModel.getAllInvoice(page)
            }
        }
    }

    fun currentSizeinvoice(size: Int): Int {
        var count = size / (page + 1)
        return count
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryInvoiceBinding.inflate(inflater)
        return binding.root
    }
}