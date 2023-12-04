package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.infonews.DefaultBool
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.model.infonews.TagInSearch
import id.co.pspmobile.databinding.FragmentInformationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.mutation.fragment.MutationAdapter
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

@AndroidEntryPoint
class InformationFragment : Fragment() {

    private lateinit var binding : FragmentInformationBinding
    private val viewModel: InformationViewModel by viewModels()
    private lateinit var infoAdapter: InformationAdapter
    private lateinit var tagsAdapter: TagsAdapter
    private var page = 0
    private var selectedTag = mutableListOf<String>()
    private var allTags = ArrayList<ModelTags>()
    private var allTagsFromUser = mutableListOf<String>()

    private var size: Int = 5
    private var totalPage: Int = 1
    private var isLoading = false
    var isAll = true

    private lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(context)

        binding.rvInfo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = infoAdapter.itemCount
                Log.d("InformationFragment", "onScrolled: $visibleItemCount $pastVisibleItem $total")
                Log.d("InformationFragment", "onScrolled: $isLoading $page $totalPage")
                if (!isLoading && page < totalPage) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        page++
                        getInfo(true)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModel.infoNewsResponse.observe(viewLifecycleOwner){
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                totalPage = it.value.totalPages
                val listResponse = it.value.content
                if (listResponse != null) {
                    infoAdapter.setInformations(listResponse)
                }
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvInfo, it)
            }
        }
        allTagsFromUser = viewModel.getUserData().tags as MutableList<String>
        selectedTag = viewModel.getUserData().tags as MutableList<String>

        setupRecyclerView()
        setupTagsRecyclerView()
        getInfo(false)

        binding.btnAll.setOnClickListener {
            if (isAll){
                return@setOnClickListener
            }
            getAll()
        }
    }

    fun getAll(){
        isAll = true
        selectedTag.clear()
        selectedTag = allTagsFromUser
        tagsAdapter.setSelectedTag(selectedTag)
        tagsAdapter.setDefaultTag(allTagsFromUser)
        tagsAdapter.setAll(true)
        page = 0
        infoAdapter.clear()
        binding.btnAll.background = resources.getDrawable(R.drawable.tab_layout_bg)
        binding.btnAll.setTextColor(resources.getColor(R.color.white))
        getInfo(false)
    }

    fun getInfo(isOnrefresh: Boolean){
        isLoading = true
        if (isAll){
            if(!selectedTag.contains("info")){
                selectedTag.add("info")
            }
        }
        val defaultBool: List<DefaultBool> =
            listOf(DefaultBool("enable", true), DefaultBool("isHeadline", true))
        val tagInSearch: List<TagInSearch> =
            listOf(TagInSearch("tags", selectedTag))
        val body = ModelInfoNews(defaultBool, emptyList(), tagInSearch)
        binding.txtSelectedTags.text = selectedTag.toString()
        viewModel.getInfoNews(body, page)
    }
    private fun setupRecyclerView() {
        binding.rvInfo.setHasFixedSize(true)
        binding.rvInfo.layoutManager = layoutManager
        infoAdapter = InformationAdapter()
        infoAdapter.setContext(requireContext())
        infoAdapter.setBaseUrl(viewModel.getBaseUrl())
        binding.rvInfo.adapter = infoAdapter
    }

    private fun setupTagsRecyclerView(){
        binding.rvTags.setHasFixedSize(true)
        binding.rvTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter(requireContext())
        tagsAdapter.setDefaultTag(allTagsFromUser)
        tagsAdapter.setSelectedTag(selectedTag)
        tagsAdapter.setOnItemClickListener { v ->
            Log.d("TagsAdapter", "InformationFragment: clicked $v  \n ${v.tag}")

            val tagTarget = tagsAdapter.clickedTag // clickedTag// requireView().tag as String
            Log.d("TagsAdapter", "InformationFragment: clicked $tagTarget")
            Log.d("TagsAdapter", "InformationFragment: clicked $selectedTag")

            tagsAdapter.setAll(false)
            var temp = if (!isAll) selectedTag else mutableListOf()
            isAll = false
            if(temp.contains(tagTarget)){
                temp.remove(tagTarget)
            } else {
                temp.add(tagTarget)
            }
            selectedTag = temp

            binding.btnAll.background = resources.getDrawable(R.drawable.tab_layout_outline)
            binding.btnAll.setTextColor(resources.getColor(R.color.primary))
            if (selectedTag.size == 0){
                getAll()
                return@setOnItemClickListener
            }
            tagsAdapter.setSelectedTag(selectedTag)
            tagsAdapter.clickedTag = ""
            Log.d("TagsAdapter", "InformationFragment: clicked $selectedTag")
            page = 0
            infoAdapter.clear()

            getInfo(false)
        }
        binding.rvTags.adapter = tagsAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
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