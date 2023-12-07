package id.co.pspmobile.ui.HomeBottomNavigation.home.menu

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.databinding.ActivityMenuBinding
import id.co.pspmobile.ui.HomeBottomNavigation.home.DefaultMenuAdapter
import id.co.pspmobile.ui.HomeBottomNavigation.home.DefaultMenuModel
import id.co.pspmobile.ui.HomeBottomNavigation.home.HomeViewModel
import id.co.pspmobile.ui.HomeBottomNavigation.home.MenuAdapter
import id.co.pspmobile.ui.HomeBottomNavigation.home.MenuModel
import id.co.pspmobile.ui.HomeBottomNavigation.profile.faq.FaqActivity
import id.co.pspmobile.ui.account.AccountActivity
import id.co.pspmobile.ui.attendance.AttendanceActivity
import id.co.pspmobile.ui.calendar.CalendarActivity
import id.co.pspmobile.ui.digitalCard.DigitalCardActivity
import id.co.pspmobile.ui.donation.DonationActivity
import id.co.pspmobile.ui.invoice.InvoiceActivity
import id.co.pspmobile.ui.mutation.MutationActivity
import id.co.pspmobile.ui.schedule.ScheduleActivity
import id.co.pspmobile.ui.topup.TopUpActivity
import id.co.pspmobile.ui.transaction.TransactionActivity

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val viewModel: HomeViewModel by viewModels()

    private var otherDefaultMenuArray: ArrayList<DefaultMenuModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extra = intent.extras
        val allMenu = extra?.getString("allMenu")
        val seeMore = extra?.getBoolean("isSeeMore")
        val isCustomApp = extra?.getBoolean("isCustomApp")
        var menuArray = ArrayList<MenuModel>()
        if (isCustomApp == true) {
            val custom = viewModel.getLocalCustomApp()
            if (custom != null) {
                val defaultMenuList = ArrayList<MenuModel>()
                for (i in custom.app_menu) {
                    val imageUrl =
                        "${viewModel.getBaseUrl()}/main_a/web_view/custom_apps/icon/${viewModel.getUserData().activeCompany.id}/${i.menu_icon_url}"
                    val intent = when (i.menu_path) {
                        "/topup" -> Intent(this@MenuActivity, TopUpActivity::class.java)
                        "/invoice" -> Intent(this@MenuActivity, InvoiceActivity::class.java)
                        "/mutation" -> Intent(this@MenuActivity, MutationActivity::class.java)
                        "/transaction-history" -> Intent(
                            this@MenuActivity,
                            TransactionActivity::class.java
                        )

                        "/attendance" -> Intent(this@MenuActivity, AttendanceActivity::class.java)
                        "/digital-card" -> Intent(
                            this@MenuActivity,
                            DigitalCardActivity::class.java
                        )

                        "/account" -> Intent(this@MenuActivity, AccountActivity::class.java)
                        "/donation" -> Intent(this@MenuActivity, DonationActivity::class.java)
                        "/schedule" -> Intent(this@MenuActivity, ScheduleActivity::class.java)
                        "/calendar" -> Intent(this@MenuActivity, CalendarActivity::class.java)
                        "/support" -> Intent(this@MenuActivity, FaqActivity::class.java)
                        "/etc" -> Intent(this@MenuActivity, FaqActivity::class.java)
                        else ->
                            if (i.menu_path.contains("http")) Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(i.menu_path)
                            )
                            else Intent(this@MenuActivity, MenuActivity::class.java)
                    }
                    if (i.menu_is_show) defaultMenuList.add(
                        MenuModel(
                            i.menu_name,
                            imageUrl,
                            intent
                        )
                    )
                }
                menuArray = defaultMenuList
                Log.e("MenuActivity", "onCreate: ${seeMore}")
                if (seeMore == false) {
                    configureCustomMenu(menuArray)
                } else {
                    configureCustomMenu(menuArray.slice(7..menuArray.size - 1) as ArrayList<MenuModel>)
                }

            }
        } else {
            configureMenu()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    fun configureCustomMenu(menuArray: ArrayList<MenuModel>) {
        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager =
            GridLayoutManager(this, rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager

        val menuAdapter = MenuAdapter()
        rv.adapter = menuAdapter
        menuAdapter.setMenuList(
            menuArray,
            viewModel.getBaseUrl(),
            viewModel.getUserData().activeCompany.id
        )
        rv.adapter = menuAdapter
    }

    fun configureMenu() {

        val menuList = ArrayList<AppMenu>()


        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager =
            GridLayoutManager(this, rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager

//        if(viewModel.getUserData().activeCompany.customApps){
        // pake custom app
//            var defaultMenuList = ArrayList<DefaultMenuModel>()
//            var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
//            defaultMenuList.add(DefaultMenuModel("Topup", R.drawable.ic_home_topup, Intent(this, MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Invoice", R.drawable.ic_home_invoice, Intent(this, MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Mutation", R.drawable.ic_home_mutation, Intent(this, MutationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Transaction", R.drawable.ic_home_transaction, Intent(this, TransactionActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Attendance", R.drawable.ic_home_attendance, Intent(this, AttendanceActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Digital Card", R.drawable.ic_home_digital_card, Intent(this, DigitalCardActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Account", R.drawable.ic_home_account, Intent(this, AccountActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Donation", R.drawable.ic_home_donation, Intent(this, DonationActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Schedule", R.drawable.ic_home_schedule, Intent(this, ScheduleActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Calendar Academic", R.drawable.ic_home_calendar, Intent(this, CalendarActivity::class.java)))
//            defaultMenuList.add(DefaultMenuModel("Support", R.drawable.ic_home_support, Intent(this, MutationActivity::class.java)))
//
//            val menuAdapter = MenuAdapter()
//            menuAdapter.setMenuList(menuArray!!, viewModel.getBaseUrl(), viewModel.getUserData().activeCompany.id)
//            menuAdapter.setOnclickListener { view ->
//                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
//            }

//            rv.adapter = menuAdapter
//        } else {
        // ga pake custom app
        var defaultMenuList = ArrayList<DefaultMenuModel>()
        var otherDefaultMenuList = ArrayList<DefaultMenuModel>()
        var seeAllDefaultMenuList = ArrayList<DefaultMenuModel>()
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.topup),
                R.drawable.ic_home_topup,
                Intent(this, TopUpActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.invoice),
                R.drawable.ic_home_invoice,
                Intent(this, InvoiceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.mutation),
                R.drawable.ic_home_mutation,
                Intent(this, MutationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.transaction),
                R.drawable.ic_home_transaction,
                Intent(this, TransactionActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.attendance),
                R.drawable.ic_home_attendance,
                Intent(this, AttendanceActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.digital_card),
                R.drawable.ic_home_digital_card,
                Intent(this, DigitalCardActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.account),
                R.drawable.ic_home_account,
                Intent(this, AccountActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.donation),
                R.drawable.ic_home_donation,
                Intent(this, DonationActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.schedule),
                R.drawable.ic_home_schedule,
                Intent(this, ScheduleActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.calendar),
                R.drawable.ic_home_calendar,
                Intent(this, CalendarActivity::class.java)
            )
        )
        defaultMenuList.add(
            DefaultMenuModel(
                resources.getString(R.string.support),
                R.drawable.ic_home_support,
                Intent(this, FaqActivity::class.java)
            )
        )

        defaultMenuList = ArrayList(defaultMenuList)

        val menuAdapter = DefaultMenuAdapter()
        menuAdapter.setMenuList(defaultMenuList, this)
        menuAdapter.setOtherMenuList(otherDefaultMenuList, this, this)
        otherDefaultMenuArray = otherDefaultMenuList
        rv.adapter = menuAdapter
//        }


    }
}