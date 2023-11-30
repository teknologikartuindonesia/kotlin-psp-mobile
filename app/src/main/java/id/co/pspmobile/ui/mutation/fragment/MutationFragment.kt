package id.co.pspmobile.ui.mutation.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LOGGER
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentMutationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.mutation.MutationViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MutationFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: MutationViewModel by activityViewModels()
    private var dateStart = ""
    private var dateEnd = ""

    private lateinit var binding: FragmentMutationBinding
    private lateinit var mutationAdapter: MutationAdapter
    private var page: Int = 0
    private var size: Int = 5
    private var totalPage: Int = 1
    private var isLoading = false

    private lateinit var layoutManager: LinearLayoutManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(context)

        val dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val startDate = LocalDate.now().minusMonths(3)
        val endDate = LocalDate.now()
        dateStart = startDate.format(dtf)
        dateEnd = endDate.format(dtf)
        val date = "$dateStart/$dateEnd"

//        binding.rvMutation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val visibleItemCount = layoutManager.childCount
//                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
//                val total = mutationAdapter.itemCount
//                if (!isLoading && page < totalPage) {
//                    if (visibleItemCount + pastVisibleItem >= total) {
//                        isLoading = true
//                        viewModel.getMutation(date, page++)
//                    }
//                }
//                super.onScrolled(recyclerView, dx, dy)
//
//            }
//        })
//        viewModel.mutationResponse.observe(viewLifecycleOwner) {
//            when (it is Resource.Loading) {
//                true -> showLottieLoader()
//                else -> hideLottieLoader()
//            }
//            if (it is Resource.Success) {
//                mutationAdapter.setMutations(it.value.content)
//                totalPage = it.value.totalPages
//                isLoading = false
//            } else if (it is Resource.Failure) {
//                isLoading = false
//                requireActivity().handleApiError(binding.rvMutation, it)
//            }
//        }

        binding.rvMutation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total  = mutationAdapter.itemCount
                if (!isLoading && page < totalPage){
                    if (visibleItemCount + pastVisibleItem>= total){
                        page++
                        getData(false)
                    }
                }
                binding.swipeRefreshLayout.isEnabled = layoutManager.findFirstCompletelyVisibleItemPosition() == 0; // 0 is for first item position
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModel.mutationResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                totalPage = it.value.totalPages
                val listResponse = it.value.content
                Log.d("listResponseLast30", listResponse.size.toString())
                if (listResponse != null){
                    mutationAdapter.setMutations(listResponse)
                }
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvMutation, it)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this)
        setupRecyclerView()
        getData(false)
    }

    fun getData(isOnRefresh: Boolean){
        isLoading = true
        val date = "$dateStart/$dateEnd"
        Log.d("getData", "last30")
        viewModel.getMutation(date, page)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        Log.d("onRefresh", "last30")
        mutationAdapter.clear()
        page = 0
        getData(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMutationBinding.inflate(inflater)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvMutation.setHasFixedSize(true)
        binding.rvMutation.layoutManager = layoutManager
        mutationAdapter = MutationAdapter(requireActivity())
        binding.rvMutation.adapter = mutationAdapter
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