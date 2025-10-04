//
//package com.saveetha.smarthealthcareapp
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button // Import Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.adapter.ClinicAppointmentAdapter
//import com.saveetha.smarthealthcareapp.model.ClinicAppointment
//import com.saveetha.smarthealthcareapp.model.ClinicAppointmentResponse
//import com.saveetha.smarthealthcareapp.models.DoctorNameRequest
//import com.saveetha.smarthealthcareapp.models.UpdateAppointmentStatusRequest
//import com.saveetha.smarthealthcareapp.network.ApiClient
//import okhttp3.ResponseBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//
//class DoctorAppointmentsActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var imgProfile: ImageView
//    private lateinit var btnAppointmentHistory: Button // Declare the new button
//    private var userId: String = ""
//    private var doctorEmail: String = ""
//    private val PROFILE_UPDATE_REQUEST_CODE = 1001
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_doctor_appointments)
//        // Initialize views
//        recyclerView = findViewById(R.id.recyclerViewAppointments)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        imgProfile = findViewById(R.id.imgProfile)
//        btnAppointmentHistory = findViewById(R.id.btnAppointmentHistory) // Initialize the new button
//
//        // Get user ID and email from intent
//        userId = intent.getStringExtra("user_id") ?: ""
//        doctorEmail = intent.getStringExtra("user_email")?.trim() ?: ""
//
//        // Load profile image
//        loadProfileImage()
//
//        // Click to edit profile
//        imgProfile.setOnClickListener {
//            val intent = Intent(this, DoctorProfileActivity::class.java).apply {
//                putExtra("user_id", userId)
//                putExtra("user_email", doctorEmail)
//            }
//            startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE)
//        }
//
//        // Add click listener for appointment history
//        btnAppointmentHistory.setOnClickListener {
//            val intent = Intent(this, DoctorAppointmentHistoryActivity::class.java).apply {
//                putExtra("user_id", userId)
//                putExtra("user_email", doctorEmail)
//            }
//            startActivity(intent)
//        }
//
//        // Validate and fetch appointments
//        if (doctorEmail.isEmpty()) {
//            Toast.makeText(this, "Doctor email not found!", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        fetchAppointments()
//    }
//
//    private fun fetchAppointments() {
//        val request = DoctorNameRequest(doctor_name = doctorEmail)
//        val call = ApiClient.instance.getAppointmentsForDoctor(request)
//
//        call.enqueue(object : Callback<ClinicAppointmentResponse> {
//            override fun onResponse(
//                call: Call<ClinicAppointmentResponse>,
//                response: Response<ClinicAppointmentResponse>
//            ) {
//                val body = response.body()
//                Log.d("API_RESPONSE", "Body: $body")
//
//                if (response.isSuccessful && body != null && body.success) {
//                    val allAppointments = body.appointments ?: emptyList()
//                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    val today = Calendar.getInstance().time
//
//                    // Filter out appointments older than one week
//                    val filteredAppointments = allAppointments.filter { appointment ->
//                        try {
//                            val appointmentDate = dateFormat.parse(appointment.date)
//                            val calendar = Calendar.getInstance().apply {
//                                time = appointmentDate
//                                add(Calendar.DAY_OF_YEAR, 7)
//                            }
//                            !calendar.time.before(today)
//                        } catch (e: Exception) {
//                            Log.e("Date Parsing", "Error parsing date: ${appointment.date}", e)
//                            true // Keep the appointment if date parsing fails
//                        }
//                    }
//                    setupRecyclerView(filteredAppointments)
//                } else {
//                    val message = body?.message ?: "No appointments found"
//                    Toast.makeText(this@DoctorAppointmentsActivity, message, Toast.LENGTH_SHORT).show()
//                    setupRecyclerView(emptyList())
//                }
//            }
//
//            override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
//                Toast.makeText(this@DoctorAppointmentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                Log.e("API_ERROR", "Failed: ${t.message}", t)
//            }
//        })
//    }
//
//    // ... rest of the code for setupRecyclerView, updateAppointmentStatus, loadProfileImage, and onActivityResult
//    private fun setupRecyclerView(appointments: List<ClinicAppointment>) {
//        recyclerView.adapter = ClinicAppointmentAdapter(appointments) { appointmentId, newStatus ->
//            updateAppointmentStatus(appointmentId, newStatus)
//        }
//    }
//
//    private fun updateAppointmentStatus(appointment: ClinicAppointment, newStatus: String) {
//        val backendStatus = if (newStatus == "approved") "confirmed" else newStatus
//
//        val request = UpdateAppointmentStatusRequest(
//            doctor_name = appointment.doctor_name ?: "",
//            patient_name = appointment.patient_name ?: "",
//            date = appointment.date ?: "",
//            slot_time = appointment.slot_time ?: "",
//            status = backendStatus
//        )
//
//        val call = ApiClient.instance.updateAppointmentStatus(request)
//        call.enqueue(object : Callback<ClinicAppointmentResponse> {
//            override fun onResponse(call: Call<ClinicAppointmentResponse>, response: Response<ClinicAppointmentResponse>) {
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@DoctorAppointmentsActivity, "Status updated to $backendStatus", Toast.LENGTH_SHORT).show()
//                    fetchAppointments()
//                } else {
//                    Toast.makeText(this@DoctorAppointmentsActivity, "Failed to update status", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
//                Toast.makeText(this@DoctorAppointmentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun loadProfileImage() {
//        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        val savedImageUrl = sharedPref.getString("profile_image_doctor_$userId", null)
//
//        val imageUrl = savedImageUrl
//            ?: "http://192.168.24.116/smart_healthcare_app/doctor_profiles/profile_$userId.jpg"
//
//        Glide.with(this)
//            .load("$imageUrl?t=${System.currentTimeMillis()}")
//            .placeholder(R.drawable.ic_profile)
//            .error(R.drawable.ic_profile)
//            .circleCrop()
//            .into(imgProfile)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PROFILE_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            loadProfileImage()
//        }
//    }
//}
//


package com.saveetha.smarthealthcareapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.adapter.ClinicAppointmentAdapter
import com.saveetha.smarthealthcareapp.model.ClinicAppointment
import com.saveetha.smarthealthcareapp.model.ClinicAppointmentResponse
import com.saveetha.smarthealthcareapp.models.DoctorNameRequest
import com.saveetha.smarthealthcareapp.models.UpdateAppointmentStatusRequest
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DoctorAppointmentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imgProfile: ImageView
    private lateinit var btnAppointmentHistory: Button
    private lateinit var adapter: ClinicAppointmentAdapter
    private var userId: String = ""
    private var doctorEmail: String = ""
    private val PROFILE_UPDATE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointments)

        recyclerView = findViewById(R.id.recyclerViewAppointments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        imgProfile = findViewById(R.id.imgProfile)
        btnAppointmentHistory = findViewById(R.id.btnAppointmentHistory)

        userId = intent.getStringExtra("user_id") ?: ""
        doctorEmail = intent.getStringExtra("user_email")?.trim() ?: ""

        loadProfileImage()

        imgProfile.setOnClickListener {
            val intent = Intent(this, DoctorProfileActivity::class.java).apply {
                putExtra("user_id", userId)
                putExtra("user_email", doctorEmail)
            }
            startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE)
        }

        btnAppointmentHistory.setOnClickListener {
            val intent = Intent(this, DoctorAppointmentHistoryActivity::class.java).apply {
                putExtra("user_id", userId)
                putExtra("user_email", doctorEmail)
            }
            startActivity(intent)
        }

        if (doctorEmail.isEmpty()) {
            Toast.makeText(this, "Doctor email not found!", Toast.LENGTH_SHORT).show()
            return
        }

        fetchAppointments()
    }

    private fun fetchAppointments() {
        val request = DoctorNameRequest(doctor_name = doctorEmail)
        ApiClient.instance.getAppointmentsForDoctor(request).enqueue(object : Callback<ClinicAppointmentResponse> {
            override fun onResponse(call: Call<ClinicAppointmentResponse>, response: Response<ClinicAppointmentResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null && body.success) {
                    val allAppointments = body.appointments ?: emptyList()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = Calendar.getInstance().time

                    val filteredAppointments = allAppointments.filter { appointment ->
                        try {
                            val appointmentDate = dateFormat.parse(appointment.date)
                            val calendar = Calendar.getInstance().apply {
                                time = appointmentDate
                                add(Calendar.DAY_OF_YEAR, 7)
                            }
                            !calendar.time.before(today)
                        } catch (e: Exception) {
                            true
                        }
                    }

                    // Adapter expects a mutable list
                    adapter = ClinicAppointmentAdapter(filteredAppointments.toMutableList()) { appointment, newStatus ->
                        updateAppointmentStatus(appointment, newStatus)
                    }
                    recyclerView.adapter = adapter

                } else {
                    Toast.makeText(this@DoctorAppointmentsActivity, body?.message ?: "No appointments found", Toast.LENGTH_SHORT).show()
                    adapter = ClinicAppointmentAdapter(mutableListOf()) { _, _ -> }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
                Toast.makeText(this@DoctorAppointmentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateAppointmentStatus(appointment: ClinicAppointment, newStatus: String) {
        val backendStatus = if (newStatus == "approved") "confirmed" else newStatus

        val request = UpdateAppointmentStatusRequest(
            doctor_name = appointment.doctor_name,
            patient_name = appointment.patient_name,
            date = appointment.date,
            slot_time = appointment.slot_time,
            status = backendStatus
        )

        ApiClient.instance.updateAppointmentStatus(request).enqueue(object : Callback<ClinicAppointmentResponse> {
//            override fun onResponse(call: Call<ClinicAppointmentResponse>, response: Response<ClinicAppointmentResponse>) {
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@DoctorAppointmentsActivity, "Status updated to $backendStatus", Toast.LENGTH_SHORT).show()
//
//                    // Update UI immediately
//                    adapter.updateAppointmentStatusLocally(appointment, backendStatus)
//                } else {
//                    Toast.makeText(this@DoctorAppointmentsActivity, "Failed to update status", Toast.LENGTH_SHORT).show()
//                }
//            }
override fun onResponse(call: Call<ClinicAppointmentResponse>, response: Response<ClinicAppointmentResponse>) {
    if (response.isSuccessful && response.body()?.success == true) {
        Toast.makeText(this@DoctorAppointmentsActivity, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()

        // Update UI immediately - use "approved" for display, not "confirmed"
        adapter.updateAppointmentStatusLocally(appointment, "approved")
    } else {
        Toast.makeText(this@DoctorAppointmentsActivity, "Failed to update status", Toast.LENGTH_SHORT).show()
    }
}

            override fun onFailure(call: Call<ClinicAppointmentResponse>, t: Throwable) {
                Toast.makeText(this@DoctorAppointmentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProfileImage() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedImageUrl = sharedPref.getString("profile_image_doctor_$userId", null)

        val imageUrl = savedImageUrl ?: "http://192.168.24.116/smart_healthcare_app/doctor_profiles/profile_$userId.jpg"

        Glide.with(this)
            .load("$imageUrl?t=${System.currentTimeMillis()}")
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .into(imgProfile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PROFILE_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadProfileImage()
        }
    }
}
