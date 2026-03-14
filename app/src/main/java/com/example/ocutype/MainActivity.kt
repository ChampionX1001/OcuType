package com.example.ocutype

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.ocutype.camera.EyeTrackingCameraManager
import com.example.ocutype.ui.theme.OcuTypeTheme
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var cameraManager: EyeTrackingCameraManager
    private val executor = Executors.newSingleThreadExecutor()
    
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            initializeEyeTracking()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        cameraManager = EyeTrackingCameraManager(this, this, executor)
        
        setContent {
            OcuTypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "OcuType",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        
        checkAndRequestCameraPermission()
    }
    
    private fun checkAndRequestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                initializeEyeTracking()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    private fun initializeEyeTracking() {
        // Initialize camera and eye tracking components
        // This will be expanded with TensorFlow Lite integration
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OcuTypeTheme {
        Greeting("Android")
    }
}