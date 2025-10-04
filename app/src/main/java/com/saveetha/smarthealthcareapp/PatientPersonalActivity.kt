package com.saveetha.smarthealthcareapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.smarthealthcareapp.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.Period

class PatientPersonalActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtDob: TextView
    private lateinit var txtAge: TextView
    private lateinit var txtGender: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtState: TextView
    private lateinit var txtAddress: TextView

    private var userId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_personal)
        Log.d("PatientPersonal", "Activity created")

        userId = intent.getStringExtra("user_id")?.toIntOrNull() ?: -1
        Log.d("PatientPersonal", "Received user_id: $userId")

        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtDob = findViewById(R.id.txtDob)
        txtAge = findViewById(R.id.txtAge)
        txtGender = findViewById(R.id.txtGender)
        txtPhone = findViewById(R.id.txtPhone)
        txtState = findViewById(R.id.txtState)
        txtAddress = findViewById(R.id.txtAddress)

        userId = intent.getStringExtra("user_id")?.toIntOrNull() ?: -1
        Log.d("PatientPersonal", "Received user_id: $userId")


        if (userId == -1) {
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnEdit = findViewById<Button>(R.id.btnEditProfile)

//        btnLogout.setOnClickListener {
//            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
//             startActivity(Intent(this, PatientLoginActivity::class.java))
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

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditPatientProfileActivity::class.java)
            intent.putExtra("user_id", userId.toString())
            startActivity(intent)
        }


        fetchProfileData()
    }

    private fun fetchProfileData() {
        val url = "http://192.168.222.116/smart_healthcare_app/get_patient_personal.php?user_id=$userId"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@PatientPersonalActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                runOnUiThread {
                    try {
                        val obj = JSONObject(json)

                        if (obj.getBoolean("success")) {
                            val data = obj.getJSONObject("data")

                            val name = data.getString("fullname")
                            val email = data.getString("email")
                            val dob = data.getString("dob")
                            val gender = data.getString("gender")
                            val phone = data.getString("phone")
                            val state = data.getString("state")
                            val address = data.getString("address")

                            txtName.text = name
                            txtEmail.text = email
                            txtDob.text = dob
                            txtGender.text = gender
                            txtPhone.text = phone
                            txtState.text = state
                            txtAddress.text = address

                            val age = calculateAge(dob)
                            txtAge.text = age
                        } else {
                            Toast.makeText(this@PatientPersonalActivity, "No profile found", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@PatientPersonalActivity, "Data parsing error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun calculateAge(dobString: String): String {
        val dob = LocalDate.parse(dobString)
        val today = LocalDate.now()
        val age = Period.between(dob, today)
        return "${age.years}y, ${age.months}m, ${age.days}d"
    }
}
