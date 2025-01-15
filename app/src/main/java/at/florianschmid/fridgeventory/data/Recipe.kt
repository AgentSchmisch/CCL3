package at.florianschmid.fridgeventory.data

import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class Recipe (
    val recipe: JsonElement
){
    val id: Int = recipe.jsonObject["id"]!!.jsonPrimitive.int
    val title: String = recipe.jsonObject["title"]!!.jsonPrimitive.content
    val image: String = recipe.jsonObject["image"]!!.jsonPrimitive.content
    val usedIngredientCount: Int = recipe.jsonObject["usedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredientCount: Int = recipe.jsonObject["missedIngredientCount"]!!.jsonPrimitive.int
    val missedIngredients: JsonElement = parseToJsonElement(recipe.jsonObject["missedIngredients"].toString())
    val usedIngredients: JsonElement = parseToJsonElement(recipe.jsonObject["usedIngrediens"].toString())
    val unusedIngredients: JsonElement = parseToJsonElement(recipe.jsonObject["unusedIngredients"].toString())
    val likes: Int = recipe.jsonObject["likes"]!!.jsonPrimitive.int
}
