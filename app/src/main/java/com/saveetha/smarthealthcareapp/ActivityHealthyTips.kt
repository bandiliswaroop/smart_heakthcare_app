package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityHealthyTips : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthy_tips)

        val btnFever = findViewById<LinearLayout>(R.id.btnFever)
        val btnCold = findViewById<LinearLayout>(R.id.btnCold)
        val btnHeadache = findViewById<LinearLayout>(R.id.btnHeadache)
        val btnBackPain = findViewById<LinearLayout>(R.id.btnBackPain)
        val btnBodyPain = findViewById<LinearLayout>(R.id.btnBodyPain)
        val btnStomachPain = findViewById<LinearLayout>(R.id.btnStomachPain)

        // Set the titles
        btnFever.findViewById<TextView>(R.id.tvTipTitle).text = "Fever"
        btnCold.findViewById<TextView>(R.id.tvTipTitle).text = "Cold"
        btnHeadache.findViewById<TextView>(R.id.tvTipTitle).text = "Headache"
        btnBackPain.findViewById<TextView>(R.id.tvTipTitle).text = "Back Pain"
        btnBodyPain.findViewById<TextView>(R.id.tvTipTitle).text = "Body Pain"
        btnStomachPain.findViewById<TextView>(R.id.tvTipTitle).text = "Stomach Pain"

        // Set click listeners to open individual tip pages
        btnFever.setOnClickListener {
            startActivity(Intent(this, FeverTipsActivity::class.java))
        }

        btnCold.setOnClickListener {
            startActivity(Intent(this, ColdTipsActivity::class.java))
        }
        btnBackPain.setOnClickListener {
            startActivity(Intent(this, BackPainTipActivity::class.java))
        }
        btnBodyPain.setOnClickListener {
            startActivity(Intent(this, BodyPainTipActivity::class.java))
        }
        btnStomachPain.setOnClickListener {
            startActivity(Intent(this, StomachPainTipsActivity::class.java))
        }
        btnHeadache.setOnClickListener {
            startActivity(Intent(this, HeadaceTipsActivity::class.java))
        }
    }
}


