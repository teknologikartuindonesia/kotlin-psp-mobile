package id.co.pspmobile.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.login.LoginActivity
import kotlinx.coroutines.runBlocking

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
    fun Context.logout() {
        val userPreferences = UserPreferences(this)
        runBlocking { userPreferences.saveAccessToken("") }
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
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

    enum class SlideDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    enum class SlideType {
        SHOW,
        HIDE
    }
    fun View.slideAnimation(direction: SlideDirection, type: SlideType, duration: Long = 250) {
        val fromX: Float
        val toX: Float
        val fromY: Float
        val toY: Float
        val array = IntArray(2)
        getLocationInWindow(array)
        if ((type == SlideType.HIDE && (direction == SlideDirection.RIGHT || direction == SlideDirection.DOWN)) ||
            (type == SlideType.SHOW && (direction == SlideDirection.LEFT || direction == SlideDirection.UP))
        ) {
            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val deviceWidth = displayMetrics.widthPixels
            val deviceHeight = displayMetrics.heightPixels
            array[0] = deviceWidth
            array[1] = deviceHeight
        }
        when (direction) {
            SlideDirection.UP -> {
                fromX = 0f
                toX = 0f
                fromY = if (type == SlideType.HIDE) 0f else (array[1] + height).toFloat()
                toY = if (type == SlideType.HIDE) -1f * (array[1] + height) else 0f
            }
            SlideDirection.DOWN -> {
                fromX = 0f
                toX = 0f
                fromY = if (type == SlideType.HIDE) 0f else -1f * (array[1] + height)
                toY = if (type == SlideType.HIDE) 1f * (array[1] + height) else 0f
            }
            SlideDirection.LEFT -> {
                fromX = if (type == SlideType.HIDE) 0f else 1f * (array[0] + width)
                toX = if (type == SlideType.HIDE) -1f * (array[0] + width) else 0f
                fromY = 0f
                toY = 0f
            }
            SlideDirection.RIGHT -> {
                fromX = if (type == SlideType.HIDE) 0f else -1f * (array[0] + width)
                toX = if (type == SlideType.HIDE) 1f * (array[0] + width) else 0f
                fromY = 0f
                toY = 0f
            }
        }
        val animate = TranslateAnimation(
            fromX,
            toX,
            fromY,
            toY
        )
        animate.duration = duration
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (type == SlideType.HIDE) {
                    visibility = View.GONE
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                visibility = View.VISIBLE
            }
        })
        startAnimation(animate)
    }

    fun showToast(context: Context, message: String, duration: String) {
        Toast.makeText(
            context, message,
            if (duration == "short") Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    }
}