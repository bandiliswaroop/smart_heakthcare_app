package com.saveetha.smarthealthcareapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RejectHospitalAccessActivity : AppCompatActivity() {

    private var requestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reject_hospital_access)

        // Get the request ID from the intent
        requestId = intent.getStringExtra("request_id")

        val rejectButton = findViewById<Button>(R.id.confirmRejectionButton)
        rejectButton.setOnClickListener {
            requestId?.let {
                updateRequestStatus(it, "Rejected", "swaroop")
            }
        }
    }

    private fun updateRequestStatus(requestId: String, status: String, actionBy: String) {
        val url = "http://192.168.24.116/smart_healthcare_app/update_request_status.php"

        val params = HashMap<String, String>()
        params["request_id"] = requestId
        params["status"] = status
        params["action_by"] = actionBy

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        Toast.makeText(this, "Status updated to $status", Toast.LENGTH_SHORT).show()
                        finish() // Go back to the previous activity
                    } else {
                        Toast.makeText(this, "Error: ${jsonResponse.optString("error")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Parsing error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> = params
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }
}
