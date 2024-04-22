package xyz.teodorowicz.assistant.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val aboutUser: String,
    val userLocation: String
)
