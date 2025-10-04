//package com.saveetha.smarthealthcareapp
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//class NotificationPermissionActivity : AppCompatActivity() {
//
//    private val NOTIFICATION_PERMISSION_CODE = 101
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_notification_permission)
//
//        val allowBtn: Button = findViewById(R.id.btnAllow)
//        val denyBtn: Button = findViewById(R.id.btnDeny)
//
//        allowBtn.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                        NOTIFICATION_PERMISSION_CODE
//                    )
//                } else {
//                    Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            } else {
//                finish() // Permission not needed below Android 13
//            }
//        }
//
//        denyBtn.setOnClickListener {
//            Toast.makeText(this, "Notifications permission denied", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//    }
//}
package com.saveetha.smarthealthcareapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NotificationPermissionActivity : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_permission)

        val allowBtn: Button = findViewById(R.id.btnAllow)
        val denyBtn: Button = findViewById(R.id.btnDeny)

        allowBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_CODE
                    )
                } else {
                    Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
                    openLocationPermissionActivity()
                    finish()
                }
            } else {
                // Below Android 13, no permission needed, just proceed
                openLocationPermissionActivity()
                finish()
            }
        }

        denyBtn.setOnClickListener {
            Toast.makeText(this, "Notifications permission denied", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                openLocationPermissionActivity()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun openLocationPermissionActivity() {
        val intent = Intent(this, LocationPermissionActivity::class.java)
        startActivity(intent)
    }
}
