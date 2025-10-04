package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {

    private val signupUrl = "http://192.168.24.116/smart_healthcare_app/signup.php"
    // Your server URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {
            startActivity(Intent(this, PatientLoginActivity::class.java))
            finish()
        }

        val role = intent.getStringExtra("ROLE") ?: "patient" // default role

        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!cbTerms.isChecked) {
                Toast.makeText(this, "You must agree to the Terms", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare JSON body
            val jsonBody = JSONObject()
            jsonBody.put("name", name)
            jsonBody.put("email", email)
            jsonBody.put("password", password)
            jsonBody.put("role", role)

            // Create request queue
            val queue = Volley.newRequestQueue(this)

            // Create JSON request
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                signupUrl,
                jsonBody,
                { response ->
                    // Handle response
                    if (response.optBoolean("success", false)) {
                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, PatientLoginActivity::class.java))
                        finish()
                    } else {
                        val message = response.optString("message", "Signup failed")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    error.printStackTrace()
                    val statusCode = error.networkResponse?.statusCode ?: "No status"
                    val responseData = error.networkResponse?.data?.let { String(it) } ?: "No response"

                    Toast.makeText(this, "Error! Status: $statusCode\nResponse: $responseData", Toast.LENGTH_LONG).show()
                }

            )

            // Add request to queue
            queue.add(jsonObjectRequest)
        }
    }
}
