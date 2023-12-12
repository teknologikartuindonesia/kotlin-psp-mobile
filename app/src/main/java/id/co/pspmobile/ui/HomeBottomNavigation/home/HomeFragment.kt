package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.BuildConfig
import id.co.pspmobile.R
import id.co.pspmobile.data.local.SharePreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.infonews.DefaultBool
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.model.infonews.TagInSearch
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.data.network.responses.customapp.CustomAppResponse
import id.co.pspmobile.databinding.FragmentHomeBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.HomeBottomNavigation.home.menu.MenuActivity
import id.co.pspmobile.ui.HomeBottomNavigation.profile.faq.FaqActivity
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.account.AccountActivity
import id.co.pspmobile.ui.attendance.AttendanceActivity
import id.co.pspmobile.ui.calendar.CalendarActivity
import id.co.pspmobile.ui.dialog.DialogBroadcast
import id.co.pspmobile.ui.digitalCard.DigitalCardActivity
import id.co.pspmobile.ui.donation.DonationActivity
import id.co.pspmobile.ui.invoice.InvoiceActivity
import id.co.pspmobile.ui.mutation.MutationActivity
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import id.co.pspmobile.ui.schedule.ScheduleActivity
import id.co.pspmobile.ui.topup.TopUpActivity
import id.co.pspmobile.ui.topup.history.HistoryTopUpActivity
import id.co.pspmobile.ui.transaction.TransactionActivity


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var menuArray: ArrayList<AppMenu>? = null
    private var otherMenuArray: ArrayList<AppMenu>? = null
    private var otherDefaultMenuArray: ArrayList<DefaultMenuModel>? = null

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
        if (viewModel.getUserData().activeCompany.customApps) {
            getCustomAppData()
        }

        binding.btnHitoryTopup.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryTopUpActivity::class.java))
        }

        binding.seeMoreInfoNews.setOnClickListener {
            try {
                (activity as HomeActivity?)?.info()
            } catch (e: Exception) {
                Log.e("HomeFragment", "onCreateView: $e")
            }
        }

        binding.layoutHomeMoreMenu.setOnClickListener {
            Log.e("HomeFragment", "onCreateView: clicked")
            var intent = Intent(requireContext(), MenuActivity::class.java)

            intent.putExtra("isCustomApp", true)
            intent.putExtra("isSeeMore", false)
            startActivity(intent)
//            openBottomSheet()
        }

        // add onscroll listener when on top, enable swipe refresh
        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.swipeRefreshLayout.isEnabled = scrollY == 0
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            getBalance()
            getInfoHeadline()
            viewModel.checkCredential()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.customAppResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                setupUICustomApp(it.value)
                viewModel.saveLocalCustomApp(it.value)
            }
        }

        viewModel.checkCredential.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val checkCredentialResponse = it.value
                viewModel.saveUserData(checkCredentialResponse)
                if (checkCredentialResponse.activeCompany.customApps) {
                    viewModel.getCustomApp(
                        checkCredentialResponse.activeCompany.id!!,
                        viewModel.getLanguage().uppercase()
                    )
                } else {
                    setUIDefault()
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }

        viewModel.balanceResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                viewModel.saveBalanceData(it.value)
                binding.txtHomeBalance.text = "Rp ${formatCurrency(it.value.balance)}"
                Log.d("HomeFragment", "balanceResponse: ${it.value.balance}")
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }

        val viewPager = binding.viewPagerHome

        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }

        viewPager.setPageTransformer(compositePageTransformer)
        viewPager.setPageTransformer(compositePageTransformer)

        viewModel.infoNewsResponse.observe(viewLifecycleOwner) {
//            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                val infoNewsResponse = it.value
                val infoNews = infoNewsResponse.content
                val infoNewsList = ArrayList(infoNews.subList(0, infoNews.size))
                val infoNewsAdapter = InfoNewsAdapter()
                infoNewsAdapter.setInfoList(infoNewsList, viewModel.getBaseUrl())
                binding.viewPagerHome.adapter = infoNewsAdapter

            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }


        configureFirebase()
        return root


    }

    fun getBalance() {
        viewModel.getBalance()
    }

    fun getInfoHeadline() {
        val defaultBool: List<DefaultBool> =
            listOf(DefaultBool("enable", true), DefaultBool("isHeadline", true))
        val tags = viewModel.getUserData().tags as MutableList<String>
        tags.add("info")
        val tagInSearch: List<TagInSearch> =
            listOf(TagInSearch("tags", tags))
        val body = ModelInfoNews(defaultBool, emptyList(), tagInSearch)
        viewModel.getInfoNews(body, 0)
    }

    fun configureFirebase() {
        val current = SharePreferences.getFbToken(requireContext())
        val serverKeyId = BuildConfig.SERVER_KEY_ID
        Log.d(
            "HomeFragment",
            "configureFirebase: \n $current \n ${viewModel.getUserData().user.firebase.token}"
        )
        if (!current.isNullOrEmpty() && current != viewModel.getUserData().user.firebase.token) {
            viewModel.saveFirebaseToken(
                current,
                serverKeyId
            )
        }
    }

    fun getCustomAppData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configureAssets() {
        if (viewModel.getUserData().activeCompany.customApps) {
            showImage(binding.imgHomeLogo, viewModel.getLocalCustomApp()?.app_icon_url ?: "psp.svg")
            binding.txtHomeCompanyName.text =
                if (viewModel.getLocalCustomApp()?.app_display_name.isNullOrEmpty())
                    viewModel.getUserData().activeCompany.name else viewModel.getLocalCustomApp()?.app_display_name
        } else {
            showImage(binding.imgHomeLogo, "psp.svg")
            binding.txtHomeCompanyName.text = viewModel.getUserData().activeCompany.name
        }
        Log.d("HomeFragment", "trying showImage")
    }

    fun configureMenu() {

        val menuList = ArrayList<AppMenu>()

        if (viewModel.getUserData().activeCompany.customApps) {
//         pake custom app
            if (viewModel.getLocalCustomApp() != null) {
                setupUICustomApp(viewModel.getLocalCustomApp()!!)
            } else {
                viewModel.getCustomApp(
                    viewModel.getUserData().activeCompany.id!!,
                    viewModel.getLanguage().uppercase()
                )
            }
            var defaultMenuList = ArrayList<MenuModel>()
            var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
//            defaultMenuList.add(DefaultMenuModel("Topup", R.drawable.ic_home_topup, Intent(requireContext(), MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Invoice", R.drawable.ic_home_invoice, Intent(requireContext(), MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Mutation", R.drawable.ic_home_mutation, Intent(requireContext(), MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Transaction", R.drawable.ic_home_transaction, Intent(requireContext(), TransactionActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Attendance", R.drawable.ic_home_attendance, Intent(requireContext(), AttendanceActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Digital Card", R.drawable.ic_home_digital_card, Intent(requireContext(), DigitalCardActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Account", R.drawable.ic_home_account, Intent(requireContext(), AccountActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Donation", R.drawable.ic_home_donation, Intent(requireContext(), DonationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Schedule", R.drawable.ic_home_schedule, Intent(requireContext(), ScheduleActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Calendar Academic", R.drawable.ic_home_calendar, Intent(requireContext(), CalendarActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Support", R.drawable.ic_home_support, Intent(requireContext(), MutationActivity::class.java)))

//            val menuAdapter = MenuAdapter()
//            menuAdapter.setMenuList(menuArray!!, viewModel.getBaseUrl(), viewModel.getUserData().activeCompany.id)
//            menuAdapter.setOnclickListener { view ->
//                Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//            }

//            rv.adapter = menuAdapter
        } else {
            // ga pake custom app
            setUIDefault()
        }


    }

    fun setupUICustomApp(custom: CustomAppResponse) {
        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager =
            GridLayoutManager(requireContext(), rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager

        val baseUrl = viewModel.getBaseUrl()

        val defaultMenuList = ArrayList<MenuModel>()
        for (i in custom.app_menu) {
            val imageUrl =
                "${baseUrl}/main_a/web_view/custom_apps/icon/${viewModel.getUserData().activeCompany.id}/${i.menu_icon_url}"
            val intent = when (i.menu_path) {
                "/topup" -> Intent(requireContext(), TopUpActivity::class.java)
                "/invoice" -> Intent(requireContext(), InvoiceActivity::class.java)
                "/mutation" -> Intent(requireContext(), MutationActivity::class.java)
                "/transaction-history" -> Intent(requireContext(), TransactionActivity::class.java)
                "/attendance" -> Intent(requireContext(), AttendanceActivity::class.java)
                "/digital-card" -> Intent(requireContext(), DigitalCardActivity::class.java)
                "/account" -> Intent(requireContext(), AccountActivity::class.java)
                "/donation" -> Intent(requireContext(), DonationActivity::class.java)
                "/schedule" -> Intent(requireContext(), ScheduleActivity::class.java)
                "/calendar" -> Intent(requireContext(), CalendarActivity::class.java)
                "/support" -> Intent(requireContext(), FaqActivity::class.java)
                "/etc" -> Intent(requireContext(), FaqActivity::class.java)
                else ->
                    if (i.menu_path.contains("http")) Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(i.menu_path)
                    )
                    else Intent(requireContext(), MenuActivity::class.java)
            }
            if (i.menu_is_show) defaultMenuList.add(MenuModel(i.menu_name, imageUrl, intent))
        }
        Log.d("HomeFragment", "setupUICustomApp(${defaultMenuList.size}): $defaultMenuList")
        if (defaultMenuList.isEmpty()) {
            setUIDefault()
        } else {
            val menuAdapter = MenuAdapter()
            menuAdapter.setAllMenuList(defaultMenuList)
            Log.d("HomeFragment", "setupUICustomApp: $defaultMenuList")
            val intentMoreMenu = Intent(requireContext(), MenuActivity::class.java)
            intentMoreMenu.putExtra("isCustomApp", true)
            intentMoreMenu.putExtra("isSeeMore", true)
            intentMoreMenu.putExtra(
                "allMenu",
                Gson().toJson(defaultMenuList.subList(7, defaultMenuList.size))
            )
            val limitedMenuList = ArrayList(defaultMenuList.subList(0, 7))
            limitedMenuList.add(
                MenuModel(
                    resources.getString(R.string.more),
                    "more.svg",
                    intentMoreMenu
                )
            )
            Log.d("HomeFragment", "setupUICustomApp: $defaultMenuList")
            menuAdapter.setMenuList(
                limitedMenuList,
                viewModel.getBaseUrl(),
                viewModel.getUserData().activeCompany.id!!
            )
            rv.adapter = menuAdapter
            showImage(binding.imgHomeLogo, custom.app_icon_url)
            binding.txtHomeCompanyName.text = if (custom.app_display_name.isNullOrEmpty())
                viewModel.getUserData().activeCompany.name else custom.app_display_name
        }
    }

    fun setUIDefault() {
        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager =
            GridLayoutManager(requireContext(), rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager

        var defaultMenuList = ArrayList<DefaultMenuModel>()
        var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
        var seeAllDefaultMenuList = ArrayList<DefaultMenuModel>()
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.topup),
                R.drawable.ic_home_topup,
                Intent(requireContext(), TopUpActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.invoice),
                R.drawable.ic_home_invoice,
                Intent(requireContext(), InvoiceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.mutation),
                R.drawable.ic_home_mutation,
                Intent(requireContext(), MutationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.transaction),
                R.drawable.ic_home_transaction,
                Intent(requireContext(), TransactionActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.attendance),
                R.drawable.ic_home_attendance,
                Intent(requireContext(), AttendanceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.digital_card),
                R.drawable.ic_home_digital_card,
                Intent(requireContext(), DigitalCardActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.account),
                R.drawable.ic_home_account,
                Intent(requireContext(), AccountActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.donation),
                R.drawable.ic_home_donation,
                Intent(requireContext(), DonationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.schedule),
                R.drawable.ic_home_schedule,
                Intent(requireContext(), ScheduleActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.calendar),
                R.drawable.ic_home_calendar,
                Intent(requireContext(), CalendarActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.support),
                R.drawable.ic_home_support,
                Intent(requireContext(), FaqActivity::class.java)
            )
        )

        otherDefaultMenuList = ArrayList(defaultMenuList.subList(7, defaultMenuList.size))
        seeAllDefaultMenuList = ArrayList(defaultMenuList)
        defaultMenuList = ArrayList(defaultMenuList.subList(0, 7))
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.more),
                R.drawable.ic_home_more_menu,
                Intent(requireContext(), MenuActivity::class.java)
            )
        )
        val menuAdapter = DefaultMenuAdapter()
        menuAdapter.setMenuList(defaultMenuList, requireContext())
        menuAdapter.setAllMenuList(seeAllDefaultMenuList, requireContext(), requireActivity())
        menuAdapter.setOtherMenuList(otherDefaultMenuList, requireContext(), requireActivity())
        otherDefaultMenuArray = otherDefaultMenuList
        rv.adapter = menuAdapter
        showImage(binding.imgHomeLogo, "psp.svg")
        binding.txtHomeCompanyName.text = viewModel.getUserData().activeCompany.name
    }

    fun openBottomSheet() {
        val bottomSheetOtherMenuFragment =
            otherDefaultMenuArray?.let {
                BottomSheetOtherMenuFragment(
                    it,
                    requireContext(),
                    requireActivity()
                )
            }
        bottomSheetOtherMenuFragment?.show(
            (requireActivity()).supportFragmentManager,
            bottomSheetOtherMenuFragment.tag
        )
    }

    fun showImage(imageView: ImageView, iconUrl: String) {
        Log.d("HomeFragment", "showImage: $imageView $iconUrl")
        if (iconUrl == "psp.svg") {
            imageView.setImageResource(R.drawable.psp_mobile_white)
            return
        }
        try {
            val imgUrl =
                "${viewModel.getBaseUrl()}/main_a/web_view/custom_apps/icon/${viewModel.getUserData().activeCompany.id}/$iconUrl"
            val imageLoader = ImageLoader.Builder(requireContext())
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            val imageRequest = ImageRequest.Builder(requireContext())
                .data(imgUrl)
                .target(imageView)
                .transformations(RoundedCornersTransformation())
                .size(70, 70)
                .scale(coil.size.Scale.FILL)
                .build()
            val disposable = imageLoader.enqueue(imageRequest)
            Log.d("HomeFragment", "showImage: $imageView $imgUrl")
        } catch (e: Exception) {
            Log.e("HomeFragment", "showImage: $e")
        }
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
