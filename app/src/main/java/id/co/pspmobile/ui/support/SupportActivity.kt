package id.co.pspmobile.ui.support

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.co.pspmobile.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val installed = appInstalledOrNot("com.whatsapp")
            if (installed) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data =
                    Uri.parse("http://api.whatsapp.com/send?phone=6281326269992&text=" +
                            binding.etMessage.text.toString().trim())
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Maaf, aplikasi Whats App tidak terinstall.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun appInstalledOrNot(url: String): Boolean {
        val appInstalled = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }

}