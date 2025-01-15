package at.florianschmid.fridgeventory.data.remote

import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.Recipe
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class RemoteService {

    private val client = OkHttpClient()
    //https://api.spoonacular.com/recipes/findByIngredients
    var recipeRecommendations = mutableListOf<Recipe>()

    fun fetchRecipeRecommendations (ingredients: List<Item>): List<Recipe> {
        val urlBuilder = Request.Builder()
            .url("https://mock-6e4ab8419c094b0a89a8a0c68f460176.mock.insomnia.rest/recipes/findByIngredients\n")
            .build().url.newBuilder()

        val request = Request.Builder()
            .url(urlBuilder.build())
            .addQueryParameter("ingredients", ingredients.joinToString(",") { it.name })
            .addQueryParameter("apiKey", "88043b90495647d2bb44a84e05e0eb78")
            .build()


        println("Request: $request")


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    var responseString = response.body!!.string()
                    var jsonRecipe = parseToJsonElement(responseString)
                    for (recipe in jsonRecipe.jsonArray) {
                        recipeRecommendations.add(Recipe(recipe = parseToJsonElement(recipe.jsonObject.toString())))
                    }
                }
            }
        }
        )
        return recipeRecommendations
    }


    fun fetchRecipeDetails(recipeId:Int){
        //TODO implement
    }
}

fun Request.Builder.addQueryParameter(name: String, value: String): Request.Builder {
    val urlBuilder = this.build().url.newBuilder()
    urlBuilder.addQueryParameter(name, value)
    return this.url(urlBuilder.build())
}


