package com.example.infoday

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class HttpBinResponse(
    val args: Map<String, String>,
    val data: String,
    val files: Map<String, String>,
    val form: Map<String, String>,
    val headers: Map<String, String>,
    val json: String?,
    val origin: String,
    val url: String
)
object KtorClient {
    private var token: String = ""

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Authorization", "Bearer " + token)
        }
        expectSuccess = true
    }
    suspend fun postFeedback(feedback: String): String {

        val response: HttpBinResponse = httpClient.post("https://httpbin.org/post") {
            setBody(feedback)
        }.body()

        token = response.headers["X-Amzn-Trace-Id"].toString()
        return response.toString()
    }
    suspend fun getFeeds(): List<Feed> {
        return httpClient.get("https://api.npoint.io/a8cea79c033ace1c8b8b").body()
    }
}