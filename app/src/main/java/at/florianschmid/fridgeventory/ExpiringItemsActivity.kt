package at.florianschmid.fridgeventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import at.florianschmid.fridgeventory.ExpiringItems.ExpiringItemsUI
import at.florianschmid.fridgeventory.ui.theme.FridgeventoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpiringItemsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridgeventoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpiringItemsUI(Modifier.padding(innerPadding), navController = rememberNavController())
                }
            }
        }
    }
}

