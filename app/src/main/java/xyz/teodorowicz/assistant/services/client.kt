package xyz.teodorowicz.assistant.services

import android.content.Context
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import xyz.teodorowicz.assistant.R


val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        /*json(
            contentType = ContentType("application", "json"),
        )*/
        gson()
    }
    expectSuccess = true
}

fun baseUrl(context: Context): String = context.getString(R.string.server_url)