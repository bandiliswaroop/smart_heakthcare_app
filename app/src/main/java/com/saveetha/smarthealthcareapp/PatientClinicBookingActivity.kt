package com.saveetha.smarthealthcareapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.adapter.DoctorSlot
import com.saveetha.smarthealthcareapp.adapter.TimeSlotAdapter
import com.saveetha.smarthealthcareapp.model.ClinicAppointmentResponse
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PatientClinicBookingActivity : AppCompatActivity() {

    private lateinit var textDoctorName: TextView
    private lateinit var textClinicAddress: TextView
    private lateinit var textPurpose: TextView
    private lateinit var dateTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonBook: Button
    private lateinit var buttonBack: Button
    private lateinit var editTextPatientName: EditText
    private lateinit var editTextPatientPhone: EditText

    private lateinit var selectedDate: String
    private var selectedSlot: DoctorSlot? = null

    private val slots = listOf(
        DoctorSlot("11:00 AM - 12:00 PM", 6),
        DoctorSlot("12:00 PM - 01:00 PM", 6),
        DoctorSlot("02:00 PM - 03:00 PM", 6),
        DoctorSlot("03:00 PM - 04:00 PM", 6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_clinic_booking)

        // Get data from intent
        val doctorName = intent.getStringExtra("doctor_name") ?: ""
        val clinicAddress = intent.getStringExtra("clinic_address") ?: ""

        // Bind views
        textDoctorName = findViewById(R.id.textDoctorName)
        textClinicAddress = findViewById(R.id.textClinicAddress)
        textPurpose = findViewById(R.id.textPurpose)
        dateTextView = findViewById(R.id.dateTextView)
        recyclerView = findViewById(R.id.recyclerViewSlots)
        buttonBook = findViewById(R.id.buttonBookNow)
        buttonBack = findViewById(R.id.buttonBack)
        editTextPatientName = findViewById(R.id.editTextPatientName)
        editTextPatientPhone = findViewById(R.id.editTextPatientPhone)

        // Set values
        textDoctorName.text = doctorName
        textClinicAddress.text = "Clinic Address: $clinicAddress"
        textPurpose.text = "Consultation"

        // Default date format (YYYY-MM-DD)
        val calendar = Calendar.getInstance()
        selectedDate = String.format(
            "%04d-%02d-%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateTextView.text = selectedDate

        // Date picker
        dateTextView.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    dateTextView.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        // RecyclerView setup
        val adapter = TimeSlotAdapter(slots) { selected ->
            selectedSlot = selected
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Book button click
        buttonBook.setOnClickListener {
            val patientName = editTextPatientName.text.toString().trim()
            val patientPhone = editTextPatientPhone.text.toString().trim()

            if (selectedSlot != null && patientName.isNotEmpty() && patientPhone.isNotEmpty()) {
                val appointmentRequest = ClinicAppointmentRequest(
                    doctor_name = doctorName,
                    clinic_address = clinicAddress,
                    date = selectedDate,
                    time = selectedSlot!!.time,
                    purpose = "Consultation",
                    patient_name = patientName,
                    patient_phone = patientPhone,
                    status = "pending"
                )

                // Debug: Log JSON
                Log.d("BOOKING_REQUEST", appointmentRequest.toString())

                ApiClient.instance.bookAppointment(appointmentRequest)
                    .enqueue(object : Callback<ClinicAppointmentResponse> {
                        override fun onResponse(
                            call: Call<ClinicAppointmentResponse>,
                            response: Response<ClinicAppointmentResponse>
                        ) {
                            if (response.isSuccessful && response.body()?.success == true) {
                                val intent = Intent(this@PatientClinicBookingActivity, SuccessActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@PatientClinicBookingActivity,
                                    "Booking failed: ${response.body()?.message ?: "Unknown error"}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
                            Toast.makeText(this@PatientClinicBookingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Please fill all fields and select a time slot", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }
}
