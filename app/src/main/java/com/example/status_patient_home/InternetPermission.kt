package com.example.status_patient_home

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface InternetPermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun updateTextView(username : String)
}

class InternetPermission {

    companion object {
        const val INTERNET_PERMISSION_REQUEST_CODE = 1001 // arbitrary
    }

    fun checkInternetPermissionAndExecute(context: Context, callback: InternetPermissionCallback) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, execute network operation
            callback.onPermissionGranted()
        } else {
            // Request permission from user
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(android.Manifest.permission.INTERNET),
                    INTERNET_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    fun performNetworkOperation(updateTextView : (String) -> Unit) {
        // network operation code goes here
        // For example, call the function to update text view on UI thread
        updateTextView("John")
    }
}
