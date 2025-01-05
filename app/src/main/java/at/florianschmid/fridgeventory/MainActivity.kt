package at.florianschmid.fridgeventory

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import at.florianschmid.fridgeventory.ui.TodoApp
import at.florianschmid.fridgeventory.ExpiringItems.theme.FridgeventoryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContent {
            FridgeventoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoApp(Modifier.padding(innerPadding))
                }
            }
        }

    }

    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
            } else {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER GRANTED PERMISSION")
            }
        }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Action to take when permission is already granted
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    // Action to take when permission was denied permanently
                    Toast.makeText(this, "Permission denied permanently", Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(permission)
                }
            }
        } else {
            // Device does not support required permission
            Toast.makeText(this, "No required permission", Toast.LENGTH_LONG).show()
        }
    }

}