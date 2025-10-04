package com.saveetha.smarthealthcareapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import kotlin.jvm.java

class AdminDashboardActivity : AppCompatActivity(), AdminDashboardAdapter.OnActionClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminDashboardAdapter
    private val requestList = mutableListOf<AdminRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = AdminDashboardAdapter(requestList, this) // 'this' implements OnActionClickListener
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
// Add this below recyclerView.adapter = adapter
        //val navHome = findViewById<LinearLayout>(R.id.navHome)
     //   val navHospitals = findViewById<LinearLayout>(R.id.navHospitals)
        val navUsers = findViewById<LinearLayout>(R.id.navUsers)
        val navLogout = findViewById<LinearLayout>(R.id.navLogout)




//        navHospitals.setOnClickListener {
//            startActivity(Intent(this, HospitalsActivity::class.java))
//        }

        navUsers.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
        }

        navLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            // Add logout logic here
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, PatientLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        loadAdminDashboard()
    }

    private fun loadAdminDashboard() {
        val url = "http://192.168.24.116/smart_healthcare_app/admin_dashboard.php"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val success = response.optBoolean("success")
                if (success) {
                    val array: JSONArray = response.optJSONArray("data") ?: JSONArray()
                    requestList.clear()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val req = AdminRequest(
                            id = obj.optString("id"),
                            hospitalName = obj.optString("hospital_name"),
                            registrationNumber = obj.optString("registration_number"),
                            hospitalAddress = obj.optString("hospital_address"),
                            contactNumber = obj.optString("contact_number"),
                            adminName = obj.optString("admin_name"),
                            adminEmail = obj.optString("admin_email"),
                            adminPosition = obj.optString("admin_position"),
                            department = obj.optString("department"),
                            expectedUsers = obj.optString("expected_users"),
                            briefDescription = obj.optString("brief_description"),
                            licensePath = obj.optString("license_path"),
                            registrationCertificatePath = obj.optString("registration_certificate_path"),
                            additionalDocumentsPath = obj.optString("additional_documents_path"),
                            agreedTerms = obj.optString("agreed_terms"),
                            submittedAt = obj.optString("submitted_at")
                        )
                        requestList.add(req)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    // Function to update status in backend
    private fun updateRequestStatus(requestId: String, status: String, actionBy: String) {
        val url = "http://192.168.24.116/smart_healthcare_app/update_request_status.php"

        val params = HashMap<String, String>()
        params["request_id"] = requestId
        params["status"] = status
        params["action_by"] = actionBy

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        Toast.makeText(this, "Status updated to $status", Toast.LENGTH_SHORT).show()
                        loadAdminDashboard() // Refresh list to see updates
                    } else {
                        Toast.makeText(this, "Error: ${jsonResponse.optString("error")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Parsing error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> = params
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    override fun onApprove(request: AdminRequest) {
        // Replace "swaroop" with actual logged-in username if available
        updateRequestStatus(request.id, "Approved", "swaroop")
    }

//    override fun onReject(request: AdminRequest) {
//        updateRequestStatus(request.id, "Rejected", "swaroop")
//    }
override fun onReject(request: AdminRequest) {
    val intent = Intent(this, RejectHospitalAccessActivity::class.java)
    intent.putExtra("request_id", request.id)
    startActivity(intent)
}


    override fun onViewDetails(request: AdminRequest) {
        val intent = Intent(this, ReviewHospitalAccessActivity::class.java)
        intent.putExtra("request_data", request) // use "request_data" as the key
        startActivity(intent)
    }




}
