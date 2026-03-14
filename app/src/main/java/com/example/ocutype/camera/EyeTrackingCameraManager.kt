package com.example.ocutype.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.guava.await
import java.util.concurrent.Executor

class EyeTrackingCameraManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val executor: Executor
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null

    suspend fun initializeCamera(): Boolean {
        return try {
            val provider = ProcessCameraProvider.getInstance(context).await()
            cameraProvider = provider
            
            // Set up front-facing camera for eye tracking
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            
            // Configure preview for real-time processing
            preview = Preview.Builder().build()
            
            // Unbind any existing use cases
            provider.unbindAll()
            
            // Bind camera to lifecycle
            provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPreview(): Preview? = preview

    suspend fun startCamera(): Boolean {
        return if (hasCameraPermission()) {
            initializeCamera()
        } else {
            false
        }
    }
}
