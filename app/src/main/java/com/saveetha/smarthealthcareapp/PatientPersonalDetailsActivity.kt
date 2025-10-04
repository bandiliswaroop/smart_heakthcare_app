package com.saveetha.smarthealthcareapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class PatientPersonalDetailsActivity : AppCompatActivity() {

    private lateinit var fullNameEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var dobEdit: EditText
    private lateinit var phoneEdit: EditText
    private lateinit var stateEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var continueButton: Button

    private lateinit var maleOption: LinearLayout
    private lateinit var femaleOption: LinearLayout

    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_personal_details)

        val userId = intent.getStringExtra("user_id") ?: ""

        // Insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        fullNameEdit = findViewById(R.id.fullNameEdit)
        emailEdit = findViewById(R.id.emailEdit)
        dobEdit = findViewById(R.id.dobEdit)
        phoneEdit = findViewById(R.id.phoneEdit)
        stateEdit = findViewById(R.id.stateEdit)
        addressEdit = findViewById(R.id.addressEdit)
        continueButton = findViewById(R.id.continueButton)

        maleOption = findViewById(R.id.maleOption)
        femaleOption = findViewById(R.id.femaleOption)

        // Date picker dialog
        dobEdit.setOnClickListener {
            showDatePickerDialog()
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

        // Submit button
        continueButton.setOnClickListener {
            submitPersonalDetails()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val dob = "$dayOfMonth/${month + 1}/$year"
                dobEdit.setText(dob)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateGenderSelection() {
        val selectedBg = ContextCompat.getDrawable(this, R.drawable.gender_selected)
        val unselectedBg = ContextCompat.getDrawable(this, R.drawable.gender_unselected)

        maleOption.background = if (selectedGender == "Male") selectedBg else unselectedBg
        femaleOption.background = if (selectedGender == "Female") selectedBg else unselectedBg
    }

    private fun submitPersonalDetails() {
        val fullName = fullNameEdit.text.toString().trim()
        val email = emailEdit.text.toString().trim()
        val dob = dobEdit.text.toString().trim()
        val phone = phoneEdit.text.toString().trim()
        val state = stateEdit.text.toString().trim()
        val address = addressEdit.text.toString().trim()
        val gender = selectedGender ?: ""

        if (fullName.isEmpty() || email.isEmpty() || dob.isEmpty() || phone.isEmpty() ||
            state.isEmpty() || address.isEmpty() || gender.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            return
        }

        // Get user_id passed via Intent
        val userId = intent.getStringExtra("user_id") ?: ""
        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare JSON body
        val jsonBody = JSONObject().apply {
            put("user_id", userId)
            put("fullname", fullName)
            put("email", email)
            put("dob", dob)
            put("gender", gender)
            put("phone", phone)
            put("state", state)
            put("address", address)
        }

        val url = "http://192.168.24.116/smart_healthcare_app/patient_personal_info.php" // Your PHP script URL

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "Unknown response")

                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                if (success) {
                    // ✅ After success, open NotificationPermissionActivity
                    val intent = Intent(this, NotificationPermissionActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: close current activity
                }
            },
            { error ->
                error.printStackTrace()
                val statusCode = error.networkResponse?.statusCode ?: "No status"
                val responseData = error.networkResponse?.data?.let { String(it) } ?: "No response"

                Toast.makeText(this, "Error! Status: $statusCode\nResponse: $responseData", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(jsonObjectRequest)
    }}
