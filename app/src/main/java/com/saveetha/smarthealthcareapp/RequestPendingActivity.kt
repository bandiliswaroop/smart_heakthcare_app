//package com.saveetha.smarthealthcareapp
//
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class RequestPendingActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_request_pending)
//
//        val userId = intent.getStringExtra("user_id") ?: "Unknown"
//        val textStatus = findViewById<TextView>(R.id.tvRequestStatus)
//        textStatus.text = "Your request has been submitted successfully.\n\n" +
//                "Admin will review your application.\nOnce approved, you can access your dashboard."
//    }
//}
package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RequestPendingActivity : AppCompatActivity() {

    private lateinit var textStatus: TextView
    private var requestId: String = ""

    // 🔑 make handler & runnable class‑level so we can clean them up
    private val handler = Handler(Looper.getMainLooper())
    private val pollRunnable = object : Runnable {
        override fun run() {
            checkApprovalStatus()
            handler.postDelayed(this, 5_000) // poll every 5 s
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_pending)

        textStatus = findViewById(R.id.tvRequestStatus)
        requestId  = intent.getStringExtra("request_id") ?: ""

        textStatus.text = """
            Your request has been submitted successfully.

            Admin will review your application.
            Once approved, you can access your dashboard.
        """.trimIndent()

        // start polling once
        handler.post(pollRunnable)
    }

    private fun checkApprovalStatus() {
        val url = "http://192.168.222.116/smart_healthcare_app/check_request_status.php"

        val req = object : StringRequest(
            Method.POST, url,
            { resp ->
                try {
                    val json = JSONObject(resp)
                    if (json.optBoolean("success")) {
                        val status = json.optString("status")
                        if (status.equals("Approved", true)) {
                            // 🛑 stop polling
                            handler.removeCallbacks(pollRunnable)

                            // → go to Approved screen
                            val approvedBy   = json.optString("approved_by", "Admin")
                            val approvalDate = json.optString("approval_date", "")
                            val referenceId  = json.optString("reference_id", "")
                            startActivity(Intent(this, RequestApprovedActivity::class.java).apply {
                                putExtra("approved_by",   approvedBy)
                                putExtra("approval_date", approvalDate)
                                putExtra("reference_id",  referenceId)
                            })
                            finish()
                        }
                    } else {
                        // optional: show pending / error
                        Toast.makeText(this, json.optString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Parse error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { err ->
                val msg = err.networkResponse?.data?.let { String(it) } ?: err.message
                Toast.makeText(this, "Network error: $msg", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams() = hashMapOf("request_id" to requestId)
        }

        Volley.newRequestQueue(this).add(req)
    }

    override fun onDestroy() {
        super.onDestroy()
        // clean up if user backs out
        handler.removeCallbacksAndMessages(null)
    }
}


