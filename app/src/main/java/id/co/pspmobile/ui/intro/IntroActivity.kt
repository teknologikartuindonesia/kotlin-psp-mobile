package id.co.pspmobile.ui.intro

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityIntroBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.getAndroidVersion
import id.co.pspmobile.ui.Utils.slideAnimation
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.dialog.DialogYesNo
import id.co.pspmobile.ui.login.LoginActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    var step = 0
    private lateinit var binding: ActivityIntroBinding
    private val viewModel: IntroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnLogo.setOnClickListener {
            step = 0
            binding.imgIntro1.visible(true)
            binding.imgIntro2.visible(false)
            binding.imgIntro3.visible(false)
            binding.imgIntro1.visible(true)
            binding.imgIntro2.visible(false)
            binding.imgIntro3.visible(false)
        }

        binding.btnNext.setOnClickListener {
            when(step) {
                0 -> {
                    binding.imgIntro2.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
                    binding.imgIntro1.visible(false)
                    binding.imgIntro1.visible(false)
                    binding.imgIntro2.visible(true)
                    binding.imgPager1.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager2.setImageResource(R.drawable.active_dot)
                    binding.imgPager3.setImageResource(R.drawable.inactive_dot)

                    binding.txtTitle.text = resources.getText(R.string.digital_invoice)
                    binding.txtSubtitle.text = resources.getText(R.string.subtitle_intro_2)
                    step += 1
                }
                1 -> {
                    binding.imgIntro3.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
                    binding.imgIntro2.visible(false)
                    binding.imgIntro2.visible(false)
                    binding.imgIntro3.visible(true)
                    binding.imgPager1.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager2.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager3.setImageResource(R.drawable.active_dot)
                    binding.txtTitle.text = resources.getText(R.string.absensi)
                    binding.txtSubtitle.text = resources.getText(R.string.subtitle_intro_3)
                    step += 1
                }
                else -> {
                    // done
                    startNewActivity(LoginActivity::class.java)
                }
            }

        }
        requestPermission()
    }
    // read file from Download folder named "credential-pspmobile.txt"
    fun readExisting(){
        val currentAndroidVersion = getAndroidVersion()
        if (currentAndroidVersion >= 13) {
            return
        }
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "credential-pspmobile.txt")
            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var text: String?
                while (run {
                        text = bufferedReader.readLine()
                        text
                    } != null) {
                    stringBuilder.append(text)
                }
                // {"username":"bob","password":"12345678"}

                if (!stringBuilder.toString().isNullOrEmpty()){
                    val reverseBack = reverseBackToNormal(stringBuilder.toString())
                    val username = getUsername(reverseBack)
                    val password = getPassword(reverseBack)
                    viewModel.saveUsername(username)
                    viewModel.savePassword(password)
                    binding.txtTitle.text =
                        "$stringBuilder \n $reverseBack \n $username \n $password \n " +
                                "${viewModel.getUsername()} \n ${viewModel.getPassword()}"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reverseBackToNormal(reversedString: String): String {
        val result = StringBuilder()

        for (i in reversedString.indices step 3) {
            val chunk = reversedString.subSequence(i, minOf(i + 3, reversedString.length))
            result.append(chunk.reversed())
        }
        return result.toString()
    }

    // request permission to read external storage
    fun requestPermission() {
        val currentAndroidVersion = getAndroidVersion()
        if (currentAndroidVersion >= 13) {
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else{
            readExisting()
        }
    }

    private fun getUsername(str: String): String{
        return str.split(",")[0].split(":")[1]
            .replace("\"", "").replace("{","").replace("}","")
    }

    private fun getPassword(str: String): String{
        return str.split(",")[1].split(":")[1]
            .replace("\"", "").replace("{","").replace("}","")
    }

    // handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readExisting()
            } else {
                val dialogYesNo = DialogYesNo(
                    "Permission Dibutuhkan",
                    "Mohon izinkan aplikasi untuk mengakses file di penyimpanan eksternal Anda.",
                    "OK",
                    "Tidak",
                    yesListener = {
                        requestPermission()
                    },
                    noListener = {
                        finish()
                    }
                )
                dialogYesNo.show(supportFragmentManager, "dialog")
            }
        }
    }
}