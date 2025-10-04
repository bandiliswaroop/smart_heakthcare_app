package com.saveetha.smarthealthcareapp

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FeverTipsActivity : AppCompatActivity() {

    private lateinit var tips: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fever_tips) // Replace with your XML filename

        tips = listOf(
            findViewById(R.id.tip1),
            findViewById(R.id.tip2),
            findViewById(R.id.tip3),
            findViewById(R.id.tip4),
            findViewById(R.id.tip5),
            findViewById(R.id.tip6)
        )

        prepareInitialStates()
        animateTipsSequentially()
    }

    private fun prepareInitialStates() {
        // Set all tips to smaller scale and transparent
        for (tip in tips) {
            tip.scaleX = 0.8f
            tip.scaleY = 0.8f
            tip.alpha = 0f
        }
    }

    private fun animateTipsSequentially() {
        var delay = 0L
        for (tip in tips) {
            Handler(Looper.getMainLooper()).postDelayed({
                tip.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(500)
                    .start()

                // Optional background flash
                tip.setBackgroundColor(Color.parseColor("#DDF2FF")) // light blue
                Handler(Looper.getMainLooper()).postDelayed({
                    tip.setBackgroundColor(Color.TRANSPARENT)
                }, 1000)

            }, delay)
            delay += 1000L // 1 second delay between tips
        }
    }
}

//class FeverTipsActivity : AppCompatActivity() {
//
//    private lateinit var tipViews: List<View>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_fever_tips)
//
//        // Initialize tip views
//        tipViews = listOf(
//            findViewById(R.id.tip1),
//            findViewById(R.id.tip2),
//            findViewById(R.id.tip3),
//            findViewById(R.id.tip4),
//            findViewById(R.id.tip5),
//            findViewById(R.id.tip6)
//        )
//
//        animateTipsSequentially()
//    }
//
//    private fun animateTipsSequentially() {
//        val animation = AnimationUtils.loadAnimation(this, R.anim.pop_in)
//
//        var delay = 0L
//        for (tip in tipViews) {
//            Handler(Looper.getMainLooper()).postDelayed({
//                tip.startAnimation(animation)
//                tip.visibility = View.VISIBLE
//            }, delay)
//            delay += 800L // Delay between each tip
//        }
//    }
//}

