package xyz.teodorowicz.ai.services

interface ISharedPreferencesService {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun saveInt(key: String, value: Int)
    fun getInt(key: String): Int
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
}