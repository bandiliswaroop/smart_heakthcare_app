package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.adapter.AppointmentsAdapter
import com.saveetha.smarthealthcareapp.model.Appointment
import com.saveetha.smarthealthcareapp.models.AppointmentResponse // ⬅️ Add this line
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.Button
class PatientBookingAppointmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppointmentsAdapter
    private val appointments = mutableListOf<Appointment>()
    private lateinit var btnAppointmentHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_booking_appointment)

        initializeViews()
        setupRecyclerView()
        loadAppointments()
        btnAppointmentHistory.setOnClickListener {
            val userEmail = intent.getStringExtra("user_email")
            val intent = Intent(this, AppointmentHistoryActivity::class.java).apply {
                putExtra("user_email", userEmail)
            }
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewAppointments)
        btnAppointmentHistory = findViewById(R.id.btnAppointmentHistory) // Initialize the button
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AppointmentsAdapter(appointments)
        recyclerView.adapter = adapter
    }

    private fun loadAppointments() {
        val userEmail = intent.getStringExtra("user_email") ?: run {
            showToast("No user email found")
            return
        }
        fetchAppointments(userEmail)
    }

    private fun fetchAppointments(email: String) {
        ApiClient.instance.getAppointments(email).enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    val appointmentResponse = response.body()
                    if (appointmentResponse != null && appointmentResponse.success) {
                        val allAppointments = appointmentResponse.appointments

                        // Create a date formatter for parsing the appointment date
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        // Get today's date
                        val today = Calendar.getInstance().time

                        // Filter appointments
                        val filteredAppointments = allAppointments.filter { appointment ->
                            try {
                                val appointmentDate = dateFormat.parse(appointment.date)
                                val calendar = Calendar.getInstance().apply {
                                    time = appointmentDate
                                    add(Calendar.DAY_OF_YEAR, 7) // Add 7 days to the appointment date
                                }
                                val oneWeekAfter = calendar.time

                                // Keep the appointment if one week after the appointment date is not in the past
                                !oneWeekAfter.before(today)
                            } catch (e: Exception) {
                                Log.e("Date Parsing", "Error parsing date: ${appointment.date}", e)
                                // In case of an error, keep the appointment to be safe
                                true
                            }
                        }

                        // Update UI with the filtered appointments
                        updateAppointmentsList(filteredAppointments)

                    } else {
                        showToast("Failed to fetch appointments")
                    }
                } else {
                    showToast("Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                showToast("Network error: ${t.message}")
                Log.e("API_ERROR", "Appointment fetch failed", t)
            }
        })
    }

    private fun updateAppointmentsList(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        adapter.notifyDataSetChanged()

        if (newAppointments.isEmpty()) {
            showToast("No appointments found")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}










//    private fun fetchAppointments(email: String) {
//        ApiClient.instance.getAppointments(email).enqueue(object : Callback<AppointmentResponse> {
//            override fun onResponse(
//                call: Call<AppointmentResponse>,
//                response: Response<AppointmentResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val appointmentResponse = response.body()
//                    if (appointmentResponse != null && appointmentResponse.success) {
//                        // Update UI with appointments
//                        appointments.clear()
//                        appointments.addAll(appointmentResponse.appointments)
//                        adapter.notifyDataSetChanged()
//
//                        if (appointmentResponse.appointments.isEmpty()) {
//                            showToast("No appointments found")
//                        }
//                    } else {
//                        showToast("Failed to fetch appointments")
//                    }
//                } else {
//                    showToast("Server error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
//                showToast("Network error: ${t.message}")
//                Log.e("API_ERROR", "Appointment fetch failed", t)
//            }
//        })
//    }