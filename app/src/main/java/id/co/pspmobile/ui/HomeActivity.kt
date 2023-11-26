package id.co.pspmobile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityHomeBinding
import id.co.pspmobile.ui.calendar.CalendarActivity
import id.co.pspmobile.ui.invoice.InvoiceActivity
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import id.co.pspmobile.ui.topup.history.HistoryTopUpActivity


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        var menu_bottom = binding.bottomNavBar
        menu_bottom.setItemSelected(R.id.navigation_home, true)
        menu_bottom.setOnItemSelectedListener {
            when (it) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                }

                R.id.navigation_information -> {
                    navController.navigate(R.id.navigation_information)
                }

                R.id.navigation_message -> {
                    navController.navigate(R.id.navigation_message)
                }

                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                }
            }
        }

        val type: String? = intent.getStringExtra("type")
        if (type != "null") {
            when (type) {
                "invoice" -> Handler().postDelayed({
                    showLottieLoader()
                    startActivity(
                        Intent(
                            this,
                            InvoiceActivity::class.java
                        )
                    )
                }, 500)
                "notification" -> Handler().postDelayed({

                    menu_bottom.setItemSelected(R.id.navigation_message, true)
                    navController.navigate(R.id.navigation_message)
                    
                }, 500)
                "academic-calendar" -> Handler().postDelayed({
                    showLottieLoader()
                    startActivity(
                        Intent(
                            this,
                            CalendarActivity::class.java
                        )
                    )
                }, 500)
                "calendar-academic" -> Handler().postDelayed({
                    showLottieLoader()
                startActivity(
                    Intent(
                        this,
                        CalendarActivity::class.java
                    )
                )
            }, 500)
            }
        }
    }

    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(supportFragmentManager, "lottieLoaderDialog")

    }
    private fun hideLottieLoader() {
        val loaderDialogFragment =
            supportFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }

}