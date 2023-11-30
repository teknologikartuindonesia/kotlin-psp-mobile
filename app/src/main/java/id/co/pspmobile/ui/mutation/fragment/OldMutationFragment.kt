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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentOldMutationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.mutation.MutationViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class OldMutationFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private val viewModel: MutationViewModel by activityViewModels()
    private var dateStart = ""
    private var dateEnd = ""

    private lateinit var binding: FragmentOldMutationBinding
    private lateinit var mutationAdapter: MutationAdapter
    private var page: Int = 0
    private var totalPage: Int = 1
    private var isLoading = false
    private lateinit var layoutManager: LinearLayoutManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(context)
//
//        viewModel.oldMutationResponse.observe(viewLifecycleOwner) {
//            when(it is Resource.Loading){
//                true -> showLottieLoader()
//                else -> hideLottieLoader()
//            }
//            if (it is Resource.Success) {
//                mutationAdapter.setMutations(it.value.content)
//                totalPage = it.value.totalPages
//                isLoading = false
//            } else if (it is Resource.Failure) {
//                isLoading = false
//                requireActivity().handleApiError(binding.rvOldMutation, it)
//            }
//        }
//
//        binding.rvOldMutation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                val visibleItemCount = layoutManager.childCount
//                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
//                val total = mutationAdapter.itemCount
//                if (!isLoading && page < totalPage) {
//                    if (visibleItemCount + pastVisibleItem >= total) {
//                        isLoading = true
//                        viewModel.getOldMutation("01-01-2020" + "/" +
//                                LocalDate.now().minusMonths(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
//                            page++)
//                    }
//                }
//                super.onScrolled(recyclerView, dx, dy)
//
//            }
//        })
//        setupRecyclerView()

        binding.rvOldMutation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        viewModel.oldMutationResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                totalPage = it.value.totalPages
                val listResponse = it.value.content
                Log.d("listResponseOld30", listResponse.size.toString())
                if (listResponse != null){
                    mutationAdapter.setMutations(listResponse)
                }
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvOldMutation, it)
            }
        }

        val endDate = LocalDate.now().minusMonths(3)

        dateStart = "01-01-2020"
        dateEnd = endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        setupRecyclerView()
        getData(false)
    }

    fun getData(isOnRefresh: Boolean){
        isLoading = true
        val date = "$dateStart/$dateEnd"
        Log.d("getData", "last30")
        viewModel.getOldMutation(date, page)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupRecyclerView() {
        binding.rvOldMutation.setHasFixedSize(true)
        binding.rvOldMutation.layoutManager = layoutManager
        mutationAdapter = MutationAdapter(requireActivity())
        binding.rvOldMutation.adapter = mutationAdapter
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
        binding = FragmentOldMutationBinding.inflate(inflater)
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