package id.co.pspmobile.data.local

import android.content.Context

object SharePreferences {
    private const val PREFS_NAME = "my_shared_prefs"
    private const val KEY_FB_TOKEN = "fb_token"
    private const val KEY_NAME = "name"

    //    ==============================================================================================
    fun saveFbToken(context: Context, token: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(KEY_FB_TOKEN, token).apply()
    }

    fun getFbToken(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(KEY_FB_TOKEN, null)
    }

    //    ==============================================================================================
    fun saveName(context: Context, name: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(KEY_NAME, null)
    }

}