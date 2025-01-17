package at.florianschmid.fridgeventory.data

import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class RecipeRecommendation (
    val recommendation: JsonElement
){
    val id: Int = recommendation.jsonObject["id"]!!.jsonPrimitive.int
    val title: String = recommendation.jsonObject["title"]!!.jsonPrimitive.content
    val image: String = recommendation.jsonObject["image"]!!.jsonPrimitive.content
    val usedIngredientCount: Int = recommendation.jsonObject["usedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredientCount: Int = recommendation.jsonObject["missedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["missedIngredients"].toString())
    val usedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["usedIngrediens"].toString())
    val unusedIngredients: JsonElement = parseToJsonElement(recommendation.jsonObject["unusedIngredients"].toString())
    val likes: Int = recommendation.jsonObject["likes"]!!.jsonPrimitive.int
}

data class Recipe(
    val recipe: JsonElement
){
    
}
