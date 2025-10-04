package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class HospitalSignupActivity : AppCompatActivity() {

    // Replace with your backend URL if it changes
    private val signupUrl = "http://192.168.24.116/smart_healthcare_app/signup.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hospital_signup)

        // Edge‑to‑edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // View references
        val etName     = findViewById<EditText>(R.id.etName)
        val etEmail    = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbTerms    = findViewById<CheckBox>(R.id.cbTerms)
        val btnSignUp  = findViewById<Button>(R.id.btnSignUp)
        val tvLogin    = findViewById<TextView>(R.id.tvLogin)

        // “Already have an account?” → Login screen
        tvLogin.setOnClickListener {
            startActivity(Intent(this, PatientLoginActivity::class.java))
            finish()
        }

        // Sign‑up button logic
        btnSignUp.setOnClickListener {
            val name     = etName.text.toString().trim()
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Basic validation
            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() ->
                    toast("Please fill all fields")
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    toast("Invalid email format")
                password.length < 6 ->
                    toast("Password must be at least 6 characters")
                !cbTerms.isChecked ->
                    toast("You must agree to the Terms")
                else -> {
                    // Assemble JSON payload
                    val body = JSONObject().apply {
                        put("name", name)
                        put("email", email)
                        put("password", password)
                        put("role", "hospital")   // ← hard‑coded role
                    }

                    // Volley request
                    Volley.newRequestQueue(this).add(
                        JsonObjectRequest(
                            Request.Method.POST,
                            signupUrl,
                            body,
                            { res ->
                                if (res.optBoolean("success", false)) {
                                    toast("Signup successful!")
                                    startActivity(Intent(this, PatientLoginActivity::class.java))
                                    finish()
                                } else {
                                    toast(res.optString("message", "Signup failed"))
                                }
                            },
                            { err ->
                                val code = err.networkResponse?.statusCode ?: "No status"
                                val data = err.networkResponse?.data?.let { String(it) } ?: "No response"
                                toast("Error! Status: $code\nResponse: $data", long = true)
                            }
                        )
                    )
                }
            }
        }
    }

    // Helper for Toast messages
    private fun toast(msg: String, long: Boolean = false) =
        Toast.makeText(this, msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}
