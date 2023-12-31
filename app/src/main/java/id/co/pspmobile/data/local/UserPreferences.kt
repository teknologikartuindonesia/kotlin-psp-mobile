package id.co.pspmobile.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.customapp.CustomAppResponse
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "psp_mobile_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val appContext = context.applicationContext

    val accessToken: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    suspend fun saveAccessToken(accessToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    fun getAccessToken() = runBlocking(Dispatchers.IO) {
        accessToken.first()
    }

    val userData: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USER_DATA] ?: ""
        }

    val digitalCardData: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[DIGITAL_CARD] ?: ""
        }

    suspend fun saveUserData(userData: CheckCredentialResponse) {
        Log.d("UserPreferences", "saveUserData: ${userData}")

        appContext.dataStore.edit { preferences ->
            preferences[USER_DATA] = Gson().toJson(userData)
        }
    }

    fun getUserData() = runBlocking(Dispatchers.IO) {
        Log.d("UserPreferences", "getUserData: ${userData.first()}")
        Gson().fromJson(userData.first().toString(), CheckCredentialResponse::class.java)
    }

    val lastResetPassword: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LAST_RESET_PASSWORD] ?: ""
        }

    suspend fun saveLastResetPassword(lastResetPassword: String) {
        appContext.dataStore.edit { preferences ->
            preferences[LAST_RESET_PASSWORD] = lastResetPassword
        }
    }

    fun getLastResetPassword() = runBlocking(Dispatchers.IO) {
        lastResetPassword.first()
    }

    val username: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USERNAME] ?: ""
        }

    suspend fun saveUsername(username: String) {
        appContext.dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    fun getUsername() = runBlocking(Dispatchers.IO) {
        username.first()
    }

    val password: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[PASSWORD] ?: ""
        }

    suspend fun savePassword(password: String) {
        appContext.dataStore.edit { preferences ->
            preferences[PASSWORD] = password
        }
    }

    fun getPassword() = runBlocking(Dispatchers.IO) {
        password.first()
    }

    val intro: Flow<Boolean>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[INTRO] ?: false
        }

    suspend fun saveIntro(intro: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[INTRO] = intro
        }
    }

    fun getIntro() = runBlocking(Dispatchers.IO) {
        intro.first()
    }

    val balanceData: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[BALANCE] ?: ""
        }

    suspend fun saveBalanceData(balanceData: BalanceResponse) {
        appContext.dataStore.edit { preferences ->
            preferences[BALANCE] = Gson().toJson(balanceData)
        }
    }

    fun getBalanceData() = runBlocking(Dispatchers.IO) {
        Log.d("UserPreferences", "getUserData: ${userData.first()}")
        Gson().fromJson(balanceData.first().toString(), BalanceResponse::class.java)
    }

    suspend fun saveSyncDigitalCard(data: String) {
        Log.d("UserPreferences", "saveSyncDigitalCard: ${data}")
        appContext.dataStore.edit { preferences ->
            preferences[DIGITAL_CARD] = data
        }
    }
    suspend fun saveDigitalCard(value: String) {
        appContext.dataStore.edit { preferences ->
            preferences[DIGITAL_CARD] = value.toString()
        }
    }


    fun getSyncDigitalCard() = runBlocking(Dispatchers.IO) {
        val value = runBlocking { digitalCardData.first() }
        GsonBuilder().create().fromJson(value.toString(), SyncDigitalCard::class.java)
    }
    inline fun <reified T> getUserObject(): T? {
        val value = runBlocking { digitalCardData.first() }
        Log.d("UserPreferences", "getUserObject: ${value.toString()}")

        return GsonBuilder().create().fromJson(value.toString(), T::class.java)
    }

    val language: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[LANGUAGE] ?: "id"
        }

    fun getLanguage() = runBlocking(Dispatchers.IO) {
        language.first()
    }

    suspend fun saveLanguage(language: String) {
        appContext.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    val customApp: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CUSTOM_APP] ?: ""
        }
    fun getCustomApp() = runBlocking(Dispatchers.IO) {
       Gson().fromJson(customApp.first().toString(), CustomAppResponse::class.java) ?: null
    }
    fun saveCustomApp(customApp: CustomAppResponse) = runBlocking(Dispatchers.IO) {
        appContext.dataStore.edit { preferences ->
            preferences[CUSTOM_APP] = Gson().toJson(customApp)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USER_DATA = stringPreferencesKey("user_data")
        private val LAST_RESET_PASSWORD = stringPreferencesKey("last_reset_password")
        private val USERNAME = stringPreferencesKey("username")
        private val PASSWORD = stringPreferencesKey("password")
        private val INTRO = booleanPreferencesKey("intro")
        private val LANGUAGE = stringPreferencesKey("language")
        private val BALANCE = stringPreferencesKey("balance")
        private val DIGITAL_CARD = stringPreferencesKey("digital_card")
        private val SYNC_DC = stringPreferencesKey("sync_digital_card")
        private val CUSTOM_APP = stringPreferencesKey("custom_app")
    }

}
