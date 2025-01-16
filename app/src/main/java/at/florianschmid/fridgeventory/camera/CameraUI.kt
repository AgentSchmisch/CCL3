package at.florianschmid.fridgeventory.camera

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import at.florianschmid.fridgeventory.R

@Composable
fun CameraUI(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = PreviewView(context)
    val imageCapture = ImageCapture.Builder().build()

    // Initialize the camera
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }, ContextCompat.getMainExecutor(context))

    // Display the preview and trigger button
    AndroidView(factory = { previewView }, modifier = modifier.fillMaxSize())
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ImageTrigger(modifier=Modifier.padding(bottom=56.dp).size(105.dp), onClick = { takeImage(context, imageCapture) })
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun ImageTrigger(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(96.dp) // Outer circle size
                .border(4.dp, Color.White, CircleShape), // Circular border
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_alt_48),
                contentDescription = "Take picture",
                tint = Color.White,
                modifier = Modifier.size(64.dp).padding(8.dp) // Inner icon size
            )
        }
    }
}


private fun takeImage(context: Context, imageCapture: ImageCapture) {
    val name = "IMG_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val resultIntent = Intent().apply {
                    putExtra("image_uri", outputFileResults.savedUri.toString())
                }
                (context as Activity).setResult(Activity.RESULT_OK, resultIntent)
                (context).finish()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error capturing image.", Toast.LENGTH_SHORT).show()
            }
        }
    )
}


@Composable
@androidx.compose.ui.tooling.preview.Preview
fun CameraUIPreview() {
        Box(
            modifier = Modifier
                .fillMaxSize().padding(bottom=56.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ImageTrigger(modifier=Modifier.padding(bottom=56.dp).size(105.dp),onClick = {  })
            Spacer(modifier = Modifier.height(16.dp))

        }
}
