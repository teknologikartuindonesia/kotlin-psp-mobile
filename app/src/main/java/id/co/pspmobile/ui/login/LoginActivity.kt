package id.co.pspmobile.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityLoginBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressbar.visible(false)
        viewModel.loginResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if(it is Resource.Success){
                if (it.value.firstLogin){
                    // go to create password
                }else{
                    viewModel.checkCredential()
                }
            }else if (it is Resource.Failure){

            }
        }

        viewModel.checkCredentialResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                val checkCredentialResponse = it.value
                if (!checkCredentialResponse.user.accounts[0].roles.contains("ROLE_USER")){
                    // if user is not ROLE_USER, show error message
                } else {
                    startNewActivity(HomeActivity::class.java)
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.progressbar, it)
            }
        }

        binding.btnLogin.setOnClickListener {
            login(binding.edUsername.text.toString(), binding.edPassword.text.toString())
        }
    }

    fun login(username: String, password: String){
        viewModel.login(username, password)
    }
}