package at.florianschmid.fridgeventory.data

import at.florianschmid.fridgeventory.data.db.ItemEntity
import at.florianschmid.fridgeventory.data.db.RecommendationDao
import at.florianschmid.fridgeventory.data.db.RecommendationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json.Default.parseToJsonElement

class RecommendationRepository(private val recommendationDao: RecommendationDao) {

    suspend fun findItemById(id: Int): Recipe {
        val item = recommendationDao.findItemById(id)
        return Recipe(
            parseToJsonElement("{\"id\":${item._id},\"title\":\"${item.title}\",\"usedIngredientCount\":${item.usedIngredients},\"missedIngredientCount\":${item.missingIngredients},\"image\":\"${item.image_path}\"}")

        )
    }

    suspend fun addItem(item: Recipe) {
        recommendationDao.addItem(RecommendationEntity(_id=item.id, item.title, item.usedIngredientCount, item.missedIngredientCount, item.image))
    }
}
