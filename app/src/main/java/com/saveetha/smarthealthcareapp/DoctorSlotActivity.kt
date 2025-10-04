//package com.saveetha.smarthealthcareapp
//
//import android.app.DatePickerDialog
//import android.os.Bundle
//import android.widget.*
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.saveetha.smarthealthcareapp.adapter.DoctorSlot
//import com.saveetha.smarthealthcareapp.adapter.SlotAdapter
//import com.saveetha.smarthealthcareapp.network.ApiClient
//import com.saveetha.smarthealthcareapp.api.DoctorSlotRequest
//import com.saveetha.smarthealthcareapp.models.GenericResponse
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.Calendar
//
//
//class DoctorSlotActivity : AppCompatActivity() {
//
//    private lateinit var edtDoctorName: EditText
//    private lateinit var edtSpecialization: EditText
//    private lateinit var edtClinicName: EditText
//    private lateinit var edtClinicAddress: EditText
//    private lateinit var edtContactNumber: EditText
//    private lateinit var tvDate: TextView
//    private lateinit var recyclerSlots: RecyclerView
//   // private lateinit var btnAddSlot: ImageButton
//   private lateinit var btnAddSlot: FloatingActionButton
//
//    private lateinit var btnSaveSlots: Button
//    private lateinit var spinnerLanguage: Spinner
//    private lateinit var slotAdapter: SlotAdapter
//    private val slotList = mutableListOf<DoctorSlot>()
//    private var selectedLanguage: String = "English"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_doctor_slot)
//
//        // Bind views
//        edtDoctorName = findViewById(R.id.edtDoctorName)
//        edtSpecialization = findViewById(R.id.edtSpecialization)
//        edtClinicName = findViewById(R.id.edtClinicName)
//        edtClinicAddress = findViewById(R.id.edtClinicAddress)
//        edtContactNumber = findViewById(R.id.edtContactNumber)
//        tvDate = findViewById(R.id.tvDate)
//        recyclerSlots = findViewById(R.id.recyclerSlots)
//        btnAddSlot = findViewById(R.id.btnAddSlot)
//        btnSaveSlots = findViewById(R.id.btnSaveSlots)
//        spinnerLanguage = findViewById(R.id.spinnerLanguage)
//
//        // Setup RecyclerView
//        slotAdapter = SlotAdapter(slotList) { position ->
//            slotList.removeAt(position)
//            slotAdapter.notifyItemRemoved(position)
//        }
//
//        recyclerSlots.layoutManager = LinearLayoutManager(this)
//        recyclerSlots.adapter = slotAdapter
//        // Setup Language Spinner
////        val languages = listOf("English", "हिंदी", "తెలుగు", "தமிழ்", "മലയാളം", "ಕನ್ನಡ")
//        val languages = listOf(
//            "English",
//            "हिंदी",       // Hindi
//            "বাংলা",      // Bengali
//            "తెలుగు",     // Telugu
//            "मराठी",      // Marathi
//            "தமிழ்",      // Tamil
//            "اردو",       // Urdu
//            "ગુજરાતી",    // Gujarati
//            "ಕನ್ನಡ",      // Kannada
//            "മലയാളം",    // Malayalam
//            "ଓଡ଼ିଆ",     // Odia
//            "ਪੰਜਾਬੀ"     // Punjabi
//        )
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerLanguage.adapter = adapter
//
//        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long
//            ) {
//                selectedLanguage = languages[position]
//                Toast.makeText(this@DoctorSlotActivity, "Selected: $selectedLanguage", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//        // Add new time slot
//        btnAddSlot.setOnClickListener {
//            showAddSlotDialog()
//        }
//
//        // Submit data
//        btnSaveSlots.setOnClickListener {
//            submitDoctorSlots()
//        }
//    }
//
//    private fun showAddSlotDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_add_slot, null)
//        val edtSlotTime = dialogView.findViewById<EditText>(R.id.edtSlotTime)
//        val edtRemaining = dialogView.findViewById<EditText>(R.id.edtRemaining)
//
//        AlertDialog.Builder(this)
//            .setTitle("Add Time Slot")
//            .setView(dialogView)
//            .setPositiveButton("Add") { _, _ ->
//                val time = edtSlotTime.text.toString().trim()
//                val remainingStr = edtRemaining.text.toString().trim()
//                val remaining = remainingStr.toIntOrNull() ?: 0
//
//                if (time.isEmpty()) {
//                    Toast.makeText(this, "Slot time cannot be empty", Toast.LENGTH_SHORT).show()
//                    return@setPositiveButton
//                }
//                tvDate.setOnClickListener {
//                    val calendar = Calendar.getInstance()
//                    val year = calendar.get(Calendar.YEAR)
//                    val month = calendar.get(Calendar.MONTH)
//                    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//                    val datePicker = DatePickerDialog(this, { _, y, m, d ->
//                        tvDate.text = String.format("%02d/%02d/%04d", d, m + 1, y)
//                    }, year, month, day)
//
//                    datePicker.show()
//                }
//
//
//                slotList.add(DoctorSlot(time, remaining))
//                slotAdapter.notifyItemInserted(slotList.size - 1)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//
//    private fun submitDoctorSlots() {
//        val doctorName = edtDoctorName.text.toString().trim()
//        val specialization = edtSpecialization.text.toString().trim()
//        val clinicName = edtClinicName.text.toString().trim()
//        val clinicAddress = edtClinicAddress.text.toString().trim()
//        val contactNumber = edtContactNumber.text.toString().trim()
//
//        val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//        val userId = sharedPrefs.getInt("user_id", -1)
//
//        if (userId == -1) {
//            Toast.makeText(this, "User ID not found. Please login again.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (doctorName.isEmpty() || specialization.isEmpty() || clinicName.isEmpty()
//            || clinicAddress.isEmpty() || contactNumber.isEmpty() || slotList.isEmpty()
//        ) {
//            Toast.makeText(this, "Please fill all fields and add at least one slot", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val request = DoctorSlotRequest(
//            user_id = userId,
//            doctor_name = doctorName,
//            specialization = specialization,
//            clinic_name = clinicName,
//            clinic_address = clinicAddress,
//            contact_number = contactNumber,
//            date = tvDate.text.toString(),
//            slots = slotList,
//            language = selectedLanguage,
//        )
//
//        ApiClient.instance.submitDoctorSlots(request).enqueue(object : Callback<GenericResponse> {
//            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@DoctorSlotActivity, "Slots submitted successfully!", Toast.LENGTH_SHORT).show()
//                    finish()
//                } else {
//                    Toast.makeText(this@DoctorSlotActivity, "Failed to submit", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
//                Toast.makeText(this@DoctorSlotActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
package com.saveetha.smarthealthcareapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.adapter.DoctorSlot
import com.saveetha.smarthealthcareapp.adapter.SlotAdapter
import com.saveetha.smarthealthcareapp.network.ApiClient
import com.saveetha.smarthealthcareapp.api.DoctorSlotRequest
import com.saveetha.smarthealthcareapp.models.GenericResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class DoctorSlotActivity : AppCompatActivity() {

    private lateinit var edtDoctorName: EditText
    private lateinit var edtSpecialization: EditText
    private lateinit var edtClinicName: EditText
    private lateinit var edtClinicAddress: EditText
    private lateinit var edtContactNumber: EditText
    private lateinit var tvDate: TextView
    private lateinit var recyclerSlots: RecyclerView
    private lateinit var btnSaveSlots: Button
    private lateinit var spinnerLanguage: Spinner
    private lateinit var slotAdapter: SlotAdapter
    private val slotList = mutableListOf<DoctorSlot>()
    private var selectedLanguage: String = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_slot)

        // Bind views
        edtDoctorName = findViewById(R.id.edtDoctorName)
        edtSpecialization = findViewById(R.id.edtSpecialization)
        edtClinicName = findViewById(R.id.edtClinicName)
        edtClinicAddress = findViewById(R.id.edtClinicAddress)
        edtContactNumber = findViewById(R.id.edtContactNumber)
        tvDate = findViewById(R.id.tvDate)
        recyclerSlots = findViewById(R.id.recyclerSlots)
        btnSaveSlots = findViewById(R.id.btnSaveSlots)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)

        // Setup RecyclerView
        slotAdapter = SlotAdapter(slotList) { position ->
            // Optional: allow removing slots if needed
            slotList.removeAt(position)
            slotAdapter.notifyItemRemoved(position)
        }

        recyclerSlots.layoutManager = LinearLayoutManager(this)
        recyclerSlots.adapter = slotAdapter

        // Setup Language Spinner
        val languages = listOf(
            "English", "Hindi", "Bengali", "Telugu", "Marathi",
            "Tamil", "Urdu", "Gujarati", "Kannada", "Malayalam",
            "Odia", "Punjabi"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long
            ) {
                selectedLanguage = languages[position]
                Toast.makeText(this@DoctorSlotActivity, "Selected: $selectedLanguage", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Date picker
        tvDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                tvDate.text = String.format("%02d/%02d/%04d", d, m + 1, y)
            }, year, month, day)
            datePicker.show()
        }

        // Submit data
        btnSaveSlots.setOnClickListener {
            submitDoctorSlots()
        }
    }

    private fun submitDoctorSlots() {
        val doctorName = edtDoctorName.text.toString().trim()
        val specialization = edtSpecialization.text.toString().trim()
        val clinicName = edtClinicName.text.toString().trim()
        val clinicAddress = edtClinicAddress.text.toString().trim()
        val contactNumber = edtContactNumber.text.toString().trim()

        val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        if (doctorName.isEmpty() || specialization.isEmpty() || clinicName.isEmpty()
            || clinicAddress.isEmpty() || contactNumber.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = DoctorSlotRequest(
            user_id = userId,
            doctor_name = doctorName,
            specialization = specialization,
            clinic_name = clinicName,
            clinic_address = clinicAddress,
            contact_number = contactNumber,
            date = tvDate.text.toString(),
            slots = slotList, // can be empty or pre-populated
            language = selectedLanguage,
        )

        ApiClient.instance.submitDoctorSlots(request).enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@DoctorSlotActivity, "Slots submitted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DoctorSlotActivity, "Failed to submit", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Toast.makeText(this@DoctorSlotActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
