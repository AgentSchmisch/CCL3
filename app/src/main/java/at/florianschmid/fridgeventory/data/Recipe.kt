package at.florianschmid.fridgeventory.data

import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.Serializable

data class RecipeRecommendation (
    val recommendation: JsonElement
){
    val id: Int = recommendation.jsonObject["id"]!!.jsonPrimitive.int
    val name: String = recommendation.jsonObject["title"]!!.jsonPrimitive.content
    val image: String = recommendation.jsonObject["image"]!!.jsonPrimitive.content
    val usedIngredientCount: Int = recommendation.jsonObject["usedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredientCount: Int = recommendation.jsonObject["missedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["missedIngredients"].toString())
    val usedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["usedIngrediens"].toString())
    val unusedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["unusedIngredients"].toString())
    val likes: Int = recommendation.jsonObject["likes"]!!.jsonPrimitive.int
}

@Serializable
data class Recipe(
    var name: String?,
    val steps: List<Step>,
    var imageUrl: String? = null // New property for image URL

)

@Serializable
data class Step(
    val equipment: List<Equipment>?,
    val ingredients: List<Ingredient>?,
    val number: Int,
    val step: String,
    val length: Length? = null // Optional as not all steps have length
)

@Serializable
data class Equipment(
    val id: Int,
    val image: String,
    val name: String,
    val temperature: Temperature? = null // Optional as not all equipment has temperature
)

@Serializable
data class Temperature(
    val number: Double,
    val unit: String
)

@Serializable
data class Ingredient(
    val id: Int,
    val image: String,
    val name: String
)

@Serializable
data class Length(
    val number: Int,
    val unit: String
)