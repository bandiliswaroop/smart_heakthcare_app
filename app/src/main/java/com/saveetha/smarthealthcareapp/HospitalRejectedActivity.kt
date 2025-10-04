package com.saveetha.smarthealthcareapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HospitalRejectedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_rejected)

        val messageText = findViewById<TextView>(R.id.rejectionMessage)
        messageText.text = "Your hospital registration was rejected.\nPlease contact support for more information."
    }
}
