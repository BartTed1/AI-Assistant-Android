package xyz.teodorowicz.assistant.models

import com.google.firebase.auth.FirebaseUser

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