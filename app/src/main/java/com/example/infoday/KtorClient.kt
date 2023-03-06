package com.example.infoday

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

object KtorClient {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        expectSuccess = true
    }
    suspend fun getFeeds(): List<Feed> {
        return httpClient.get("https://api.npoint.io/a8cea79c033ace1c8b8b").body()
    }
}