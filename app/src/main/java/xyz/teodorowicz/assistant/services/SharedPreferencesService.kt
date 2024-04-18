package xyz.teodorowicz.assistant.services

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesService(context: Context) : ISharedPreferencesService {
    private lateinit var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("xyz.teodorowicz.assistant", Context.MODE_PRIVATE)
    }

    override fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}