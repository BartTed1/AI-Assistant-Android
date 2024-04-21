package xyz.teodorowicz.assistant.services

import android.content.Context
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.teodorowicz.assistant.models.User

class UserService(private val user: User) {
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
}