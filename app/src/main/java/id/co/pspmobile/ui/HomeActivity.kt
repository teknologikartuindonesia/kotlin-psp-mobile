package id.co.pspmobile.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityHomeBinding
import id.co.pspmobile.ui.Utils.showToast


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var exit = false
    private lateinit var navController: NavController
    private lateinit var menuBottom : ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment_activity_home)

        menuBottom = binding.bottomNavBar
        menuBottom.setItemSelected(R.id.navigation_home, true)
        menuBottom.setOnItemSelectedListener {
            when(it){
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

    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            if (exit) {
                finish()
            } else {
                showToast(this, resources.getString(R.string.press_back_to_exit), "short")
                exit = true
                Handler().postDelayed({ exit = false }, 3 * 1000)
            }
        } else {
            navController.navigate(R.id.navigation_home)
            menuBottom = binding.bottomNavBar
            menuBottom.setItemSelected(R.id.navigation_home, true)
        }
    }
}