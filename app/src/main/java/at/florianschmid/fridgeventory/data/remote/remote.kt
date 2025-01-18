package at.florianschmid.fridgeventory.data.remote

import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.Recipe
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteService {

    private val client = OkHttpClient()

    suspend fun fetchRecipeRecommendations(ingredients: List<Item>): List<RecipeRecommendation> =
        suspendCancellableCoroutine { continuation ->
            val urlBuilder = HttpUrl.Builder()
                .scheme("https")
                .host("mock-cfe60b19bc9a4ddaad1bb22b30daa6e7.mock.insomnia.rest")
                .addPathSegment("recipes")
                .addPathSegment("findByIngredients")
                .apply {
                    addQueryParameter("ingredients", ingredients.joinToString(",") { it.name })
                    addQueryParameter("apiKey", "88043b90495647d2bb44a84e05e0eb78")
                }

            val request = Request.Builder()
                .url(urlBuilder.build())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            if (continuation.isActive) continuation.resumeWithException(
                                IOException(
                                    "Unexpected code $response"
                                )
                            )
                            return
                        }
                        try {
                            val responseString = response.body!!.string()
                            val jsonRecommendation = Json.parseToJsonElement(responseString)
                            val recommendations =
                                jsonRecommendation.jsonArray.map { recommendation ->
                                    RecipeRecommendation(recommendation)
                                }
                            if (continuation.isActive) continuation.resume(recommendations)
                        } catch (e: Exception) {
                            if (continuation.isActive) continuation.resumeWithException(e)
                        }
                    }
                }
            })
        }


    suspend fun fetchRecipeDetails(recipeId: Int, recipeName: String, recipeImage: String): Recipe =
        suspendCancellableCoroutine { continuation ->
            val urlBuilder = HttpUrl.Builder()
                //https://mock-cfe60b19bc9a4ddaad1bb22b30daa6e7.mock.insomnia.rest/recipes/findByIngredients
                .scheme("https")
                .host("mock-cfe60b19bc9a4ddaad1bb22b30daa6e7.mock.insomnia.rest")
                .addPathSegment("recipes")
                .addPathSegment(recipeId.toString())
                .addPathSegment("analyzedInstructions")

            val request = Request.Builder()
                .url(urlBuilder.build())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            if (continuation.isActive) continuation.resumeWithException(
                                IOException(
                                    "Unexpected code $response"
                                )
                            )
                            return
                        }
                        try {
                            val responseString = response.body!!.string()
                            val recipes: List<Recipe> = Json { ignoreUnknownKeys = true }.decodeFromString(responseString)
                            val recipeDetail = recipes.first()
                            recipeDetail.name = recipeName
                            recipeDetail.imageUrl = recipeImage

                            if (continuation.isActive) continuation.resume(recipeDetail)
                        } catch (e: Exception) {
                            if (continuation.isActive) continuation.resumeWithException(e)
                        }
                    }
                }
            })
        }
}
