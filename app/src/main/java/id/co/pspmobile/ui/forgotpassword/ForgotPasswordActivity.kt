package id.co.pspmobile.ui.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityForgotPasswordBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.showToast
import id.co.pspmobile.ui.Utils.slideAnimation
import id.co.pspmobile.ui.Utils.visible
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    var isWhatsapp = true
    var enteredPhoneEmailValue = ""
    var reqTime = ""
    var otp = ""

    var timerMinute = 4
    var timerSecond = 59
    var isTimerStopped = true

    var show = false
    var show2 = false
    var lastEmailPhone: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edit =
            arrayOf(binding.edOtp1, binding.edOtp2, binding.edOtp3, binding.edOtp4, binding.edOtp5)

        binding.edOtp1.addTextChangedListener(GenericTextWatcher(binding.edOtp1, edit))
        binding.edOtp2.addTextChangedListener(GenericTextWatcher(binding.edOtp2, edit))
        binding.edOtp3.addTextChangedListener(GenericTextWatcher(binding.edOtp3, edit))
        binding.edOtp4.addTextChangedListener(GenericTextWatcher(binding.edOtp4, edit))
        binding.edOtp5.addTextChangedListener(GenericTextWatcher(binding.edOtp5, edit))

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.showCs.setOnClickListener {
//            showCS()
        }

        binding.btnFirstSlide.setOnClickListener {
            goToNext(binding.firstSlide, binding.slideSendOtp)
        }

        binding.btnSendOtp.setOnClickListener {
            sendOtp()
        }

        binding.btnCheckOtp.setOnClickListener {
            checkOtp()
        }

        binding.radioType.setOnCheckedChangeListener { radioGroup, i ->
            Log.d("test", i.toString())
        }

        binding.radioType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            Log.d("group", group.toString())
            Log.d("checkedId", checkedId.toString())
            changeType()
        })

        binding.btnResendOtp.setOnClickListener {
            sendOtp()
        }

        binding.btnSavePassword.setOnClickListener {
            savePassword()
        }

        binding.btnEye.setOnClickListener {
            if (show){
                binding.edNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edNewPassword.setSelection(binding.edNewPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edNewPassword.setSelection(binding.edNewPassword.text.length)
                binding.btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show = !show
        }

        binding.btnEye2.setOnClickListener {
            if (show2){
                binding.edConfirmNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                binding.edConfirmNewPassword.setSelection(binding.edConfirmNewPassword.text.length)
                binding.btnEye2.setImageDrawable(resources.getDrawable(R.drawable.ic_open_eye))
            } else {
                binding.edConfirmNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.edConfirmNewPassword.setSelection(binding.edConfirmNewPassword.text.length)
                binding.btnEye2.setImageDrawable(resources.getDrawable(R.drawable.ic_close_eye))
            }
            show2 = !show2
        }

        binding.progressbar.visible(false)
        
        viewModel.sendOtpResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                goToNext(binding.slideSendOtp, binding.slideCheckOtp)
                isTimerStopped = false
                startTimer()
            } else if (it is Resource.Failure){
                handleApiError(binding.progressbar, it)
            }
        }

        viewModel.checkOtpResponse.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                goToNext(binding.slideCheckOtp, binding.slideNewPassword)
            } else if (it is Resource.Failure){
                handleApiError(binding.progressbar, it)
            }
        }
        
        viewModel.sendNewPassword.observe(this){
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success){
                // di auto login kan apa di direct ke login page
                viewModel.saveUsername(enteredPhoneEmailValue)
                viewModel.savePassword(binding.edConfirmNewPassword.text.toString())

                showToast(this, resources.getString(R.string.login_with_new_password), "short")
                finish()
                
            } else if (it is Resource.Failure){
                handleApiError(binding.progressbar, it)
            }
        }
    }

    class GenericTextWatcher(private val view: View, private val editText: Array<EditText>) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (view.id) {
                R.id.ed_otp_1 -> if (text.length == 1) editText[1].requestFocus()
                R.id.ed_otp_2 -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
                R.id.ed_otp_3 -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
                R.id.ed_otp_4 -> if (text.length == 1) editText[4].requestFocus() else if (text.isEmpty()) editText[2].requestFocus()
                R.id.ed_otp_5 -> if (text.isEmpty()) editText[3].requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    }

    private fun goToNext(currentLayout: LinearLayout, nextLayout: LinearLayout) {
        currentLayout.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.HIDE)
        Handler().postDelayed({
            nextLayout.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
            nextLayout.visible(true)
        }, 300)
    }

    fun startTimer() {
        if (isTimerStopped){
            return
        }
        if (timerMinute == 0 && timerSecond == 0) {
            stopTimer()
            binding.txtTimer.visible(false)
            binding.btnResendOtp.visible(true)
            return
        } else {
            binding.txtTimer.visible(true)
            binding.btnResendOtp.visible(false)
            Handler().postDelayed({
                if (timerSecond == 0) {
                    timerMinute -= 1
                    timerSecond = 59
                }
                timerSecond -= 1
                val showedSecond = if (timerSecond < 10) "0$timerSecond" else timerSecond
                binding.txtTimer.text =
                    "Anda dapat mengirim ulang OTP dalam $timerMinute:$showedSecond"

                Log.d("timer", "\"Anda dapat mengirim ulang OTP dalam $timerMinute:$showedSecond\"")
                startTimer()
            }, 1000)
        }
    }

    fun stopTimer() {
        isTimerStopped = true
        timerMinute = 0
        timerSecond = 0
    }
    private fun changeType() {
        Log.d("isWhatsapp", isWhatsapp.toString())
        binding.edWa.visible(!isWhatsapp)
        binding.edEmail.visible(isWhatsapp)
        isWhatsapp = !isWhatsapp
        Log.d("isWhatsapp", isWhatsapp.toString())
        if (isWhatsapp) {
            binding.edEmail.setText("")
        } else {
            binding.edWa.setText("")
        }
    }

    fun sendOtp() {
        lastEmailPhone = viewModel.getLastResetPassword()

        Log.d("ssendOtplastEmailPhone", lastEmailPhone.toString())
        enteredPhoneEmailValue =
            if (isWhatsapp) binding.edWa.text.toString() else binding.edEmail.text.toString()

        if (enteredPhoneEmailValue.isNullOrEmpty()){
            showToast(this, resources.getString(R.string.please_complete_form), "short")
            return
        }
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val now = sdf.format(Date()) // string
        var dateNow = SimpleDateFormat("dd/M/yyyy hh:mm:ss").parse(now) // date
        System.out.println(" C DATE is  " + now)

        if (!lastEmailPhone.isNullOrEmpty()) {

            var x = lastEmailPhone!!.split("-")
            var valueLastEmailPhone = x[0]
            var lastDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss").parse(x[1])
            var lastAttempt = x[2].toInt()

            if (valueLastEmailPhone == enteredPhoneEmailValue) {
                val milliSeconds: Long = dateNow.time - lastDate.time
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
                val minuteDiff = seconds / 60
                Log.d("timeDiff", "$milliSeconds--$seconds--$minuteDiff")

                if (lastAttempt < 3) {
                    Log.d("ssendOtp", "lastAttemp | $lastAttempt")
//                     masih boleh dalam sehari
//                     attempt tersebut ditambah 1
                    if (minuteDiff <= 4) {
                        // belum lima menit
                        Log.d("ssendOtp", "lastAttemp | $lastEmailPhone | minuteDiff:$minuteDiff")
                        showToast(this, resources.getString(R.string.otp_need_spare_time), "long")
                    } else {
                        // lebih dari lima
                        lastAttempt += 1
                        val newValue = "$valueLastEmailPhone-$now-$lastAttempt"
                        Log.d("ssendOtp", "> 5 menit | $newValue")
                        lastEmailPhone = newValue
                        viewModel.saveLastResetPassword(newValue)
                        executeSendOtp()
                    }
                } else {
//                    udah lebih dari 3 gaboleh
                    Log.d("ssendOtp", "more 3 times | $lastEmailPhone")
                    showToast(this, resources.getString(R.string.otp_more_than_three_times), "long")
                }

            } else {
                val newValue = "$enteredPhoneEmailValue-$now-1"
                Log.d("ssendOtp", "email/phone beda dari sblme | $newValue")
                lastEmailPhone = newValue
                viewModel.saveLastResetPassword(newValue)
                executeSendOtp()
            }
        } else {
            val newValue = "$enteredPhoneEmailValue-$now-1"
            Log.d("ssendOtp", "masih kosongg | $newValue")
            lastEmailPhone = newValue
            viewModel.saveLastResetPassword(newValue)
            executeSendOtp()
        }

    }

    private fun executeSendOtp() {
        enteredPhoneEmailValue =
            if (isWhatsapp) binding.edWa.text.toString() else binding.edEmail.text.toString()

        val sdf = SimpleDateFormat("HH:mm")
        val currentTime = sdf.format(Date())
        reqTime = currentTime

        timerMinute = 4
        timerSecond = 59

        Log.d("enteredPhoneEmailValue", "$isWhatsapp $enteredPhoneEmailValue")
        if (!enteredPhoneEmailValue.isNullOrEmpty()) {
            val tempBody = ModelSendOtp(
                "",
                "",
                enteredPhoneEmailValue,
                reqTime,
                if (isWhatsapp) "wa" else "email"
            )


            viewModel.sendOtp(tempBody)
        } else {
            showToast(this, resources.getString(R.string.please_complete_form), "short")
        }
    }

    fun checkOtp() {
        val tempOtp = binding.edOtp1.text.toString() +
                binding.edOtp2.text.toString() +
                binding.edOtp3.text.toString() +
                binding.edOtp4.text.toString() +
                binding.edOtp5.text.toString()
        if (tempOtp.length == 5) {
            otp = tempOtp
            val tempBody = ModelCheckOtp("", otp, enteredPhoneEmailValue)

            viewModel.checkOtp(tempBody)
        } else {
            showToast(this, resources.getString(R.string.please_otp_first), "short")
        }
    }

    fun savePassword() {
        val pass1 = binding.edNewPassword.text.toString()
        val pass2 = binding.edConfirmNewPassword.text.toString()

        if (pass1 != pass2){
            showToast(this, resources.getString(R.string.password_should_match), "short")
            return
        }

        if (pass1.isNullOrEmpty()){
            showToast(this, resources.getString(R.string.please_complete_form), "short")
            return
        }

        if(!checkValidity(pass1)){
            showToast(this, resources.getString(R.string.password_should_valid), "long")
            return
        }

        val tempBody = ModelSendOtp(
            pass1,
            otp,
            enteredPhoneEmailValue,
            reqTime,
            if (isWhatsapp) "wa" else "email"
        )
        
        viewModel.sendNewPasswordForgot(tempBody)
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
}