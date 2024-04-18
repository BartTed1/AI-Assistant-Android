package xyz.teodorowicz.assistant.services

import android.content.Context

class ChatService(context: Context) : IChatService {
    private val sharedPreferencesService = SharedPreferencesService(context)
}