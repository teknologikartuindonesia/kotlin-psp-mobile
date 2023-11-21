package id.co.pspmobile.ui.createpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityCreatePasswordBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.slideAnimation
import id.co.pspmobile.ui.Utils.snackbar
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import id.co.pspmobile.ui.forgotpassword.ModelCreatePassword
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class CreatePasswordActivity : AppCompatActivity() {

    var email = ""
    var wa = ""
    var pass = ""
    var oldPass = ""

    var wasInputted = false

    var show0 = false
    var show = false
    var show2 = false

    private var helpCount = 0

    private lateinit var binding: ActivityCreatePasswordBinding
    private val viewModel: CreatePasswordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        oldPass = this.intent.extras?.getString("currentPassword").toString()
        binding.btnCreatePassword.setOnClickListener {
            sendCreatePassword()
        }

        binding.edOldPassword.setText(viewModel.getPassword())

        checkRole()

        binding.btnEye0.setOnClickListener {
            Log.d("show0", "${show0}")
            if (show0){
                binding.edOldPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edOldPassword.setSelection(binding.edOldPassword.text.length)
                binding.btnEye0.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edOldPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edOldPassword.setSelection(binding.edOldPassword.text.length)
                binding.btnEye0.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show0 = !show0
            Log.d("show0", "${show0}")
        }

        binding.btnEye.setOnClickListener {
            if (show){
                binding.edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edPassword.setSelection(binding.edPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edPassword.setSelection(binding.edPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show = !show
        }

        binding.btnEye2.setOnClickListener {
            if (show2){
                binding.edConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edConfirmPassword.setSelection(binding.edConfirmPassword.text.length)
                binding.btnEye2.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edConfirmPassword.setSelection(binding.edConfirmPassword.text.length)
                binding.btnEye2.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show2 = !show2
        }

        binding.edConfirmPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                //do here your stuff f
                sendCreatePassword()
                true
            } else false
        })

        binding.edConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println(">>>>>" + s.toString().length)
                if (s.toString().isEmpty()) {
                    binding.layoutConfirmNewPassword.background = resources.getDrawable(R.drawable.input_bg)
                } else {
                    if (s.toString() == binding.edPassword.text.toString()) {
                        binding.layoutConfirmNewPassword.background = resources.getDrawable(R.drawable.input_bg_green)
                    } else  {
                        binding.layoutConfirmNewPassword.background = resources.getDrawable(R.drawable.input_bg_red)
                    }
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.btnIgnore.setOnClickListener {
            doLogin()
        }

        binding.btnChange.setOnClickListener {
            goToNext(binding.slideIgnoreChoice, binding.slideNewPasswordConfirmation)
            binding.layoutIgnoreChoice.visible(false)
        }

        binding.btnSetNewPassword.setOnClickListener {
            changePassword()
        }

        binding.btnSuccessChangePassword.setOnClickListener {
            doLogin()
        }

        viewModel.createPasswordResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                goToNext(binding.slideEmailWa, binding.slideIgnoreChoice)
                binding.layoutSupportedBy.visible(false)
                binding.layoutIgnoreChoice.visible(true)
            } else if (it is Resource.Failure){
                handleApiError(binding.root, it)
            }
        }

        viewModel.changePasswordResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                binding.mainScrollView.visible(false)
                binding.slideSuccessChangePassword.visible(true)
            } else if (it is Resource.Failure){
                handleApiError(binding.root, it)
            }
        }

        viewModel.loginResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                viewModel.saveUsername(email)
                viewModel.savePassword(pass)
                wasInputted = true
                checkRole()
            } else if (it is Resource.Failure){
                handleApiError(binding.root, it)
            }
        }

        viewModel.checkCredentialResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                Log.d("role",it.value.user.accounts[0].roles.toString())
                Log.d("role",it.value.user.accounts[0].roles.contains("ROLE_USER").toString())
                if (wasInputted){
                    if (it.value.user.accounts[0].roles.contains("ROLE_USER")){

                        viewModel.saveUserData(it.value)
                        viewModel.saveUsername(email)
                        viewModel.savePassword(pass)
                        val i = Intent(this, HomeActivity::class.java)
                        startActivity(i)
                        startNewActivity(HomeActivity::class.java)

//                        val fbToken = SharedPref.getFbToken(requireContext())
//                        if (fbToken != null) {
//                            saveFirebase(fbToken)
//                        }
//                        subscribeAll()

                    } else {
                        Log.d("role","Tidak punya akses")
                        binding.root.snackbar(resources.getString(R.string.dont_have_access))
                        viewModel.saveAccessToken("")
                    }
                } else {
                    binding.edEmailCreatePassword.setText(it.value.user.email)

                    if (it.value.user.phone.isNotEmpty()){
                        binding.edWaCreatePassword.setText(it.value.user.phone)
                    }

                    wasInputted = true
                }
            }
            else if (it is Resource.Failure){
                handleApiError(binding.root, it)
            }
        }
    }

    fun changePassword(){
        val old = binding.edOldPassword.text.toString()
        val new1 = binding.edPassword.text.toString()
        val new2 = binding.edConfirmPassword.text.toString()

        if (new1 != new2){
            binding.root.snackbar(resources.getString(R.string.password_should_match))
        } else {
            if (pass != old){
                binding.root.snackbar(resources.getString(R.string.wrong_old_password))
                return
            }
            if (checkValidity(new1)){
                val tempModel = ModelChangePassword(old, new1)
                pass = new1
                viewModel.sendChangePassword(tempModel)
            } else {
                binding.root.snackbar(resources.getString(R.string.password_should_valid))
            }
        }

    }

    fun sendCreatePassword(){
        email = binding.edEmailCreatePassword.text.toString()
        wa = binding.edWaCreatePassword.text.toString().replace(Regex("[^0-9]"), "")
        pass = oldPass
        val confirmPass = oldPass

        if (!isValidEmail(email)){
            binding.root.snackbar(resources.getString(R.string.must_a_valid_email))
            return
        }

        if (email.isNullOrEmpty() || wa.isNullOrEmpty()){
            binding.root.snackbar(resources.getString(R.string.please_input_email_or_whatsapp))
            return
        }

        if (pass != confirmPass){
            binding.root.snackbar(resources.getString(R.string.password_should_match))
        } else {
            val tempModel: ModelCreatePassword = ModelCreatePassword(
                email,
                pass,
                wa
            )
            viewModel.sendCreatePassword(tempModel)
        }
    }

    fun isValidEmail(str: String): Boolean{
        return !TextUtils.isEmpty(str) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    fun doLogin(){
        viewModel.login(email, pass)
    }

    private fun checkRole(){
        viewModel.checkCredential()
    }

    private fun saveFirebase(token: String){
//        var currentToken = runBlocking { userPreferences.getCompleteProfile() }
//        if (currentToken?.user?.firebase?.token != token){
//            viewModel.setFbToken(token, serverKeyId)
//        }
    }

    private fun checkValidity(str:String): Boolean{
        var ch: Char
        var capitalFlag = true
        var lowerCaseFlag = true
        var letterFlag = false
        var numberFlag = false
        var isMoreThan6 = str.length > 5
        for (i in str.indices) {
            ch = str[i]
            when {
                Character.isDigit(ch) -> {
                    numberFlag = true
                }
                Character.isLetter(ch) -> {
                    letterFlag = true
                }
//                Character.isUpperCase(ch) -> {
//                    capitalFlag = true
//                }
//                Character.isLowerCase(ch) -> {
//                    lowerCaseFlag = true
//                }
            }
            if (numberFlag && letterFlag && isMoreThan6) return true
        }
        return false
    }

    private fun goToNext(currentLayout: LinearLayout, nextLayout: LinearLayout) {
        currentLayout.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.HIDE)
        Handler().postDelayed({
            nextLayout.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
            nextLayout.visible(true)
        }, 300)
    }
}