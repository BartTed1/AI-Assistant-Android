package xyz.teodorowicz.ai.models

import android.content.Context
import kotlinx.coroutines.Deferred

interface IUser {
    val uid: String
    val firstName: String
    val lastName: String
    val email: String
    val photoUrl: String
}