package id.co.pspmobile.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.service.FirebaseService
import id.co.pspmobile.databinding.ActivityLoginBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.HomeBottomNavigation.home.MenuModel
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.showToast
import id.co.pspmobile.ui.Utils.snackbar
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.createpassword.CreatePasswordActivity
import id.co.pspmobile.ui.dialog.DialogCS
import id.co.pspmobile.ui.dialog.DialogYesNo
import id.co.pspmobile.ui.forgotpassword.ForgotPasswordActivity
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    var show = false
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var binding: ActivityLoginBinding
    private var firebaseService = FirebaseService()

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurePartner()
        viewModel.loginResponse.observe(this) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                if (it.value.firstLogin) {
                    // go to create password
                    val i = Intent(this, CreatePasswordActivity::class.java)
                    val t = binding.edPassword.text.toString()
                    i.putExtra("currentPassword", t)
                    startActivity(i)
                } else {
                    viewModel.checkCredential()
                }
            } else if (it is Resource.Failure) {
                if (it.errorCode == 401){
                    binding.root.snackbar(resources.getString(R.string.wrong_username_password))
                }
            }
        }

        binding.btnCs.setOnClickListener {
            val dialogYesNot = DialogYesNo(
                "Perhatian",
                "Apakah Kendala Anda Lupa Password? Silahkan Gunakan Fitur Lupa Password",
                "Buka Fitur",
                "Tidak",
                yesListener = {
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                },
                noListener = {
                    val args = Bundle()
                    val dialog = DialogCS()
                    dialog.arguments = args
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            )
            dialogYesNot.show(supportFragmentManager, dialogYesNot.tag)
        }

        viewModel.checkCredentialResponse.observe(this) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                val checkCredentialResponse = it.value
                if (!checkCredentialResponse.user.accounts[0].roles.contains("ROLE_USER")) {
                    // if user is not ROLE_USER, show error message
                } else {
                    viewModel.saveUserData(it.value)

                    subscribeTopics("broadcast-all")
                    subscribeTopics("broadcast-${it.value.activeCompany.companyCode}")
                    subscribeTopics("broadcast-${it.value.activeCompany.companyCode}")
                    subscribeTopics("academic-${it.value.activeCompany.companyCode}")

                    viewModel.saveUsername(binding.edUsername.text.toString())
                    viewModel.savePassword(binding.edPassword.text.toString())

                    startNewActivity(HomeActivity::class.java)
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.progressbar, it)
            }
        }

        binding.btnLogin.setOnClickListener {
            login(binding.edUsername.text.toString(), binding.edPassword.text.toString())
        }

        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.edPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                //do here your stuff f
                login(binding.edUsername.text.toString(), binding.edPassword.text.toString())
                true
            } else false
        })
        binding.btnEye.setOnClickListener {
            if (show) {
                binding.edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edPassword.setSelection(binding.edPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edPassword.setSelection(binding.edPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show = !show
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast(this@LoginActivity, "Authentication error: $errString", "short")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    login(viewModel.getUsername(), viewModel.getPassword())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast(this@LoginActivity, "Authentication failed", "short")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login Biometrik")
            .setSubtitle("Log in menggunakan sidik jari")
            .setNegativeButtonText("Batalkan")
            .build()

        binding.btnBiometric.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        if (deviceHasBiometric() && (viewModel.getUsername() != "" && viewModel.getPassword() != "")) {
            binding.btnBiometric.visible(true)
            binding.lineBtnBiometric.visible(true)
        }
    }

    fun login(username: String, password: String) {
        viewModel.login(username, password)
    }

    fun configurePartner() {
        val partner = listOf("TKI")
        val adapter = BankPartnerAdapter()
        adapter.setMenuList(partner)
        binding.rvBank.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.edUsername.setText(viewModel.getUsername())
        binding.edPassword.setText(if (viewModel.getPassword() == "null") "" else viewModel.getPassword())
    }

    fun deviceHasBiometric(): Boolean {
        var info = ""
        var returnValue = false
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                info = "App can authenticate using biometrics."
                returnValue = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                info = "No biometric features available on this device."
                returnValue = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                info = "Biometric features are currently unavailable."
                returnValue = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

            }
        }
        return returnValue
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
