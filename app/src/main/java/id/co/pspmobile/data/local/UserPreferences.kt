package id.co.pspmobile.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "psp_mobile_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context){

    private val appContext = context.applicationContext

    val accessToken: Flow<String>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    suspend fun saveAccessToken(headers: Headers) {
        val accessToken = headers.get("Authorization")
        if (accessToken !== null) {
            appContext.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = accessToken
            }
        }
    }

    fun getAccessToken() = runBlocking(Dispatchers.IO) {
        accessToken.first()
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

}