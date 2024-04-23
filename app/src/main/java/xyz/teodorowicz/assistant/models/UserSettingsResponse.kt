package xyz.teodorowicz.assistant.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettingsResponse(
    val success: Boolean,
    val message: String,
    val status: Int,
    val data: UserSettings?
)
