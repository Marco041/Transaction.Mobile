package com.example.transaction.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.transaction.dto.TransactionSetting
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsStore {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "transaction_settings")
    private val ZIP_BACKUP = stringPreferencesKey("zip_backup")
    private val ZIP_PASSWORD = stringPreferencesKey("zip_password")

    suspend fun savePreferenceValue(context: Context, settings: TransactionSetting) {
        context.dataStore.edit { preferences ->
            preferences[ZIP_BACKUP] = settings.zipBackup
            preferences[ZIP_PASSWORD] = settings.zipPassword
        }
    }

    suspend fun getPreferenceValue(context: Context): TransactionSetting {
        return context.dataStore.data.map { preferences ->
            TransactionSetting(
                preferences[ZIP_BACKUP] ?: "",
                preferences[ZIP_PASSWORD] ?: ""
            )
        }.first()
    }
}