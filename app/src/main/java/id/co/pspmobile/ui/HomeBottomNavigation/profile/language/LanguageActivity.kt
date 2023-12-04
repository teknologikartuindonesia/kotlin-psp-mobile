package id.co.pspmobile.ui.HomeBottomNavigation.profile.language

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityLanguageBinding
import id.co.pspmobile.ui.Utils.hideLottieLoader
import id.co.pspmobile.ui.Utils.showLottieLoader
import kotlinx.coroutines.runBlocking
import java.util.Locale

@AndroidEntryPoint
class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private val viewModel: LanguageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Language", Locale.getDefault().language)
        if (viewModel.getLanguage() == "en"){
            binding.radioEnglish.isChecked = true
        } else {
            binding.radioIndonesia.isChecked = true
        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.radioIndonesia -> {
                    setLocale("id")
                }
                R.id.radioEnglish -> {
                    setLocale("en")
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.customAppResponse.observe(this ){
            when(it is id.co.pspmobile.data.network.Resource.Loading){
                true -> showLottieLoader(supportFragmentManager)
                else -> hideLottieLoader(supportFragmentManager)
            }
            if (it is id.co.pspmobile.data.network.Resource.Success){
                viewModel.saveLocalCustomApp(it.value)
                finish()
            }else {
                finish()
            }
        }
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
        viewModel.saveLanguage(lang)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        Log.d("Language", "Success set "+Locale.getDefault().language)
    }
    fun setLocale(language: String?) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
        viewModel.saveLanguage(language!!)
        if(viewModel.getUserData().activeCompany.customApps){
            viewModel.getCustomApp(
                viewModel.getUserData().activeCompany.id,
                if (language == "en") "EN" else "ID"
            )
        }else{
            finish()
        }
    }
}