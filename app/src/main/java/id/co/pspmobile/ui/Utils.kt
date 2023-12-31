package id.co.pspmobile.ui

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import id.co.pspmobile.R
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.ui.donation.detail.DonationDetailActivity
import id.co.pspmobile.ui.invoice.InvoicePaymentActivity
import id.co.pspmobile.ui.invoice.fragment.BottomSheetPaymentSuccessInvoice
import id.co.pspmobile.ui.login.LoginActivity
import id.co.pspmobile.ui.main.MainActivity
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat


object Utils {

    fun Activity.handleApiError(
        view: View,
        failure: Resource.Failure,
    ) {
        when {
            failure.isNetworkError -> {
                view.snackbar("Gagal koneksi. Silahkan check kembali koneksi jaringan anda")
            }

            failure.errorCode == 401 -> {
                if (this is LoginActivity) {
                    val error = failure.errorBody?.string().toString()

                } else {
                    this.logout()
                }
            }

            failure.errorCode == 400 -> {
                if (this is InvoicePaymentActivity) {
                    view.snackbar(failure.errorBody?.string().toString())
                } else if (this is DonationDetailActivity) {
                    val error = failure.errorBody?.string().toString()
                    view.snackbar(error)
                } else if(this is MainActivity){
                    val error = failure.errorBody?.string().toString()
                    if (error == "BLOCK_USER") {
                        view.snackbar("Block User")
                        this.logout()
                    }
                }
                else if(this is LoginActivity){
                    val error = failure.errorBody?.string().toString()
                    if (error == "BLOCK_USER") {
                        view.snackbar("Block User")
                    }
                }
                else {
                    val error = failure.errorBody?.string().toString()
                    if (!error.isNullOrEmpty()) {
                        view.snackbar(error)
                    } else {
                        view.snackbar("Mohon Maaf, sedang tidak dapat memproses request anda.")
                    }
                }
            }

            else -> {
                val error = failure.errorBody?.string().toString()
                view.snackbar(error)
            }
        }
    }

    //    for Fragment use handle this
    fun Fragment.handleFragmentApiError(
        failure: Resource.Failure,
        retry: (() -> Unit)? = null
    ) {
        when {
            failure.isNetworkError -> {
                requireView().snackbar("Gagal koneksi. Silahkan check kembali koneksi jaringan anda")
            }

            failure.errorCode == 401 -> {
            }

            failure.errorCode == 400 -> {
                if (this is BottomSheetPaymentSuccessInvoice) {
                    requireView().snackbar("Transaksi Gagal, Silahkan coba beberapa saat lagi.")
                } else {
                    this.requireActivity().logout()
                }
            }

            else -> {
                val error = failure.errorBody?.string().toString()
                requireView().snackbar(error)
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
        val snackbarView = snackbar.view
        val layoutParams = FrameLayout.LayoutParams(snackbar.view.layoutParams)
        val snackTextView =
            snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView

        layoutParams.setMargins(10, 10, 10, 10)
        layoutParams.gravity = Gravity.BOTTOM
        snackbar.view.setPadding(10, 10, 10, 10)
        snackTextView.maxLines = 5
        snackbar.view.layoutParams = layoutParams
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snackbar.show()
    }

    fun Activity.hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, notificationMessage, Toast.LENGTH_SHORT).show()
    }

    fun formatCurrency(doubleValue: Double): String {
        val unusualSymbols = DecimalFormatSymbols()
        unusualSymbols.decimalSeparator = ','
        unusualSymbols.groupingSeparator = '.'

        val formatter = DecimalFormat("#,##0.##", unusualSymbols)
        formatter.groupingSize = 3
        return formatter.format(doubleValue)
    }

    fun parseDouble(s: String?): Double {
        return if (s == null || s.isEmpty()) 0.0 else s.toDouble()
    }

    fun formatDateTime(isoDate: String, pattern: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(parser.parse(isoDate))
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


    fun Activity.showLottieLoader(supportFragmentManager: FragmentManager) {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(supportFragmentManager, "lottieLoaderDialog")
    }

    fun Activity.hideLottieLoader(supportFragmentManager: FragmentManager) {
        val loaderDialogFragment =
            supportFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }

    fun subString(input: String, startIndex: Int, endIndex: Int): String {
        // Ensure startIndex and endIndex are within valid bounds
        val start = startIndex.coerceIn(0, input.length)
        val end = endIndex.coerceIn(0, input.length)

        // Return the substring
        return if (start <= end) {
            input.substring(start, end)
        } else {
            ""
        }
    }

    fun getBankShowName(name: String): String {
        return allBanks.find { it.name == name }?.showName ?: name
    }

    fun getBankIcon(name: String): Int {
        return allBanks.find { it.name == name }?.icon ?: R.drawable.logo_default_bank
    }

    fun getMerchantIcon(merchantName: String): Int {
        when (merchantName) {
            "INDOMARET" -> {
                return R.drawable.logo_indomaret
            }

            "ALFAMART" -> {
                return R.drawable.logo_alfamart
            }

            "GOPAY" -> {
                return R.drawable.logo_gopay
            }

            "TOKOPEDIA" -> {
                return R.drawable.logo_tokopedia
            }

            "SHOPEE" -> {
                return R.drawable.logo_shopee
            }

            "BLIBLI" -> {
                return R.drawable.logo_blibli
            }

            "AYOPOP" -> {
                return R.drawable.logo_ayopop
            }

            else -> {
                return R.drawable.logo_default_bank
            }
        }
    }

    data class Bank(
        val name: String,
        val showName: String,
        val icon: Int
    )

    private val allBanks = listOf(
        Bank("TKI", "Bank Negara Indonesia", R.drawable.logo_bni),
        Bank("BNI", "Bank Negara Indonesia", R.drawable.logo_bni),
        Bank("BNI OGP", "Bank Negara Indonesia", R.drawable.logo_bni),
        Bank("BNI 46", "Bank Negara Indonesia", R.drawable.logo_bni),
        Bank("BNI SYARIAH", "Bank BNI Syariah", R.drawable.logo_bni),
        Bank("BANKJATIM", "Bank Jatim Syariah", R.drawable.logo_bjs),
        Bank("BANKJATIM OVERBOOK", "Bank Jatim Syariah", R.drawable.logo_bjs),
        Bank("BSI", "Bank Syariah Indonesia", R.drawable.logo_bsi),
        Bank("BSI-MAKARA", "Bank Syariah Indonesia", R.drawable.logo_bsi),
        Bank("BSI-SBI", "Bank Syariah Indonesia", R.drawable.logo_bsi),
        Bank("NTB SYARIAH", "Bank Ntb Syariah", R.drawable.logo_ntbs),
        Bank("MUAMALAT", "Bank Muamalat", R.drawable.logo_muamalat),
        Bank("BCA", "Bank Central Asia", R.drawable.logo_bca),
        Bank("BRI", "Bank Rakyat Indonesia", R.drawable.logo_bri),
        Bank("MANDIRI", "Bank Mandiri", R.drawable.logo_mandiri),
        Bank("PERMATA", "Bank Permata", R.drawable.logo_permata),
        Bank("PERMATA SYARIAH", "Bank Permata Syariah", R.drawable.logo_permata_syariah),
        Bank("BJB", "Bank BJB", R.drawable.logo_bjb),
        Bank("BJB SYARIAH", "Bank BJB Syariah", R.drawable.logo_bjbs),
        Bank("BTN", "Bank BTN", R.drawable.logo_btn),
        Bank("BTPN", "Bank BTPN", R.drawable.logo_btpn),
        Bank("CIMB", "Bank CIMB Niaga", R.drawable.logo_cimb),
        Bank("DANAMON", "Bank Danamon", R.drawable.logo_danamon),
        Bank("MAYBANK", "Bank Maybank", R.drawable.logo_maybank),
        Bank("OCBC", "Bank OCBC NISP", R.drawable.logo_ocbc)
    )

    fun splitDecimalEndPoint(input: String?): String {
        val splitString =
            input?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        return splitString?.get(0) ?: "0"
    }
}
