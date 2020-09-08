package com.example.pdfgeneratorapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pdfgeneratorapplication.PermissionsActivity

class PermissionsActivity : AppCompatActivity() {
    private var checker: PermissionsChecker? = null
    private var requiresCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            throw RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.")
        }
        checker = PermissionsChecker(this)
        requiresCheck = true
    }

    override fun onResume() {
        super.onResume()
        if (requiresCheck) {
            val permissions = permissions
            if (checker!!.lacksPermissions(permissions.toString())) {
                requestPermissions(*permissions!!)
            } else {
                allPermissionsGranted()
            }
        } else {
            requiresCheck = true
        }
    }

    private val permissions: Array<String>?
        private get() = intent.getStringArrayExtra(EXTRA_PERMISSIONS)

    private fun requestPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    private fun allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true
            allPermissionsGranted()
        } else {
            requiresCheck = false
            showMissingPermissionDialog()
        }
    }

    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    private fun showMissingPermissionDialog() {
        val dialogBuilder = AlertDialog.Builder(this@PermissionsActivity)
        dialogBuilder.setTitle("Liberar Permissão")
        dialogBuilder.setMessage("Libera permissao ?")
        dialogBuilder.setNegativeButton("Não") { dialog, which ->
            setResult(PERMISSIONS_DENIED)
            finish()
        }
        dialogBuilder.setPositiveButton("Sim") { dialog, which -> startAppSettings() }
        dialogBuilder.show()
    }

    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data =
            Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }

    companion object {
        const val PERMISSIONS_GRANTED = 0
        const val PERMISSIONS_DENIED = 1
        const val PERMISSION_REQUEST_CODE = 0
        private const val EXTRA_PERMISSIONS = ""
        private const val PACKAGE_URL_SCHEME = "package:"
        fun startActivityForResult(
            activity: Activity,
            requestCode: Int,
            vararg permissions: String?
        ) {
            val intent = Intent(activity, PermissionsActivity::class.java)
            intent.putExtra(EXTRA_PERMISSIONS, permissions)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
        }
    }
}