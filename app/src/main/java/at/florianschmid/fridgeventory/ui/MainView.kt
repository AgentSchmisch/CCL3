package at.florianschmid.fridgeventory.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

class MainApp {
    Scaffold(modifier = Modifier.fillMaxSize(),
    bottomBar = { Footer() }
    ) { innerPadding ->
        FridgeventoryApp(Modifier.padding(innerPadding))
    }
}