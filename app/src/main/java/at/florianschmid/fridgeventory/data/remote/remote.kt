package at.florianschmid.fridgeventory.data.remote

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class RemoteService {

    private val client = OkHttpClient()

    fun run() {
        val urlBuilder = Request.Builder()
            .url("https://api.spoonacular.com/recipes/findByIngredients")
            .build().url.newBuilder()

        val request = Request.Builder()
            .url(urlBuilder.build())
            .addQueryParameter("ingredients", "apples,flour,sugar")
            .addQueryParameter("apiKey", "YOUR_API_KEY")
            .build()



        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
    }
}


fun Request.Builder.addQueryParameter(name: String, value: String): Request.Builder {
    val urlBuilder = this.build().url.newBuilder()
    urlBuilder.addQueryParameter(name, value)
    return this.url(urlBuilder.build())
}


