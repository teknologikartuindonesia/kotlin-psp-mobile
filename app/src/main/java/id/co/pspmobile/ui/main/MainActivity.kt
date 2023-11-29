package id.co.pspmobile.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.service.FirebaseService
import id.co.pspmobile.databinding.ActivityMainBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.intro.IntroActivity
import id.co.pspmobile.ui.login.LoginActivity
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configLanguage()
        Handler().postDelayed({
            if (!viewModel.getIntro()) {
                viewModel.saveIntro(true)
                startNewActivity(IntroActivity::class.java)
            } else {
                if (viewModel.getToken() != "") {
                    checkCurrentToken()
                } else {
                    startNewActivity(LoginActivity::class.java)
                }

            }
        }, 2000)
        // define user will be login or not
        // if already logged in, go to home activity
        // if not, go to login activity


        viewModel.checkCredentialResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                val checkCredentialResponse = it.value

                //Subscribe Topics FCM
                subscribeTopics("broadcast-all")
                subscribeTopics("broadcast-${it.value.activeCompany.companyCode}")
                subscribeTopics("broadcast-${it.value.activeCompany.companyCode}")
                subscribeTopics("academic-${it.value.activeCompany.companyCode}")

                if (checkCredentialResponse.firstLogin) {
                    // create password
                } else {
                    var id: String = intent?.getStringExtra("type").toString()
                    var type: String = intent?.getStringExtra("id").toString()

                    val i = Intent(this, HomeActivity::class.java)
                    i.putExtra("type", intent?.getStringExtra("type").toString())
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.progressbar, it)
            }
        }
    }

    private fun checkCurrentToken() {
        viewModel.checkCredential()
    }

    private fun configLanguage() {
        // config language
        val lang = viewModel.getLanguage()
        setLang(lang)
    }

    fun setLang(lang: String) {
        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        if (lang == "id") {
            val locale = Locale("id", "ID")
            Locale.setDefault(locale)
            configuration.setLocale(locale)
        } else {
            val locale: Locale = Locale(lang)
            Locale.setDefault(locale)
            configuration.setLocale(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun subscribeTopics(value: String) {
        var topic = value
        if (viewModel.getBaseUrl().contains("dev")) {
            topic += "-staging"
        }
        firebaseService.subscribeTopic(this, topic)
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