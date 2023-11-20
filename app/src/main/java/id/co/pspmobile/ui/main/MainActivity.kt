package id.co.pspmobile.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityMainBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoices.InvoicesActivity
import id.co.pspmobile.ui.login.LoginActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // define user will be login or not
        // if already logged in, go to home activity
        // if not, go to login activityo

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
    }

    private fun checkCurrentToken(){
        viewModel.checkCredential()
    }
}