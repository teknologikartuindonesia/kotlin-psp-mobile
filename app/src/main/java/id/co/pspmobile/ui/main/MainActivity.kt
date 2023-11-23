package id.co.pspmobile.ui.main

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityMainBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.intro.IntroActivity
import id.co.pspmobile.ui.login.LoginActivity
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!viewModel.getIntro()){
            viewModel.saveIntro(true)
            startNewActivity(IntroActivity::class.java)
        }
        // define user will be login or not
        // if already logged in, go to home activity
        // if not, go to login activity

        if (viewModel.getToken() != ""){
            checkCurrentToken()
        } else {
            startNewActivity(LoginActivity::class.java)
        }

        viewModel.checkCredentialResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                val checkCredentialResponse = it.value
                if (checkCredentialResponse.firstLogin){
                    // create password
                } else {
                    startNewActivity(HomeActivity::class.java)
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.progressbar, it)
            }
        }
        configLanguage()
    }

    private fun checkCurrentToken(){
        viewModel.checkCredential()
    }

    private fun configLanguage(){
        // config language
        val lang = viewModel.getLanguage()
        setLang(lang)
    }
    fun setLang(lang: String){
        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        if (lang == "id"){
            val locale = Locale("id", "ID")
            Locale.setDefault(locale)
            configuration.setLocale(locale)
        } else  {
            val locale: Locale = Locale(lang)
            Locale.setDefault(locale)
            configuration.setLocale(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}