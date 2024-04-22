package xyz.teodorowicz.assistant.services

import android.content.Context
import android.content.SharedPreferences
import xyz.teodorowicz.assistant.models.User

class SharedPreferencesService(context: Context) : ISharedPreferencesService {
    private val sharedPreferences = context.getSharedPreferences("xyz.teodorowicz.assistant", Context.MODE_PRIVATE)

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

    override fun getUser(): User {
        return User(
            uid = getString("uid") ?: "",
            firstName = getString("firstName") ?: "",
            lastName = getString("lastName") ?: "",
            email = getString("email") ?: "",
            photoUrl = getString("photoUrl") ?: ""
        )
    }
}