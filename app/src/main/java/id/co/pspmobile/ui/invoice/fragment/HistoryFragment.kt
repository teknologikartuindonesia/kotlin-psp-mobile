package id.co.pspmobile.ui.invoice.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var size: Int = 5
    private var totalPage: Int = 1
    private var isLoading = false

    private lateinit var layoutManager: LinearLayoutManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)
        layoutManager = LinearLayoutManager(context)

        binding.rvInvoice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = historyAdapter.itemCount

                Log.e("visible", "visible "+visibleItemCount.toString())
                Log.e("pass", "pass "+pastVisibleItem.toString())
                Log.e("total","total "+ total.toString())

                if (!isLoading && total == totalPage){
                    if (visibleItemCount + pastVisibleItem>= total){
                        page++
                        isLoading=true
                        viewModel.getPaidInvoice(page++)

                    }
                }
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        viewModel.paidInvoiceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                historyAdapter.setInvoices(it.value.content)
                totalPage = it.value.content.size
                binding.apply {
                    rvInvoice.setHasFixedSize(true)
                    rvInvoice.layoutManager= layoutManager
                    rvInvoice.adapter = historyAdapter
                }
                isLoading=false

            } else if (it is Resource.Failure) {
                isLoading=false
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