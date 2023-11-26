package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.content.Intent
import android.content.res.Resources
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
import androidx.viewpager2.widget.ViewPager2
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
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.account.AccountActivity
import id.co.pspmobile.ui.attendance.AttendanceActivity
import id.co.pspmobile.ui.calendar.CalendarActivity
import id.co.pspmobile.ui.digitalCard.DigitalCardActivity
import id.co.pspmobile.ui.donation.DonationActivity
import id.co.pspmobile.ui.invoice.InvoiceActivity
import id.co.pspmobile.ui.mutation.MutationActivity
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

        binding.layoutHomeMoreMenu.setOnClickListener {
            openBottomSheet()
        }

        viewModel.balanceResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                viewModel.saveBalanceData(it.value)
                binding.txtHomeBalance.text = "Rp ${formatCurrency(it.value.balance)}"
                getNotification()
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
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                val infoNewsResponse = it.value
                Log.d("HomeFragment", "infoNewsResponse: $infoNewsResponse")
                val infoNews = infoNewsResponse.content
                val infoNewsList = ArrayList(infoNews.subList(0, infoNews.size))
                val infoNewsAdapter = InfoNewsAdapter()
                infoNewsAdapter.setInfoList(infoNewsList, viewModel.getBaseUrl())
                binding.viewPagerHome.adapter = infoNewsAdapter

            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.progressbar, it)
            }
        }
        return root


    }

    fun getBalance() {
        viewModel.getBalance()
    }

    fun getInfoHeadline() {
        val defaultBool: List<DefaultBool> =
            listOf(DefaultBool("enable", true), DefaultBool("isHeadline", true))
        val tagInSearch: List<TagInSearch> =
            listOf(TagInSearch("tags", viewModel.getUserData().tags))
        val body = ModelInfoNews(defaultBool, emptyList(), tagInSearch)
        viewModel.getInfoNews(body, 0)
    }

    fun getCustomAppData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configureAssets() {
        showImage(binding.imgHomeLogo, "psp.svg")
        Log.d("HomeFragment", "trying showImage")
        binding.txtHomeCompanyName.text = viewModel.getUserData().activeCompany.name
    }

    fun configureMenu() {

        val menuList = ArrayList<AppMenu>()


        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager =
            GridLayoutManager(requireContext(), rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager

//        if(viewModel.getUserData().activeCompany.customApps){
        // pake custom app
//            var defaultMenuList = ArrayList<DefaultMenuModel>()
//            var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
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
//
//            val menuAdapter = MenuAdapter()
//            menuAdapter.setMenuList(menuArray!!, viewModel.getBaseUrl(), viewModel.getUserData().activeCompany.id)
//            menuAdapter.setOnclickListener { view ->
//                Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//            }

//            rv.adapter = menuAdapter
//        } else {
        // ga pake custom app
        var defaultMenuList = ArrayList<DefaultMenuModel>()
        var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
        defaultMenuList.add(
            DefaultMenuModel(
                "Topup",
                R.drawable.ic_home_topup,
                Intent(requireContext(), TopUpActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Invoice",
                R.drawable.ic_home_invoice,
                Intent(requireContext(), InvoiceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Mutation",
                R.drawable.ic_home_mutation,
                Intent(requireContext(), MutationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Transaction",
                R.drawable.ic_home_transaction,
                Intent(requireContext(), TransactionActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Attendance",
                R.drawable.ic_home_attendance,
                Intent(requireContext(), AttendanceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Digital Card",
                R.drawable.ic_home_digital_card,
                Intent(requireContext(), DigitalCardActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Account",
                R.drawable.ic_home_account,
                Intent(requireContext(), AccountActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Donation",
                R.drawable.ic_home_donation,
                Intent(requireContext(), DonationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Schedule",
                R.drawable.ic_home_schedule,
                Intent(requireContext(), ScheduleActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Calendar Academic",
                R.drawable.ic_home_calendar,
                Intent(requireContext(), CalendarActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                "Support",
                R.drawable.ic_home_support,
                Intent(requireContext(), MutationActivity::class.java)
            )
        )

        otherDefaultMenuList = ArrayList(defaultMenuList.subList(7, defaultMenuList.size))
        defaultMenuList = ArrayList(defaultMenuList.subList(0, 7))
        defaultMenuList.add(
            DefaultMenuModel(
                "More",
                R.drawable.ic_home_more_menu,
                Intent(requireContext(), MutationActivity::class.java)
            )
        )
        val menuAdapter = DefaultMenuAdapter()
        menuAdapter.setMenuList(defaultMenuList, requireContext())
        menuAdapter.setOtherMenuList(otherDefaultMenuList, requireContext(), requireActivity())
        otherDefaultMenuArray = otherDefaultMenuList
        rv.adapter = menuAdapter
//        }


    }

    fun openBottomSheet(){
        val bottomSheetOtherMenuFragment =
            otherDefaultMenuArray?.let { BottomSheetOtherMenuFragment(it, requireContext(), requireActivity()) }
        bottomSheetOtherMenuFragment?.show(
            (requireActivity()).supportFragmentManager,
            bottomSheetOtherMenuFragment.tag
        )
    }

    fun showImage(imageView: ImageView, iconUrl: String) {
        Log.d("HomeFragment", "showImage: $imageView $iconUrl")
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
                .build()
            val disposable = imageLoader.enqueue(imageRequest)
            Log.d("HomeFragment", "showImage: $imageView $imgUrl")
        } catch (e: Exception) {
            Log.e("HomeFragment", "showImage: $e")
        }
    }

    fun getNotification(){
//        var type: String = activity?.intent?.getStringExtra("type").toString()
//        Toast.makeText(context, "this, ${type}", Toast.LENGTH_SHORT).show()
//        when(type){
//            "invoice" ->
//                startActivity(Intent(requireContext(), HistoryTopUpActivity::class.java))
//
//        }
    }
}
