package id.co.pspmobile.ui

import android.app.Activity
import android.content.Intent
import android.view.View

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
                    logout()
                }
            }
            else -> {
                val error = failure.errorBody?.string().toString()
                view.snackbar(error)
            }
        }
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
}