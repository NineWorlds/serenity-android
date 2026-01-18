package us.nineworlds.serenity.core.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.SharedPreferencesMigration

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "serenity_preferences",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "${context.packageName}_preferences"))
    }
)

@Singleton
class SettingsRepository @Inject constructor(
    private val context: Context
) {

    fun getString(key: String, defaultValue: String?): String? = runBlocking {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }

    fun setString(key: String, value: String) = runBlocking {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
        Unit
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean = runBlocking {
        val prefKey = booleanPreferencesKey(key)
        context.dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }

    fun setBoolean(key: String, value: Boolean) = runBlocking {
        val prefKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
        Unit
    }

    fun getInt(key: String, defaultValue: Int): Int = runBlocking {
        val prefKey = intPreferencesKey(key)
        context.dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }

    fun setInt(key: String, value: Int) = runBlocking {
        val prefKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
        Unit
    }

    fun contains(key: String): Boolean = runBlocking {
        // DataStore doesn't have a direct 'contains' that is efficient without reading.
        // We check if the key exists in the current preferences.
        context.dataStore.data.map { preferences ->
            preferences.contains(stringPreferencesKey(key)) || 
            preferences.contains(booleanPreferencesKey(key)) ||
            preferences.contains(intPreferencesKey(key)) ||
            preferences.contains(longPreferencesKey(key)) ||
            preferences.contains(floatPreferencesKey(key)) ||
            preferences.contains(doublePreferencesKey(key))
        }.first()
    }

    fun remove(key: String) = runBlocking {
        val stringKey = stringPreferencesKey(key)
        val booleanKey = booleanPreferencesKey(key)
        val intKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(stringKey)
            preferences.remove(booleanKey)
            preferences.remove(intKey)
        }
        Unit
    }
}
