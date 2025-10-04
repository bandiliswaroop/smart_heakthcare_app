package com.saveetha.smarthealthcareapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class DoctorPersonalActivity : AppCompatActivity() {

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textDob: TextView
    private lateinit var textHospital: TextView
    private lateinit var textGender: TextView
    private lateinit var textPhone: TextView
    private lateinit var textSpecialization: TextView
    private lateinit var textAddress: TextView

    // Use a String to hold the user ID to prevent conversion issues.
    private var userIdString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_personal)
        Log.d("DoctorPersonal", "Activity created")

        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        textDob = findViewById(R.id.textDob)
        textHospital = findViewById(R.id.textHospital)
        textGender = findViewById(R.id.textGender)
        textPhone = findViewById(R.id.textPhone)
        textSpecialization = findViewById(R.id.textSpecialization)
        textAddress = findViewById(R.id.textAddress)

        // Get the user ID as a String and store it.
        userIdString = intent.getStringExtra("user_id")
        Log.d("PatientPersonal", "Received user_id: $userIdString")

        if (userIdString.isNullOrEmpty()) {
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnEdit = findViewById<Button>(R.id.btnEditDoctorProfile)

//        btnLogout.setOnClickListener {
//            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, PatientLoginActivity::class.java))
//            finish()
//        }
        btnLogout.setOnClickListener {
            // 1. Show the "Logged out!" message
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()

            // 2. Clear the session data from SharedPreferences
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // 3. Navigate to the login screen and clear the back stack
            val intent = Intent(this, PatientLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        // The code to open EditDoctorProfileActivity should be inside this click listener.
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDoctorProfileActivity::class.java)
            // Pass the user ID string directly to the new activity.
            intent.putExtra("user_id", userIdString)
            startActivity(intent)
        }

        fetchProfileData()
    }

    private fun fetchProfileData() {
        // Use the String userId here.
        val url = "http://192.168.24.116/smart_healthcare_app/get_doctor_personal.php?doctor_id=$userIdString"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@DoctorPersonalActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                runOnUiThread {
                    try {
                        val obj = JSONObject(json)

                        if (obj.getBoolean("success")) {
                            val data = obj.getJSONObject("data")

                            val name = data.getString("full_name")
                            val email = data.getString("email")
                            val dob = data.getString("dob")
                            val hospital = data.getString("hospital_name")
                            val gender = data.getString("gender")
                            val phone = data.getString("phone")
                            val specialization = data.getString("specialization")
                            val address = data.getString("address")

                            textName.text = name
                            textEmail.text = email
                            textDob.text = dob
                            textHospital.text = hospital
                            textGender.text = gender
                            textPhone.text = phone
                            textSpecialization.text = specialization
                            textAddress.text = address
                        } else {
                            Toast.makeText(this@DoctorPersonalActivity, "No profile found", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@DoctorPersonalActivity, "Data parsing error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // It is good practice to refresh the data when the activity resumes,
        // in case the profile was updated on the previous screen.
        fetchProfileData()
    }
}