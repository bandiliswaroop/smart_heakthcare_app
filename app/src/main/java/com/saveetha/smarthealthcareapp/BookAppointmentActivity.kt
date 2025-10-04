package com.saveetha.smarthealthcareapp

import ApiResponse
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.smarthealthcareapp.models.BookAppointmentRequest
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etDate: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSubmit: Button

    private var doctorId: Int = -1
    private var hospitalId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        // Initialize views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etDate = findViewById(R.id.etDate)
        etMessage = findViewById(R.id.messageEditText)
        btnSubmit = findViewById(R.id.submitButton)

        // Get IDs from intent
        doctorId = intent.getIntExtra("doctor_id", -1)
        hospitalId = intent.getIntExtra("hospital_id", -1)

        // Open date picker when date field is clicked
        etDate.setOnClickListener {
            showDatePicker()
        }

        // Submit appointment
        btnSubmit.setOnClickListener {
            bookAppointment()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format to MySQL standard: YYYY-MM-DD
                val formattedDate = "${selectedYear}-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
                etDate.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }


    private fun bookAppointment() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val date = etDate.text.toString()
        val message = etMessage.text.toString()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || date.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = BookAppointmentRequest(
            name = name,
            email = email,
            phone = phone,
            date = date,
            message = message,
            doctor = doctorId,
            hospital = hospitalId
        )

        ApiClient.instance.bookAppointment(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val intent = Intent(this@BookAppointmentActivity, SuccessActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@BookAppointmentActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@BookAppointmentActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
