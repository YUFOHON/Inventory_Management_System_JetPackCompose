package com.example.infoday

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.json.JSONArray
import java.net.URLEncoder

//@Serializable
@Serializable
data class User(
    val firstname: String,
    val last_name: String,
    val token: String
)

object KtorClient {
    var token: String = ""
    var firstname: String = ""
    var lastname: String = ""

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("authorization", token)
        }
        expectSuccess = true
    }

    //sbeceril0@unc.edu
    @OptIn(InternalAPI::class)
    suspend fun login(email: String, password: String): Boolean {
        try {
//
            val json = "{\"email\":\"$email\",\"password\":\"$password\"}"
            val response: User = httpClient.post("http://comp4107.herokuapp.com/user/login") {
//            body = json
                setBody(json)
            }.body()
            token = response.token
            firstname = response.firstname
            lastname = response.last_name
//            Log.d("login", "token: $token")
            return true
        } catch (e: Exception) {
            Log.d("loginErr", "error: $e")
        }
        return false
    }

    suspend fun borrow(id: String): Boolean {
//http://comp4107.herokuapp.com/user/borrow/641beb90fc13ae2cf0000494
        try {
            //print the token
//            Log.d("token", "token: $token")
            if(token.isEmpty())
                return false
            val response: HttpResponse =
                httpClient.post("http://comp4107.herokuapp.com/user/borrow/$id").body()
            if (response.status.value == 200)
                return true
            Log.d("borrow", "response: $response")
        } catch (e: Exception) {
            Log.d("borrowErr", "error: $e")
        }
        return false
    }

    suspend fun consume(id: String): Boolean {
        try {
            if(token.isEmpty())
                return false

            val response: HttpResponse =
                httpClient.post("http://comp4107.herokuapp.com/user/consume/$id").body()
            if (response.status.value == 200)
                return true
            Log.d("consume", "response: $response")
        } catch (e: Exception) {
            Log.d("consumeErr", "error: $e")
        }
        return false
    }

    suspend fun returnItem(id: String): Boolean {
//http://comp4107.herokuapp.com/user/borrow/641beb90fc13ae2cf0000494
        try {
            //print the token
//            Log.d("token", "token: $token")
            val response: HttpResponse =
                httpClient.post("http://comp4107.herokuapp.com/user/return/$id").body()
            if (response.status.value == 200)
                return true
            Log.d("return", "response: $response")
        } catch (e: Exception) {
            Log.d("returnErr", "error: $e")
        }
        return false
    }

    suspend fun getBook(page: Int): List<Book> {
        try {

            return httpClient.get("http://comp4107.herokuapp.com/inventory?type=book&page=$page")
                .body()
        } catch (e: Exception) {
            Log.d("getBook", "error: $e")

        }
        return emptyList()
    }

    suspend fun getGame(page: Int): List<Game> {
        try {

            return httpClient.get("http://comp4107.herokuapp.com/inventory?type=game&page=$page")
                .body()
        } catch (e: Exception) {
            Log.d("getBook", "error: $e")

        }
        return emptyList()
    }

    suspend fun getGifts(page: Int): List<Gift> {

        try {
            return httpClient.get("http://comp4107.herokuapp.com/inventory?type=gift&page=$page")
                .body()
        } catch (e: Exception) {
            Log.d("getBook", "error: $e")
        }
        return emptyList()
    }

    suspend fun getMaterials(page: Int): List<Material> {
        try {
            return httpClient.get("http://comp4107.herokuapp.com/inventory?type=material&page=$page")
                .body()
        } catch (e: Exception) {
            Log.d("getBook", "error: $e")
        }
        return emptyList()
    }

    suspend fun search(keyword: String,page:Int): List<InventoryItem> {
        try {
            //lOG THE PAGE
            val encodedQuery = URLEncoder.encode(keyword, "UTF-8")
            val result = mutableListOf<InventoryItem>()
            val response: HttpResponse =
                httpClient.get("http://comp4107.herokuapp.com/inventory?keyword=$encodedQuery&page=$page")
            val responseBody: String = response.bodyAsText()
            val jsonArray = JSONArray(responseBody)
            val json = Json { ignoreUnknownKeys = true }
            for (i in 0 until jsonArray.length()) {
//                val item = json.decodeFromString<InventoryItem>(jsonArray[i].toString())
                val item = json.decodeFromString(InventoryItemSerializer(), jsonArray[i].toString())
                result.add(item)
            }
//            val item = json.decodeFromString(InventoryItemSerializer(), response.body())
            return result
        } catch (e: Exception) {
            Log.d("search", "error: $e")
        }
        return emptyList()
    }

}

class InventoryItemSerializer :
    JsonContentPolymorphicSerializer<InventoryItem>(InventoryItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out InventoryItem> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "book" -> Book.serializer()
            "game" -> Game.serializer()
            "gift" -> Gift.serializer()
            "material" -> Material.serializer()
            else -> throw IllegalArgumentException("Invalid inventory item type")
        }
    }
}