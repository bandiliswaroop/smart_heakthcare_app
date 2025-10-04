//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.*
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.android.volley.Request
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
//import org.json.JSONObject
//
//class PatientLoginActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_patient_login)
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // Sign Up TextView click
//        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)
//        tvSignUp.setOnClickListener {
//            val intent = Intent(this, RoleSelectionActivity::class.java)
//            startActivity(intent)
//        }
//
//        // 🔽 Login Logic Starts Here 🔽
//        val etEmail = findViewById<EditText>(R.id.etEmail)
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val btnLogin = findViewById<Button>(R.id.btnLogin)
//
//        btnLogin.setOnClickListener {
//            val email = etEmail.text.toString().trim()
//            val password = etPassword.text.toString().trim()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // ✅ Check for admin login
//            if (email == "bandiliswaroop21@gmail.com" && password == "swaroop212004") {
//                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, AdminDashboardActivity::class.java)
//                startActivity(intent)
//                finish()
//                return@setOnClickListener
//            }
//
//            val loginUrl = "http://192.168.24.116/smart_healthcare_app/login.php"
//            val jsonBody = JSONObject().apply {
//                put("email", email)
//                put("password", password)
//            }
//
//            val request = JsonObjectRequest(
//                Request.Method.POST, loginUrl, jsonBody,
//                { response ->
//                    if (response.optBoolean("success")) {
//                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
//
//                        if (response.has("user")) {
//                            val user = response.getJSONObject("user")
//                            val id = user.optString("id", "")
//                            val role = user.optString("role", "")
//                            val email = user.optString("email", "")
//                            val personalInfoComplete = user.optInt("personal_info_complete", 0)
//                            val isApproved = user.optInt("is_approved", 0)
//                            val requestId = user.optString("request_id", "")
//
//                            val intent = when {
//                                role == "hospital" && personalInfoComplete == 0 -> Intent(this, HospitalPersonalDetailsActivity::class.java)
//
//                                role == "hospital" && personalInfoComplete == 1 && isApproved == 0 -> Intent(this, HospitalHomeActivity::class.java).apply {
//                                    putExtra("request_id", requestId)
//                                }
//
//                                role == "hospital" && personalInfoComplete == 1 && isApproved == 1 -> Intent(this, HospitalHomeActivity::class.java)
//
//                                role == "patient" && personalInfoComplete == 0 -> Intent(this, PatientPersonalDetailsActivity::class.java)
//                                role == "patient" -> Intent(this, PatientHomeActivity::class.java)
//
//                                role == "doctor" && personalInfoComplete == 0 -> Intent(this, DoctorPersonalDetailsActivity::class.java)
//                                role == "doctor" -> Intent(this, DoctorAppointmentsActivity::class.java)
//
//                                else -> Intent(this, PatientHomeActivity::class.java)
//                            }
//
//                            intent.putExtra("user_id", id)
//                            intent.putExtra("user_email", email) // ✅ Passing email here
//                            startActivity(intent)
//                            finish()
//
//                        } else {
//                            Toast.makeText(this, "User data missing in response", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(this, response.optString("message", "Login failed"), Toast.LENGTH_SHORT).show()
//                    }
//                },
//                { error ->
//                    Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
//                }
//            )
//
//            Volley.newRequestQueue(this).add(request)
//        }
//        // 🔼 Login Logic Ends Here 🔼
//    }
//}



package com.saveetha.smarthealthcareapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PatientLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_patient_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🚀 Check for existing session on app start
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val userRole = sharedPreferences.getString("user_role", null)
        val userId = sharedPreferences.getString("user_id", null)
        val userEmail = sharedPreferences.getString("user_email", null)

        if (isLoggedIn && userRole != null && userId != null && userEmail != null) {
            // User is already logged in, redirect to their respective home activity
            val intent = when (userRole) {
                "admin" -> Intent(this, AdminDashboardActivity::class.java)
                "hospital" -> Intent(this, HospitalHomeActivity::class.java)
                "patient" -> Intent(this, PatientHomeActivity::class.java)
                "doctor" -> Intent(this, DoctorAppointmentsActivity::class.java)
                else -> Intent(this, PatientHomeActivity::class.java) // Fallback
            }
            intent.putExtra("user_id", userId)
            intent.putExtra("user_email", userEmail)
            startActivity(intent)
            finish() // Prevents returning to this activity
            return // Stop further execution of onCreate
        }

        // Sign Up TextView click
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)
        tvSignUp.setOnClickListener {
            val intent = Intent(this, RoleSelectionActivity::class.java)
            startActivity(intent)
        }

        // 🔽 Login Logic Starts Here 🔽
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Check for admin login
            if (email == "bandiliswaroop21@gmail.com" && password == "swaroop212004") {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                // Store admin session
                with(sharedPreferences.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("user_role", "admin")
                    putString("user_id", "admin_user") // A static ID for admin
                    putString("user_email", email)
                    apply()
                }
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            val loginUrl = "http://192.168.24.116/smart_healthcare_app/login.php"
            val jsonBody = JSONObject().apply {
                put("email", email)
                put("password", password)
            }

            val request = JsonObjectRequest(
                Request.Method.POST, loginUrl, jsonBody,
                { response ->
                    if (response.optBoolean("success")) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        if (response.has("user")) {
                            val user = response.getJSONObject("user")
                            val id = user.optString("id", "")
                            val role = user.optString("role", "")
                            val userEmailFromResponse = user.optString("email", "")
                            val personalInfoComplete = user.optInt("personal_info_complete", 0)
                            val isApproved = user.optInt("is_approved", 0)
                            val requestId = user.optString("request_id", "")

                            // ✅ Store user session data in SharedPreferences
                            with(sharedPreferences.edit()) {
                                putBoolean("is_logged_in", true)
                                putString("user_id", id)
                                putString("user_email", userEmailFromResponse)
                                putString("user_role", role)
                                apply()
                            }

//                            val intent = when {
//                                role == "hospital" && personalInfoComplete == 0 -> Intent(this, HospitalPersonalDetailsActivity::class.java)
//
//                                role == "hospital" && personalInfoComplete == 1 && isApproved == 0 -> Intent(this, HospitalHomeActivity::class.java).apply {
//                                    putExtra("request_id", requestId)
//                                }
                            val intent = when {
                                // 🚀 Corrected logic for a new hospital login after server response
                                role == "hospital" -> {
                                    if (personalInfoComplete == 1 && isApproved == 1) {
                                        Intent(this, HospitalHomeActivity::class.java)
                                    } else if (personalInfoComplete == 1 && isApproved == 0) {
                                        Intent(this, RequestPendingActivity::class.java).apply {
                                            putExtra("request_id", requestId)
                                        }
                                    } else {
                                        Intent(this, HospitalPersonalDetailsActivity::class.java)
                                    }
                                }

                                role == "hospital" && personalInfoComplete == 1 && isApproved == 1 -> Intent(this, HospitalHomeActivity::class.java)

                                role == "patient" && personalInfoComplete == 0 -> Intent(this, PatientPersonalDetailsActivity::class.java)
                                role == "patient" -> Intent(this, PatientHomeActivity::class.java)

                                role == "doctor" && personalInfoComplete == 0 -> Intent(this, DoctorPersonalDetailsActivity::class.java)
                                role == "doctor" -> Intent(this, DoctorAppointmentsActivity::class.java)

                                else -> Intent(this, PatientHomeActivity::class.java)
                            }

                            intent.putExtra("user_id", id)
                            intent.putExtra("user_email", userEmailFromResponse)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(this, "User data missing in response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, response.optString("message", "Login failed"), Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            Volley.newRequestQueue(this).add(request)
        }
        // 🔼 Login Logic Ends Here 🔼
    }
}
