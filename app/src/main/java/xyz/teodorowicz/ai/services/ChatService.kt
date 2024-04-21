package xyz.teodorowicz.ai.services

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class ChatService(context: Context) : IChatService {
    private val sharedPreferencesService = SharedPreferencesService(context)
    // private val jwt = sharedPreferencesService.getString("jwt")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
}