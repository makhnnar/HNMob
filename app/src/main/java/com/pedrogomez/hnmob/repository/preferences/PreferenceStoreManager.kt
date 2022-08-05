package com.pedrogomez.hnmob.repository.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceStoreManager(var context: Context) {

    private val tag = "FormDataStoreManager"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hnmob_data_store")

    suspend fun <T> containsKey(key: Preferences.Key<T>): Boolean {
        try {
            return context.dataStore.data.first().contains(key)
        } catch (e: Exception) {
            Log.d(tag,"Error preferencesDataStore containsKey")
            throw e
        }
    }

    suspend fun <T> removeItem(key: Preferences.Key<T>) = context.dataStore.edit {
        try {
            it.remove(key)
        } catch (e: Exception) {
            Log.d(tag,"Error removing preferencesDataStore")
            throw e
        }
    }

    suspend fun <T> getValue(key: Preferences.Key<T>, defaultValueIfNull: T): T {
        try {
            return context.dataStore.data
                .map { preferences ->
                    preferences[key]
                }.first() ?: defaultValueIfNull
        } catch (e: Exception) {
            Log.d(tag,"Error getting preferencesDataStore values")
            return defaultValueIfNull
        }
    }

    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        try {
            context.dataStore.edit { preferences ->
                preferences[key] = value
            }
        } catch (e: Exception) {
            Log.d(tag,"Error setting preferencesDataStore values")
            throw e
        }
    }

    suspend fun getPreferenceKeys(): Set<Preferences.Key<*>> {
        try {
            return context.dataStore.data.first().asMap().keys
        } catch (e: Exception) {
            Log.d(tag,"Error getting preferencesDataStore values")
            throw e
        }
    }

    fun <T> fieldObserver(key: Preferences.Key<T>): Flow<T?> {
        try {
            return context.dataStore.data
                .map { preferences ->
                    preferences[key]
                }
        } catch (e: Exception) {
            Log.d(tag,"Error fieldObserver preferencesDataStore")
            throw e
        }
    }

    companion object{
        val IS_REQUIRED = booleanPreferencesKey("IsRequired")
        val USER_NAME = stringPreferencesKey("UserName")
        val DSN = intPreferencesKey("DSN")
        val LAST_TIME = longPreferencesKey(name = "lastTime")
    }
}