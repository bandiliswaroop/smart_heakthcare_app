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


class EditPatientProfileActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtDob: EditText
    private lateinit var edtGender: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtState: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnUpdate: Button
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient_profile)

        userId = intent.getStringExtra("user_id")
        Log.d("EditProfile", "Received user_id: $userId")
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        edtName = findViewById(R.id.editName)
        edtEmail = findViewById(R.id.editEmail)
        edtDob = findViewById(R.id.editDob)
        edtGender = findViewById(R.id.editGender)
        edtPhone = findViewById(R.id.editMobile)
        edtState = findViewById(R.id.editState)
        edtAddress = findViewById(R.id.editAddress)
        btnUpdate = findViewById(R.id.btnUpdate)

        fetchCurrentProfile() // optional

        btnUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun fetchCurrentProfile() {
        val url = "http://192.168.24.116/smart_healthcare_app/get_patient_personal.php?user_id=$userId"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@EditPatientProfileActivity, "Network error", Toast.LENGTH_SHORT).show()
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
                            edtName.setText(data.getString("fullname"))
                            edtEmail.setText(data.getString("email"))
                            edtDob.setText(data.getString("dob"))
                            edtGender.setText(data.getString("gender"))
                            edtPhone.setText(data.getString("phone"))
                            edtState.setText(data.getString("state"))
                            edtAddress.setText(data.getString("address"))
                        } else {
                            Toast.makeText(this@EditPatientProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@EditPatientProfileActivity, "Data parsing error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun updateProfile() {
        val name = edtName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val dob = edtDob.text.toString().trim()
        val gender = edtGender.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val state = edtState.text.toString().trim()
        val address = edtAddress.text.toString().trim()

        // Validate input
        if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || gender.isEmpty() ||
            phone.isEmpty() || state.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.24.116/smart_healthcare_app/update_patient_personal_info.php" // Replace with actual endpoint

        val params = HashMap<String, String>()
        params["user_id"] = userId ?: ""
        params["fullname"] = name
        params["email"] = email
        params["dob"] = dob
        params["gender"] = gender
        params["phone"] = phone
        params["state"] = state
        params["address"] = address

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
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
