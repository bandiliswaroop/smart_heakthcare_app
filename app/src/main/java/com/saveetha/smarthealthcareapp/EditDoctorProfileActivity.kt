package com.saveetha.smarthealthcareapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class EditDoctorProfileActivity : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edDob: EditText
    private lateinit var edGender: EditText
    private lateinit var edPhone: EditText
    private lateinit var edHospital: EditText
    private lateinit var edSpecialization: EditText
    private lateinit var edAddress: EditText
    private lateinit var btnUpdate: Button
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor_profile)

        userId = intent.getStringExtra("user_id")
        Log.d("EditDoctorProfile", "Received user_id: $userId")
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        edDob = findViewById(R.id.edDob)
        edGender = findViewById(R.id.edGender)
        edPhone = findViewById(R.id.edPhone)
        edHospital = findViewById(R.id.edHospital)
        edSpecialization = findViewById(R.id.edSpectalization)
        edAddress = findViewById(R.id.edAddress)
        btnUpdate = findViewById(R.id.btnUpdatedoctor)

        fetchCurrentProfile()

        btnUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun fetchCurrentProfile() {
        val url = "http://192.168.24.116/smart_healthcare_app/get_doctor_personal.php?doctor_id=$userId"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@EditDoctorProfileActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                runOnUiThread {
                    try {
                        val obj = JSONObject(json)
                        if (obj.getBoolean("success")) {
                            val data = obj.getJSONObject("data")

                            // Set actual user data instead of hints
                            edName.setText(data.getString("full_name"))
                            edEmail.setText(data.getString("email"))
                            edDob.setText(data.getString("dob"))
                            edGender.setText(data.getString("gender"))
                            edPhone.setText(data.getString("phone"))
                            edHospital.setText(data.getString("hospital_name"))
                            edSpecialization.setText(data.getString("specialization"))
                            edAddress.setText(data.getString("address"))
                        } else {
                            Toast.makeText(this@EditDoctorProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@EditDoctorProfileActivity, "Data parsing error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun updateProfile() {
        val name = edName.text.toString().trim()
        val email = edEmail.text.toString().trim()
        val dob = edDob.text.toString().trim()
        val gender = edGender.text.toString().trim()
        val phone = edPhone.text.toString().trim()
        val hospital = edHospital.text.toString().trim()
        val specialization = edSpecialization.text.toString().trim()
        val address = edAddress.text.toString().trim()

        // Validate input
        if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || gender.isEmpty() ||
            phone.isEmpty() || hospital.isEmpty() || specialization.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.24.116/smart_healthcare_app/update_doctor_info.php"

        val params = HashMap<String, String>()
        params["doctor_id"] = userId ?: ""
        params["full_name"] = name
        params["email"] = email
        params["dob"] = dob
        params["gender"] = gender
        params["phone"] = phone
        params["hospital_name"] = hospital
        params["specialization"] = specialization
        params["address"] = address

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Update failed: ${obj.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Response error", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> = params
        }

        queue.add(request)
    }
}