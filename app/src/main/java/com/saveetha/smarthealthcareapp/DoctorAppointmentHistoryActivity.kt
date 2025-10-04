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
import com.saveetha.smarthealthcareapp.adapter.ClinicAppointmentAdapter
import com.saveetha.smarthealthcareapp.model.ClinicAppointment
import com.saveetha.smarthealthcareapp.model.ClinicAppointmentResponse
import com.saveetha.smarthealthcareapp.models.DoctorNameRequest
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DoctorAppointmentHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var adapter: ClinicAppointmentAdapter
    private val allAppointments = mutableListOf<ClinicAppointment>()
    private var doctorEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointment_history)

        initializeViews()
        setupRecyclerView()
        setupSearch()

        doctorEmail = intent.getStringExtra("user_email")?.trim() ?: ""

        if (doctorEmail.isEmpty()) {
            Toast.makeText(this, "Doctor email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        fetchAppointmentHistory()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewHistory)
        etSearch = findViewById(R.id.etSearch)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ClinicAppointmentAdapter(allAppointments) { _, _ ->
            Toast.makeText(this, "Cannot update past appointments.", Toast.LENGTH_SHORT).show()
        }
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

    private fun fetchAppointmentHistory() {
        val request = DoctorNameRequest(doctor_name = doctorEmail)
        val call = ApiClient.instance.getAppointmentsForDoctor(request)

        call.enqueue(object : Callback<ClinicAppointmentResponse> {
            override fun onResponse(
                call: Call<ClinicAppointmentResponse>,
                response: Response<ClinicAppointmentResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null && body.success) {
                    allAppointments.clear()
                    allAppointments.addAll(body.appointments ?: emptyList())
                    filterAppointments("") // Display all appointments initially
                } else {
                    val message = body?.message ?: "No appointment history found"
                    Toast.makeText(this@DoctorAppointmentHistoryActivity, message, Toast.LENGTH_SHORT).show()
                    allAppointments.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
                Toast.makeText(this@DoctorAppointmentHistoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Failed to fetch history: ${t.message}", t)
            }
        })
    }

    private fun filterAppointments(query: String) {
        val filteredList = if (query.isEmpty()) {
            allAppointments
        } else {
            val lowerCaseQuery = query.toLowerCase(Locale.getDefault())
            allAppointments.filter { appointment ->
                appointment.patient_name?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
                        appointment.date?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true ||
                        appointment.contact_number?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery) == true
            }
        }
//        adapter = ClinicAppointmentAdapter(filteredList) { _, _ ->
//            Toast.makeText(this, "Cannot update past appointments.", Toast.LENGTH_SHORT).show()
//        }
        adapter = ClinicAppointmentAdapter(filteredList as MutableList<ClinicAppointment>) { _, _ ->
            Toast.makeText(this, "Cannot update past appointments.", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }
}