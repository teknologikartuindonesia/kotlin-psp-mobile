package id.co.pspmobile.ui.invoice.fragment

import android.os.Bundle
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
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

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

        layoutManager = LinearLayoutManager(context)

        binding.rvInvoice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = historyAdapter.itemCount

                if (!isLoading && total == totalPage) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        page++
                        isLoading = true
                        viewModel.getPaidInvoice(page++)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModel.paidInvoiceResponse.observe(viewLifecycleOwner) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                historyAdapter.setInvoices(it.value.content)
                totalPage = it.value.content.size
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvInvoice, it)
            }
        }

//        historyAdapter = HistoryAdapter(requireActivity())
//        historyAdapter.setOnDetailClickListener() { invoice ->
//            val bottomSheetDetailInvoice = BottomSheetDetailInvoice("User Name", invoice)
//            bottomSheetDetailInvoice.show(childFragmentManager, tag)
//        }
        setupRecyclerView()
        viewModel.getPaidInvoice(page)
    }

    private fun setupRecyclerView() {
        binding.rvInvoice.setHasFixedSize(true)
        binding.rvInvoice.layoutManager = layoutManager
        historyAdapter = HistoryAdapter(requireActivity())
        binding.rvInvoice.adapter = historyAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryInvoiceBinding.inflate(inflater)
        return binding.root
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