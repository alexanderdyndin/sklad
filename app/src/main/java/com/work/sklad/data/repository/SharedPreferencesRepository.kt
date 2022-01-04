package com.work.sklad.data.repository

import android.content.SharedPreferences
import com.work.sklad.domain.repository.ISharedPreferencesRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(private val prefs: SharedPreferences): ISharedPreferencesRepository {

    override fun get(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun get(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    override fun get(key: String, defaultValue: Long): Long {
        return prefs.getLong(key, defaultValue)
    }

    override fun get(key: String, defaultValue: Float): Float {
        return prefs.getFloat(key, defaultValue)
    }

    override fun get(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue)!!
    }

    override fun get(key: String, defaultValue: Set<String>): Set<String> {
        return prefs.getStringSet(key, defaultValue)!!
    }


    override fun set(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun set(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    override fun set(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    override fun set(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    override fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun set(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}