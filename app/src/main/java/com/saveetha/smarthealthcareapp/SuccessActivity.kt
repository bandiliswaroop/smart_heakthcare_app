package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SuccessActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val lottieCheck = findViewById<LottieAnimationView>(R.id.successAnim)
        val successText = findViewById<TextView>(R.id.successText)
        val btnGoHome = findViewById<Button>(R.id.btnGoHome)

        // Fade-in for text and button
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 800
        }

        // Play sound on start
        mediaPlayer = MediaPlayer.create(this, R.raw.success_sound)
        mediaPlayer?.start()

        lottieCheck.addAnimatorUpdateListener {
            if (it.animatedFraction >= 0.9f && successText.alpha == 0f) {
                successText.startAnimation(fadeIn)
                successText.alpha = 1f

                btnGoHome.startAnimation(fadeIn)
                btnGoHome.alpha = 1f
            }
        }

        btnGoHome.setOnClickListener {
            val intent = Intent(this, PatientHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
