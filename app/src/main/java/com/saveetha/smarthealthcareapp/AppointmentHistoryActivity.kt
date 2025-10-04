package com.saveetha.smarthealthcareapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.adapter.AppointmentsAdapter
import com.saveetha.smarthealthcareapp.model.Appointment
import com.saveetha.smarthealthcareapp.models.AppointmentResponse
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class AppointmentHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppointmentsAdapter
    private lateinit var etSearch: EditText
    private val allAppointments = mutableListOf<Appointment>()
    private val displayedAppointments = mutableListOf<Appointment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_history)

        initializeViews()
        setupRecyclerView()
        setupSearch()
        loadAppointments()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewHistory)
        etSearch = findViewById(R.id.etSearch)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AppointmentsAdapter(displayedAppointments)
        recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAppointments(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadAppointments() {
        val userEmail = intent.getStringExtra("user_email") ?: run {
            showToast("No user email found")
            return
        }
        fetchAppointmentHistory(userEmail)
    }

    private fun fetchAppointmentHistory(email: String) {
        ApiClient.instance.getAppointments(email).enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    val appointmentResponse = response.body()
                    if (appointmentResponse != null && appointmentResponse.success) {
                        allAppointments.clear()
                        allAppointments.addAll(appointmentResponse.appointments)
                        filterAppointments("") // Display all appointments initially

                        if (allAppointments.isEmpty()) {
                            showToast("No appointments found")
                        }
                    } else {
                        showToast("Failed to fetch history")
                    }
                } else {
                    showToast("Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                showToast("Network error: ${t.message}")
                Log.e("API_ERROR", "Appointment history fetch failed", t)
            }
        })
    }

    private fun filterAppointments(query: String) {
        val filteredList = if (query.isEmpty()) {
            allAppointments
        } else {
//            val lowerCaseQuery = query.toLowerCase(Locale.getDefault())
//            allAppointments.filter { appointment ->
//                appointment.doctor_name?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
//                        appointment.date?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
//                        appointment.clinic_address?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true
//            }
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            allAppointments.filter { appointment ->
                appointment.doctor_name?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
                        appointment.date?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
                        appointment.clinic_address?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true
            }

        }
        displayedAppointments.clear()
        displayedAppointments.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}