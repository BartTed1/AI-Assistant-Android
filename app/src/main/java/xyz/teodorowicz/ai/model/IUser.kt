package xyz.teodorowicz.ai.model

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred

interface IUser {
    val uid: String
    val firstName: String
    val lastName: String
    val email: String
    val photoUrl: String

    suspend fun register(context: Context): Deferred<Any>
}