package id.co.pspmobile.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "psp_mobile_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context){

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

    suspend fun saveUserData(userData: CheckCredentialResponse) {
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
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USER_DATA = stringPreferencesKey("user_data")
        private val LAST_RESET_PASSWORD = stringPreferencesKey("last_reset_password")
        private val USERNAME = stringPreferencesKey("username")
        private val PASSWORD = stringPreferencesKey("password")
    }

}