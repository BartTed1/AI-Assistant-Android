package xyz.teodorowicz.assistant.services

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import xyz.teodorowicz.assistant.models.User
import xyz.teodorowicz.assistant.models.UserSettings
import xyz.teodorowicz.assistant.models.UserSettingsResponse
import java.net.ConnectException

class UserService(
    private val user: User,
    private val activity: ComponentActivity
) {
    private val authService = AuthService(activity)

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun register(context: Context, token: String?): Deferred<Any> {
        return GlobalScope.async {
            try {
                if (token == null) throw Exception("Token is null")
                val url = "${baseUrl(context)}/auth/register"
                val response = client.post(url) {
                    headers {
                        append("Authorization", "Bearer $token")
                    }
                    contentType(io.ktor.http.ContentType.Application.Json)
                    setBody(user)
                }
                return@async true
            } catch (e: Exception) {
                return@async e
            }
        }
    }

    suspend fun getUserSettings(): UserSettings? {
        val token = authService.getToken() // suspend function call
        return try {
            val url = "${baseUrl(activity)}/user/settings"
            val response = client.get(url) {
                headers {
                    append("Authorization", "Bearer $token")
                }
                contentType(io.ktor.http.ContentType.Application.Json)
                parameter("uid", user.uid)
            }
            response.body<UserSettings>()
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("UserService", "token: $token")
                Log.e("UserService", "Failed to get user settings", e)
            }
            null
        }
    }

    suspend fun saveUserSettings(aboutUser: String, userLocation: String): UserSettingsResponse {
        val token = authService.getToken() // suspend function call
        return try {
            val url = "${baseUrl(activity)}/user/settings"
            val response = client.post(url) {
                headers {
                    append("Authorization", "Bearer $token")
                }
                contentType(io.ktor.http.ContentType.Application.Json)
                setBody(UserSettings(aboutUser, userLocation))
                parameter("uid", user.uid)
            }
            response.body<UserSettingsResponse>()
        } catch (e: Exception) {
            activity.runOnUiThread {
                Log.e("UserService", "token: $token")
                Log.e("UserService", "Failed to save user settings", e)
            }
            if (e is ConnectException && e.message == "Network is unreachable") {
                UserSettingsResponse(false, "Network is unreachable", status = 503, null)
            } else {
                UserSettingsResponse(false, "Internal server error", status = 500, null)
            }
        }
    }
}