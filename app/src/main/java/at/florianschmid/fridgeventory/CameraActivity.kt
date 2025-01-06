package at.florianschmid.fridgeventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import at.florianschmid.fridgeventory.camera.CameraUI
import at.florianschmid.fridgeventory.ui.theme.FridgeventoryTheme

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridgeventoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraUI(Modifier.padding(innerPadding))
                }
            }
        }
    }

}