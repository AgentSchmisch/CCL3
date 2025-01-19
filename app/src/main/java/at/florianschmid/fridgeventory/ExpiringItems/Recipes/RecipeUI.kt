package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.Equipment
import at.florianschmid.fridgeventory.data.Ingredient
import at.florianschmid.fridgeventory.data.Recipe
import at.florianschmid.fridgeventory.data.Step
import at.florianschmid.fridgeventory.data.Temperature
import at.florianschmid.fridgeventory.ui.theme.Typography
import coil3.compose.AsyncImage

@Composable
fun RecipeUI(
    modifier: Modifier = Modifier,
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by recipeViewModel.recipe.collectAsStateWithLifecycle()
    val recipeStep = recipeViewModel.recipeStep.collectAsState()

    val recipe = state.recipe

    Column(modifier) {
        if (recipe != null) {
            RecipeDisplay(recipe, recipeStep.value.step, modifier)
        } else {
            DefaultRecipe()
        }
    }
}


@Composable
fun RecipeDisplay(recipe: Recipe, recipeStep: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            "${recipe.name}",
            style = Typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            color = colorResource(R.color.f_dark_purple)
        )
        //Image of the recipe
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = "Image of ${recipe.name}",
        )
        if (recipeStep == 0) {
            IngredientDisplay(recipe)

        } else if (recipeStep > 0 && recipeStep <= recipe.steps.size) {
            RecipeStepDisplay(recipe, recipeStep)

        } else {
            RecipeFinishedDisplay()
        }
        RecipeStepNavigator(currentStep = recipeStep, steps = recipe.steps.size)
    }
}

@Composable
fun RecipeStepDisplay(recipe: Recipe, step: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        Column(Modifier.padding(vertical = 8.dp)) {
            Text(
                "Step $step",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = colorResource(R.color.f_dark_purple)
            )
            recipe.steps.forEach {
                if (it.number == step) {
                    Text(
                        it.step,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                        color = colorResource(R.color.f_dark_purple)
                    )
                }
            }
        }
    }

}

@Composable
fun IngredientDisplay(recipe: Recipe) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        Column(Modifier.padding(vertical = 8.dp)) {
            Text(
                "Ingredients",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = colorResource(R.color.f_dark_purple)
            )
            recipe.steps.forEach { it ->
                it.ingredients?.forEach {
                    Text(
                        it.name,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                        color = colorResource(R.color.f_dark_purple)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeFinishedDisplay() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center // Center content horizontally and vertically
    ) {
        Column(
            Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Recipe Finished",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = colorResource(R.color.f_dark_purple)
            )
            Text(
                "Enjoy your meal!",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = colorResource(R.color.f_dark_purple)
            )
        }
    }
}

@Composable
fun DefaultRecipe() {
    Text("No Recipe")
}

@Composable
fun RecipeStepNavigator(
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    steps: Int = 0,
    currentStep: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                if (currentStep > 0) {
                    recipeViewModel.decrementRecipeStep()
                }
            },
            modifier = Modifier.background(
                color = colorResource(id = R.color.f_pink),
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Next Step",
            )
        }

        IconButton(
            onClick = {
                if (currentStep <= steps) {
                    recipeViewModel.incrementRecipeStep()
                }
            },
            modifier = Modifier.background(
                color = colorResource(id = R.color.f_pink),
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next Step"
            )
        }


    }
}


@Preview
@Composable
fun RecipeUIPreview() {
    RecipeStepNavigator()
}

@Preview
@Composable
fun RecipeDisplayPreview() {

    IngredientDisplay(mockRecipe)
}

@Preview
@Composable
fun RecipeStepPreview() {
    RecipeStepDisplay(mockRecipe, 1)
}

@Preview
@Composable
fun RecipeFinishedPreview() {
    RecipeFinishedDisplay()
}

val mockRecipe = Recipe(
    name = "Pancake Recipe",
    steps = listOf(
        Step(
            equipment = listOf(
                Equipment(
                    id = 404784,
                    image = "oven.jpg",
                    name = "oven",
                    temperature = Temperature(
                        number = 200.0,
                        unit = "Fahrenheit"
                    )
                )
            ),
            ingredients = listOf(
                Ingredient(
                    id = 19334,
                    image = "light-brown-sugar.jpg",
                    name = "light brown sugar"
                ),
                Ingredient(
                    id = 20081,
                    image = "flour.png",
                    name = "all purpose flour"
                )
            ),
            number = 1,
            step = "Preheat the oven to 200 degrees F."
        )
    ),
    imageUrl = "path/to/images/recipe.jpg"
)