package at.florianschmid.fridgeventory

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import android.Manifest
import at.florianschmid.fridgeventory.ui.Footer
import at.florianschmid.fridgeventory.ui.FridgeventoryApp
import at.florianschmid.fridgeventory.ui.theme.FridgeventoryTheme
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {
    var cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        requestNotificationPermission()
        requestCameraPermission()
        setContent {
            FridgeventoryTheme {
                MainView()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("PERMISSION_MANAGER", "USER DENIED PERMISSION")
            } else {
                Log.d("PPERMISSION_MANAGER", "USER GRANTED PERMISSION")
            }
        }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED ->
                    {  }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> { }
                else -> {
                    // Request permission if not granted
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }