package id.co.pspmobile.ui

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.ui.login.LoginActivity
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.room.TypeConverter
import java.text.SimpleDateFormat

object Utils {

    fun Activity.handleApiError(
        view: View,
        failure: Resource.Failure,
    ) {
        when {
            failure.isNetworkError ->  {
                view.snackbar("Gagal koneksi. Silahkan check kembali koneksi jaringan anda")
            }
            failure.errorCode == 401 -> {
                if (this is LoginActivity) {
                    view.snackbar("Username dan password tidak valid")
                } else {
                    this.logout()
                }
            }
            else -> {
                val error = failure.errorBody?.string().toString()
                view.snackbar(error)
            }
        }
    }

    fun Activity.logout() {
        val userPreferences = UserPreferences(this)
        runBlocking { userPreferences.saveAccessToken("") }
        startNewActivity(LoginActivity::class.java)
    }
    fun View.snackbar(message: String) {
        val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        val layoutParams = FrameLayout.LayoutParams(snackbar.view.layoutParams)
        layoutParams.gravity = Gravity.BOTTOM
        snackbar.view.setPadding(0, 0, 0, 20)
        snackbar.view.layoutParams = layoutParams
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackbar.show()
    }

    fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
        Intent(this, activity).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    fun copyToClipboard(context: Context, text: String, notificationMessage: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, notificationMessage, Toast.LENGTH_SHORT).show()
    }

    fun formatCurrency(doubleValue: Double) : String{
        val unusualSymbols = DecimalFormatSymbols()
        unusualSymbols.decimalSeparator = ','
        unusualSymbols.groupingSeparator = '.'

        val formatter = DecimalFormat("#,##0.##", unusualSymbols)
        formatter.groupingSize = 3
        return formatter.format(doubleValue)
    }

    fun formatDateTime(isoDate:String,pattern:String):String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat(pattern)
        val output: String = formatter.format(parser.parse(isoDate))
        return output
    }

}
