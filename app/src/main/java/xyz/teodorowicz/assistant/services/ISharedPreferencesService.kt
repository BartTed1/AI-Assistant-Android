package xyz.teodorowicz.assistant.services

import xyz.teodorowicz.assistant.models.User

interface ISharedPreferencesService {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun saveInt(key: String, value: Int)
    fun getInt(key: String): Int
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
    fun getUser(): User
}