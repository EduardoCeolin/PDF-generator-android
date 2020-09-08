package com.example.pdfgeneratorapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionsChecker(
    /**
     * Context
     */
    private val context: Context
) {
    fun lacksPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun lacksPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_DENIED
    }

    companion object {
        /**
         * Permission List
         */
        var REQUIRED_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        /**
         * GPS Permission
         */
        var GPS_PERMISSION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}