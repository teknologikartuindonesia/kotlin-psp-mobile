package id.co.pspmobile.ui.HomeBottomNavigation.profile.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityChangePasswordBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.hideKeyboard
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    var inputMode = false
    var isPassword1 = true
    var isPassword2 = true
    var isPassword3 = true

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditPassword.setOnClickListener {
            changeMode()
        }

        binding.btnSavePassword.setOnClickListener {
            changePassword()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        hideKeyboard()

        binding.progressBar.visible(false)

        viewModel.changePasswordResponse.observe(this) {
            binding.progressBar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                Toast.makeText(this, resources.getString(R.string.password_success_changed), Toast.LENGTH_SHORT).show()
                val newPass = binding.edNewPassword.text.toString()
                viewModel.savePassword(newPass)
                finish()
            } else if (it is Resource.Failure){
                handleApiError(binding.root, it)
            }
        }

        binding.btnEye1.setOnClickListener {
            if (isPassword1){
                binding.btnEye1.setImageResource(R.drawable.ic_close_eye)
                binding.edOldPassword.inputType = 129
                isPassword1 = false
            } else {
                binding.btnEye1.setImageResource(R.drawable.ic_open_eye)
                binding.edOldPassword.inputType = 128
                isPassword1 = true
            }
        }
        binding.btnEye2.setOnClickListener {
            if (isPassword2){
                binding.btnEye2.setImageResource(R.drawable.ic_close_eye)
                binding.edNewPassword.inputType = 129
                isPassword2 = false
            } else {
                binding.btnEye2.setImageResource(R.drawable.ic_open_eye)
                binding.edNewPassword.inputType = 128
                isPassword2 = true
            }
        }
        binding.btnEye3.setOnClickListener {
            if (isPassword3){
                binding.btnEye3.setImageResource(R.drawable.ic_close_eye)
                binding.edConfirmNewPassword.inputType = 129
                isPassword3 = false
            } else {
                binding.btnEye3.setImageResource(R.drawable.ic_open_eye)
                binding.edConfirmNewPassword.inputType = 128
                isPassword3 = true
            }
        }
    }

    fun changeMode(){
        if (inputMode){
            binding.apply {
                inputMode = false
                llOldPassword.background = resources.getDrawable(R.drawable.input_bg_inactive)
                llNewPassword.background = resources.getDrawable(R.drawable.input_bg_inactive)
                llConfirmNewPassword.background = resources.getDrawable(R.drawable.input_bg_inactive)
                edOldPassword.isEnabled = false
                edNewPassword.isEnabled = false
                edConfirmNewPassword.isEnabled = false
                btnEditPassword.visible(true)
                btnSavePassword.visible(false)
            }
        } else {
            binding.apply {
                inputMode = true
                llOldPassword.background = resources.getDrawable(R.drawable.input_bg)
                llNewPassword.background = resources.getDrawable(R.drawable.input_bg)
                llConfirmNewPassword.background = resources.getDrawable(R.drawable.input_bg)
                llOldPassword.isEnabled = true
                llNewPassword.isEnabled = true
                llConfirmNewPassword.isEnabled = true
                edOldPassword.isEnabled = true
                edNewPassword.isEnabled = true
                edConfirmNewPassword.isEnabled = true
                btnEditPassword.visible(false)
                btnSavePassword.visible(true)
            }
        }
    }
    fun changePassword(){
        val old = binding.edOldPassword.text.toString()
        val new1 = binding.edNewPassword.text.toString()
        val new2 = binding.edConfirmNewPassword.text.toString()
        val pass = viewModel.getCurrentPassword()
        hideKeyboard()

        if (new1 != new2){
            Snackbar.make(findViewById(android.R.id.content), resources.getString(R.string.password_should_match), Snackbar.LENGTH_SHORT).show()
        } else {
//            if (pass != old){
//                Snackbar.make(findViewById(android.R.id.content), resources.getString(R.string.wrong_old_password), Snackbar.LENGTH_SHORT).show()
//                return
//            }
            if (checkValidity(new1)){
                viewModel.changePassword(old, new1)
            } else {
                Snackbar.make(findViewById(android.R.id.content), resources.getString(R.string.password_should_valid), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkValidity(str:String): Boolean{
        var ch: Char
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
            }
            if (numberFlag && letterFlag && isMoreThan6) return true
        }
        return false
    }
}