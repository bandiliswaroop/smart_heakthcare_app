//package com.saveetha.smarthealthcareapp
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class RegisterActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register) // Your register layout
//
//        val role = intent.getStringExtra("ROLE")
//        Toast.makeText(this, "Selected Role: $role", Toast.LENGTH_SHORT).show()
//
//        // You can use this role to customize your registration form
//    }
//}
package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        role = intent.getStringExtra("ROLE")

        val tvLogin = findViewById<TextView>(R.id.tv_login)

        tvLogin.setOnClickListener {
            startActivity(Intent(this, PatientLoginActivity::class.java))
            finish()
        }

//        val btnSignUpEmail = findViewById<Button>(R.id.btn_sign_up_email)
//        btnSignUpEmail.setOnClickListener {
//            val intent = Intent(this, SignupActivity::class.java)
//            intent.putExtra("ROLE", role)
//            startActivity(intent)
        val btnSignUpEmail = findViewById<Button>(R.id.btn_sign_up_email)
        btnSignUpEmail.setOnClickListener {
            val intent = when (role) {
                "doctor" -> Intent(this, DoctorSignupActivity::class.java)
                "patient" -> Intent(this, SignupActivity::class.java)
                "hospital" -> Intent(this, HospitalSignupActivity::class.java)
                else -> Intent(this, SignupActivity::class.java) // fallback
            }
            intent.putExtra("ROLE", role)
            startActivity(intent)
        }

    }

    }
