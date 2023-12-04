package id.co.pspmobile.data.local

import android.content.Context
import com.google.gson.Gson
import id.co.pspmobile.data.network.responses.digitalCard.NewDigitalCardData

object SharePreferences {
    private const val PREFS_NAME = "my_shared_prefs"
    private const val KEY_FB_TOKEN = "fb_token"
    private const val KEY_NAME = "name"
    private const val NEW_SYNC_DC = "new_sync_dc"

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

    fun saveNewSyncDigitalCard(context: Context, newSyncDigitalCard: NewDigitalCardData) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(NEW_SYNC_DC, Gson().toJson(newSyncDigitalCard)).apply()
    }

    fun getNewSyncDigitalCard(context: Context): NewDigitalCardData? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPrefs.getString(NEW_SYNC_DC, null)
        return Gson().fromJson(json, NewDigitalCardData::class.java)
    }

}