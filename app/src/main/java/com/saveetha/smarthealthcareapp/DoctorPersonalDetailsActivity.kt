package com.saveetha.smarthealthcareapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

class DoctorPersonalDetailsActivity : AppCompatActivity() {

    private lateinit var fullNameEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var dobEdit: EditText
    private lateinit var phoneEdit: EditText
    private lateinit var specializationEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var continueButton: Button
    private lateinit var hospitalNameEdit: EditText
    private lateinit var maleOption: LinearLayout
    private lateinit var femaleOption: LinearLayout

    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_personal_details)

        // Initialize views
        fullNameEdit = findViewById(R.id.fullNameEdit)
        emailEdit = findViewById(R.id.emailEdit)
        dobEdit = findViewById(R.id.dobEdit)
        phoneEdit = findViewById(R.id.phoneEdit)
        hospitalNameEdit = findViewById(R.id.hospitalNameEdit)
        specializationEdit = findViewById(R.id.specializationEdit)
        addressEdit = findViewById(R.id.addressEdit)
        continueButton = findViewById(R.id.continueButton)
        maleOption = findViewById(R.id.maleOption)
        femaleOption = findViewById(R.id.femaleOption)

        // Date picker
        dobEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val dob = "$day/${month + 1}/$year"
                    dobEdit.setText(dob)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Gender selection
        maleOption.setOnClickListener {
            selectedGender = "Male"
            updateGenderSelection()
        }

        femaleOption.setOnClickListener {
            selectedGender = "Female"
            updateGenderSelection()
        }

        // Continue button
        continueButton.setOnClickListener {
            submitDoctorDetails()
        }
    }

    private fun updateGenderSelection() {
        val selectedBg = ContextCompat.getDrawable(this, R.drawable.gender_selected)
        val unselectedBg = ContextCompat.getDrawable(this, R.drawable.gender_unselected)

        maleOption.background = if (selectedGender == "Male") selectedBg else unselectedBg
        femaleOption.background = if (selectedGender == "Female") selectedBg else unselectedBg
    }

    private fun submitDoctorDetails() {
        val fullName = fullNameEdit.text.toString().trim()
        val email = emailEdit.text.toString().trim()
        val dob = dobEdit.text.toString().trim()
        val phone = phoneEdit.text.toString().trim()
        val hospitalName = hospitalNameEdit.text.toString().trim()
        val specialization = specializationEdit.text.toString().trim()
        val address = addressEdit.text.toString().trim()
        val gender = selectedGender ?: ""

        if (fullName.isEmpty() || email.isEmpty() || dob.isEmpty() || phone.isEmpty()
            || hospitalName.isEmpty() || specialization.isEmpty() || address.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonBody = JSONObject().apply {
            // Do not include user_id anymore
            put("fullname", fullName)
            put("email", email)
            put("dob", dob)
            put("gender", gender)
            put("phone", phone)
            put("hospital_name", hospitalName)
            put("specialization", specialization)
            put("address", address)
        }

        val url = "http://192.168.24.116/smart_healthcare_app/doctor_personal_info.php"
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.POST, url, jsonBody,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "Unknown response")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                if (success) {
                    val doctorId = response.optInt("doctor_id", -1)
                    if (doctorId != -1) {
                        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        prefs.edit().putInt("user_id", doctorId).apply()
                    }

                    startActivity(Intent(this, DoctorHomeActivity::class.java))
                    finish()
                }
            },
            { error ->
                error.printStackTrace()
                val statusCode = error.networkResponse?.statusCode ?: "No status"
                val responseData = error.networkResponse?.data?.let { String(it) } ?: "No response"
                Toast.makeText(this, "Error! Status: $statusCode\nResponse: $responseData", Toast.LENGTH_LONG).show()
            })

        queue.add(request)
    }

}
