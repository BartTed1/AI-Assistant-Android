package xyz.teodorowicz.ai.models

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.Serializable
import xyz.teodorowicz.ai.services.baseUrl
import xyz.teodorowicz.ai.services.client

@Serializable
data class User(
    override val uid: String,
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val photoUrl: String
) : IUser {
    companion object {
        fun fromFirebaseUser(firebaseUser: FirebaseUser): User {
            return User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                firstName = firebaseUser.displayName?.split(" ")?.get(0) ?: "",
                lastName = firebaseUser.displayName?.split(" ")?.get(1) ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
        }
    }
}