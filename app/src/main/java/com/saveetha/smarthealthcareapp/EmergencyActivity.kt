package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergency)

        // Set system window insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Call button click listeners
        findViewById<ImageView>(R.id.callAmbulance).setOnClickListener {
            dialNumber("108")
        }

        findViewById<ImageView>(R.id.callPolice).setOnClickListener {
            dialNumber("100")
        }

        findViewById<ImageView>(R.id.call104).setOnClickListener {
            dialNumber("104")
        }

        findViewById<ImageView>(R.id.callDisha).setOnClickListener {
            dialNumber("1056")
        }

        // Optional: Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
    }

    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        startActivity(intent)
    }
}
