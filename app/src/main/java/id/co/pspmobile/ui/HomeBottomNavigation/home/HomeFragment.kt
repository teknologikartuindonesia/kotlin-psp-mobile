package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.infonews.DefaultBool
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.model.infonews.TagInSearch
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.databinding.FragmentHomeBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var menuArray: ArrayList<AppMenu>? = null
    private var otherMenuArray: ArrayList<AppMenu>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        configureMenu()
        configureAssets()
        getBalance()
        getInfoHeadline()
        if (viewModel.getUserData().activeCompany.customApps){
            getCustomAppData()
        }

        viewModel.balanceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                binding.txtHomeBalance.text = "Rp ${it.value.balance}"
                Log.d("HomeFragment", "balanceResponse: ${it.value.balance}")
            }else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }

        viewModel.infoNewsResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                val infoNewsResponse = it.value
                Log.d("HomeFragment", "infoNewsResponse: $infoNewsResponse")
                val infoNews = infoNewsResponse.content
                val infoNewsList = ArrayList(infoNews.subList(0, infoNews.size))
                val infoNewsAdapter = InfoNewsAdapter()
                infoNewsAdapter.setInfoList(infoNewsList, viewModel.getBaseUrl())
                binding.rvInfoNews.adapter = infoNewsAdapter

            }else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }
        return root

    }

    fun getBalance(){
        viewModel.getBalance()
    }

    fun getInfoHeadline(){
        val defaultBool: List<DefaultBool> = listOf(DefaultBool("isHeadline", true))
        val tagInSearch: List<TagInSearch> = listOf(TagInSearch("tags", viewModel.getUserData().tags))
        val body = ModelInfoNews(defaultBool, emptyList(), tagInSearch)
        viewModel.getInfoNews(body,0)
    }

    fun getCustomAppData(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configureAssets(){
        showImage(binding.imgHomeLogo, "psp.svg")
        Log.d("HomeFragment", "trying showImage")
        binding.txtHomeCompanyName.text = viewModel.getUserData().activeCompany.name
    }
    fun configureMenu(){

        val menuList = ArrayList<AppMenu>()
        val defaultMenuList = ArrayList<AppMenu>()

        defaultMenuList.add(AppMenu("63dfc62c31bd560297c1238c", "1686195840.svg", true, "Top Up", "/topup", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c1238d", "invoice.svg", true, "Invoice", "/invoice", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c1238e", "mutation.svg", true, "Mutation", "/mutation", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c1238f", "transaction.svg", true, "Transaction", "/transaction-history", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12390", "attendance.svg", true, "Attendance", "/attendance", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12391", "card.svg", true, "Digital Card", "/digital-card", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12392", "account.svg", true, "Account", "/account", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12393", "donation.svg", true, "Donation", "/donation", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12394", "schedule.svg", true, "Schedule", "/schedule", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12395", "calendar.svg", true, "Calendar Academic", "/calendar", "PAGE"))
        defaultMenuList.add(AppMenu("63dfc62c31bd560297c12396", "support.svg", true, "Support", "/etc", "PAGE"))
        // split 7 menuList to menuArray then the rest to otherMenuArray
        menuArray = ArrayList(defaultMenuList.subList(0, 7))
        menuArray!!.add(AppMenu("63dfc62c31bd560297c12397", "more.svg", true, "More", "/more", "PAGE"))
        otherMenuArray = ArrayList(defaultMenuList.subList(7, defaultMenuList.size))

        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager = GridLayoutManager(requireContext(), rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager


        val menuAdapter = MenuAdapter()
        menuAdapter.setMenuList(menuArray!!, viewModel.getBaseUrl(), viewModel.getUserData().activeCompany.id)
        menuAdapter.setOnclickListener { view ->
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        }

        rv.adapter = menuAdapter
    }
    fun showImage(imageView: ImageView, iconUrl: String){
        Log.d("HomeFragment", "showImage: $imageView $iconUrl")
        try {
            val imgUrl = "${viewModel.getBaseUrl()}/main_a/web_view/custom_apps/icon/${viewModel.getUserData().activeCompany.id}/$iconUrl"
            val imageLoader = ImageLoader.Builder(requireContext())
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            val imageRequest = ImageRequest.Builder(requireContext())
                .data(imgUrl)
                .target(imageView)
                .transformations(RoundedCornersTransformation())
                .build()
            val disposable = imageLoader.enqueue(imageRequest)
            Log.d("HomeFragment", "showImage: $imageView $imgUrl")
        } catch (e: Exception){
            Log.e("HomeFragment", "showImage: $e")
        }
    }
}