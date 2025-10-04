////package com.saveetha.smarthealthcareapp
////
////import android.os.Bundle
////import android.widget.TextView
////import androidx.appcompat.app.AppCompatActivity
////
////class HospitalHomeActivity : AppCompatActivity() {
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_hospital_home)
////
////        val userId = intent.getStringExtra("user_id") ?: "Unknown"
////        val welcomeText = findViewById<TextView>(R.id.tvWelcome)
////        welcomeText.text = "Welcome, Hospital Admin! Your ID: $userId"
////    }
////}
//package com.saveetha.smarthealthcareapp
//
//import ApiResponse
//import android.app.Activity
//import android.app.ProgressDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Specialty
//import com.saveetha.smarthealthcareapp.models.Technology
//import com.saveetha.smarthealthcareapp.network.RetrofitClient
//import okhttp3.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import com.saveetha.smarthealthcareapp.utils.FileUtils
//
//
//class HospitalHomeActivity : AppCompatActivity() {
//
//    private lateinit var imgHospital: ImageView
//    private lateinit var btnUploadImage: ImageView
//    private lateinit var etHospitalName: EditText
//    private lateinit var etAbout: EditText
//    private lateinit var etAddress: EditText
//    private lateinit var etContact: EditText
//    private lateinit var cbEmergency: CheckBox
//    private lateinit var btnSave: TextView
//
//    private lateinit var rvSpecialties: RecyclerView
//    private lateinit var specialtiesAdapter: SpecialtyAdapter
//    private val specialties = mutableListOf<Specialty>()
//
//    private lateinit var rvTechnologies: RecyclerView
//    private lateinit var techAdapter: TechnologyAdapter
//    private val technologies = mutableListOf<Technology>()
//
//    private var selectedImageUri: Uri? = null
//    private val PICK_IMAGE_REQUEST = 1001
//    private val PICK_TECH_IMAGE_BASE = 2000
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_home)
//
//        imgHospital = findViewById(R.id.imgHospital)
//        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
//        etHospitalName = findViewById(R.id.etHospitalName)
//        etAbout = findViewById(R.id.etAbout)
//        etAddress = findViewById(R.id.etAddress)
//        etContact = findViewById(R.id.etContact)
//        cbEmergency = findViewById(R.id.cbEmergency)
//        btnSave = findViewById(R.id.btnSave)
//
//        rvSpecialties = findViewById(R.id.rvSpecialties)
//        rvSpecialties.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        specialtiesAdapter = SpecialtyAdapter(specialties)
//        rvSpecialties.adapter = specialtiesAdapter
//
//        rvTechnologies = findViewById(R.id.rvTechnologies)
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        techAdapter = TechnologyAdapter(technologies, this)
//        rvTechnologies.adapter = techAdapter
//
//        btnUploadImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        btnSave.setOnClickListener {
//            if (isFormValid()) {
//                uploadHospitalProfile()
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val selectedUri = data?.data
//
//        if (resultCode == Activity.RESULT_OK && selectedUri != null) {
//            when {
//                requestCode == PICK_IMAGE_REQUEST -> {
//                    selectedImageUri = selectedUri
//                    imgHospital.setImageURI(selectedUri)
//                }
//                requestCode >= PICK_TECH_IMAGE_BASE -> {
//                    val techPosition = requestCode - PICK_TECH_IMAGE_BASE
//                    if (techPosition in technologies.indices) {
//                        technologies[techPosition].imageUri = selectedUri
//                        techAdapter.updateTechnologyImage(techPosition, selectedUri)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun isFormValid(): Boolean {
//        return when {
//            etHospitalName.text.isNullOrEmpty() -> {
//                etHospitalName.error = "Enter hospital name"; false
//            }
//            etAbout.text.isNullOrEmpty() -> {
//                etAbout.error = "Enter about hospital"; false
//            }
//            etAddress.text.isNullOrEmpty() -> {
//                etAddress.error = "Enter address"; false
//            }
//            etContact.text.isNullOrEmpty() -> {
//                etContact.error = "Enter contact number"; false
//            }
//            else -> true
//        }
//    }
//
//    private fun uploadHospitalProfile() {
//        val progressDialog = ProgressDialog(this).apply {
//            setMessage("Uploading...")
//            setCancelable(false)
//            show()
//        }
//
//        val name = etHospitalName.text.toString()
//        val about = etAbout.text.toString()
//        val address = etAddress.text.toString()
//        val contact = etContact.text.toString()
//        val emergency = if (cbEmergency.isChecked) "1" else "0"
//
//        val specialtiesJson = specialties.joinToString(",") { it.name }
//        val technologiesJson = technologies.joinToString(",") { it.name }
//
//        val formFields = mapOf(
//            "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "about" to about.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "specialties" to specialtiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "technologies" to technologiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "facilities" to "ICU:5,OT:3,Beds:50".toRequestBody("text/plain".toMediaTypeOrNull()),
//            "address" to address.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "contact" to contact.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "emergency_services" to emergency.toRequestBody("text/plain".toMediaTypeOrNull())
//        )
//
//        val logoPart = selectedImageUri?.let {
//            val file = File(FileUtils.getPathFromUri(this, it) ?: "")
//
//            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//            MultipartBody.Part.createFormData("logo", file.name, requestFile)
//        }
//
//        val specialtiesImagePart: MultipartBody.Part? = null
//
//        val techImageParts = technologies.mapNotNullIndexed { index, tech ->
//            tech.imageUri?.let {
//                val file = File(FileUtils.getPathFromUri(this, it) ?: "")
//
//                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                MultipartBody.Part.createFormData("technologies_images[]", file.name, requestFile)
//            }
//        }
//
//        val api = RetrofitClient.instance.create(ApiService::class.java)
//        val call = api.uploadHospitalProfile(formFields, logoPart, specialtiesImagePart, techImageParts)
//
//        call.enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                progressDialog.dismiss()
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@HospitalHomeActivity, "Saved successfully", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@HospitalHomeActivity, DashboardActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this@HospitalHomeActivity, "Failed to save", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                progressDialog.dismiss()
//                Toast.makeText(this@HospitalHomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
//
//// Extension to allow indexed mapNotNull
//inline fun <T, R : Any> List<T>.mapNotNullIndexed(transform: (index: Int, T) -> R?): List<R> {
//    return mapIndexedNotNull(transform)
//}
//

//package com.saveetha.smarthealthcareapp
//
//import ApiResponse
//import android.app.ProgressDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Specialty
//import com.saveetha.smarthealthcareapp.models.Technology
//import com.saveetha.smarthealthcareapp.network.RetrofitClient
//import com.saveetha.smarthealthcareapp.utils.FileUtils
//import okhttp3.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//
//class HospitalHomeActivity : AppCompatActivity() {
//
//    private lateinit var imgHospital: ImageView
//    private lateinit var btnUploadImage: ImageView
//    private lateinit var etHospitalName: EditText
//    private lateinit var etAbout: EditText
//    private lateinit var etAddress: EditText
//    private lateinit var etContact: EditText
//    private lateinit var cbEmergency: CheckBox
//    private lateinit var btnSave: TextView
//
//    private lateinit var rvSpecialties: RecyclerView
//    private lateinit var specialtiesAdapter: SpecialtyAdapter
//    private val specialties = mutableListOf<Specialty>()
//
//    private lateinit var rvTechnologies: RecyclerView
//    private lateinit var techAdapter: TechnologyAdapter
//    private val technologies = mutableListOf<Technology>()
//
//    private var selectedImageUri: Uri? = null
//    private val PICK_IMAGE_REQUEST = 1001
//
//    private lateinit var techImageLauncher: ActivityResultLauncher<Intent>
//    private var currentTechPosition: Int = -1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_home)
//
//        imgHospital = findViewById(R.id.imgHospital)
//        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
//        etHospitalName = findViewById(R.id.etHospitalName)
//        etAbout = findViewById(R.id.etAbout)
//        etAddress = findViewById(R.id.etAddress)
//        etContact = findViewById(R.id.etContact)
//        cbEmergency = findViewById(R.id.cbEmergency)
//        btnSave = findViewById(R.id.btnSave)
//
//        rvSpecialties = findViewById(R.id.rvSpecialties)
//        rvSpecialties.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        specialtiesAdapter = SpecialtyAdapter(specialties)
//        rvSpecialties.adapter = specialtiesAdapter
//
//        rvTechnologies = findViewById(R.id.rvTechnologies)
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        techAdapter = TechnologyAdapter(technologies) { position ->
//            currentTechPosition = position
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            techImageLauncher.launch(intent)
//        }
//        rvTechnologies.adapter = techAdapter
//
//        techImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null && currentTechPosition in technologies.indices) {
//                    technologies[currentTechPosition].imageUri = uri
//                    techAdapter.updateTechnologyImage(currentTechPosition, uri)
//                }
//            }
//        }
//
//        btnUploadImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        btnSave.setOnClickListener {
//            if (isFormValid()) {
//                uploadHospitalProfile()
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            val uri = data?.data
//            if (uri != null) {
//                selectedImageUri = uri
//                imgHospital.setImageURI(uri)
//            }
//        }
//    }
//
//    private fun isFormValid(): Boolean {
//        return when {
//            etHospitalName.text.isNullOrEmpty() -> {
//                etHospitalName.error = "Enter hospital name"; false
//            }
//            etAbout.text.isNullOrEmpty() -> {
//                etAbout.error = "Enter about hospital"; false
//            }
//            etAddress.text.isNullOrEmpty() -> {
//                etAddress.error = "Enter address"; false
//            }
//            etContact.text.isNullOrEmpty() -> {
//                etContact.error = "Enter contact number"; false
//            }
//            else -> true
//        }
//    }
//
//    private fun uploadHospitalProfile() {
//        val progressDialog = ProgressDialog(this).apply {
//            setMessage("Uploading...")
//            setCancelable(false)
//            show()
//        }
//
//        val name = etHospitalName.text.toString()
//        val about = etAbout.text.toString()
//        val address = etAddress.text.toString()
//        val contact = etContact.text.toString()
//        val emergency = if (cbEmergency.isChecked) "1" else "0"
//
//        val specialtiesJson = specialties.joinToString(",") { it.name }
//        val technologiesJson = technologies.joinToString(",") { it.name }
//
//        val formFields = mapOf(
//            "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "about" to about.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "specialties" to specialtiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "technologies" to technologiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "facilities" to "ICU:5,OT:3,Beds:50".toRequestBody("text/plain".toMediaTypeOrNull()),
//            "address" to address.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "contact" to contact.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "emergency_services" to emergency.toRequestBody("text/plain".toMediaTypeOrNull())
//        )
//
//        val logoPart = selectedImageUri?.let {
//            val file = File(FileUtils.getPathFromUri(this, it) ?: return@let null)
//            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//            MultipartBody.Part.createFormData("logo", file.name, requestFile)
//        }
//
//        val techImageParts = technologies.mapNotNull { tech ->
//            tech.imageUri?.let {
//                val file = File(FileUtils.getPathFromUri(this, it) ?: return@let null)
//                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                MultipartBody.Part.createFormData("technologies_images[]", file.name, requestFile)
//            }
//        }
//
//        val api = RetrofitClient.instance.create(ApiService::class.java)
//        val call = api.uploadHospitalProfile(formFields, logoPart, null, techImageParts)
//
//        call.enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                progressDialog.dismiss()
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@HospitalHomeActivity, "Saved successfully", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@HospitalHomeActivity, DashboardActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this@HospitalHomeActivity, "Failed to save", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                progressDialog.dismiss()
//                Toast.makeText(this@HospitalHomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}




//FINAL CODE




//FFFFFFFIIIIIIIIINNNNNNAAAAALLLLLLLLLL CCCCCCOOOOOOOODDDDDDEEEEEEE//
package com.saveetha.smarthealthcareapp

import ApiResponse
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
import com.saveetha.smarthealthcareapp.models.Specialty
import com.saveetha.smarthealthcareapp.models.Technology
import com.saveetha.smarthealthcareapp.network.ApiService
import com.saveetha.smarthealthcareapp.network.RetrofitClient
import com.saveetha.smarthealthcareapp.utils.FileUtils
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class HospitalHomeActivity : AppCompatActivity() {

    private lateinit var imgHospital: ImageView
    private lateinit var btnUploadImage: ImageView
    private lateinit var etHospitalName: EditText
    private lateinit var etAbout: EditText
    private lateinit var etAddress: EditText
    private lateinit var etContact: EditText
    private lateinit var cbEmergency: CheckBox
    private lateinit var btnSave: TextView
    private lateinit var btnAddSpecialty: Button
    private lateinit var btnAddTechnology: Button
    private lateinit var btnAddFacility: Button
    private lateinit var btnLogout: TextView


    private lateinit var rvSpecialties: RecyclerView
    private lateinit var specialtiesAdapter: SpecialtyAdapter
    private val specialties = mutableListOf<Specialty>()

    private lateinit var rvTechnologies: RecyclerView
    private lateinit var techAdapter: TechnologyAdapter
    private val technologies = mutableListOf<Technology>()

    private lateinit var facilityContainer: LinearLayout

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1001

    private lateinit var techImageLauncher: ActivityResultLauncher<Intent>
    private var currentTechPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_home)

        imgHospital = findViewById(R.id.imgHospital)
        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
        etHospitalName = findViewById(R.id.etHospitalName)
        etAbout = findViewById(R.id.etAbout)
        etAddress = findViewById(R.id.etAddress)
        etContact = findViewById(R.id.etContact)
        cbEmergency = findViewById(R.id.cbEmergency)
        btnSave = findViewById(R.id.btnSave)
        btnAddSpecialty = findViewById(R.id.btnAddSpecialty)
        btnAddTechnology = findViewById(R.id.btnAddTechnology)
        facilityContainer = findViewById(R.id.facilityContainer)
        btnAddFacility = findViewById(R.id.btnAddFacility)
        btnLogout = findViewById(R.id.btnLogout)


        specialties.add(Specialty(""))
        specialtiesAdapter = SpecialtyAdapter(specialties)
        rvSpecialties = findViewById(R.id.rvSpecialties)
        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
        rvSpecialties.adapter = specialtiesAdapter

        technologies.add(Technology())
        techAdapter = TechnologyAdapter(technologies) { position ->
            currentTechPosition = position
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            techImageLauncher.launch(intent)
        }
        rvTechnologies = findViewById(R.id.rvTechnologies)
        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTechnologies.adapter = techAdapter

        techImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                if (uri != null && currentTechPosition in technologies.indices) {
                    technologies[currentTechPosition].imageUri = uri
                    techAdapter.updateTechnologyImage(currentTechPosition, uri)
                }
            }
        }

        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnAddSpecialty.setOnClickListener {
            specialties.add(Specialty(""))
            specialtiesAdapter.notifyItemInserted(specialties.size - 1)
        }

        btnAddTechnology.setOnClickListener {
            technologies.add(Technology())
            techAdapter.notifyItemInserted(technologies.size - 1)
        }

        btnAddFacility.setOnClickListener {
            addFacilityRow()
        }

        addFacilityRow()

        btnSave.setOnClickListener {
            if (isFormValid()) {
                uploadHospitalProfile()
            }
        }
        btnLogout.setOnClickListener {
            // Clear the correct session store
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            prefs.edit().clear().apply()

            // Redirect to common login screen
            val intent = Intent(this, PatientLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


    }

    private fun addFacilityRow() {
        val inflater = LayoutInflater.from(this)
        val facilityView = inflater.inflate(R.layout.facility_input_row, null)

        facilityView.findViewById<ImageView>(R.id.btnRemoveFacility).setOnClickListener {
            facilityContainer.removeView(facilityView)
        }

        facilityContainer.addView(facilityView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                selectedImageUri = uri
                imgHospital.setImageURI(uri)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            etHospitalName.text.isNullOrEmpty() -> {
                etHospitalName.error = "Enter hospital name"; false
            }
            etAbout.text.isNullOrEmpty() -> {
                etAbout.error = "Enter about hospital"; false
            }
            etAddress.text.isNullOrEmpty() -> {
                etAddress.error = "Enter address"; false
            }
            etContact.text.isNullOrEmpty() -> {
                etContact.error = "Enter contact number"; false
            }
            else -> true
        }
    }

    private fun uploadHospitalProfile() {
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading...")
            setCancelable(false)
            show()
        }

        val name = etHospitalName.text.toString()
        val about = etAbout.text.toString()
        val address = etAddress.text.toString()
        val contact = etContact.text.toString()
        val emergency = if (cbEmergency.isChecked) "1" else "0"

        val specialtiesJson = specialties.map { it.name.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(",")

        val technologiesJson = technologies.map { it.name.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(",")

        val facilityList = mutableListOf<String>()
        for (i in 0 until facilityContainer.childCount) {
            val row = facilityContainer.getChildAt(i)
            val nameView = row.findViewById<EditText>(R.id.etFacilityName)
            val countView = row.findViewById<EditText>(R.id.etFacilityCount)
            val fName = nameView?.text?.toString()?.trim()
            val fCount = countView?.text?.toString()?.trim()
            if (!fName.isNullOrEmpty() && !fCount.isNullOrEmpty()) {
                facilityList.add("$fName:$fCount")
            }
        }
        val facilitiesString = facilityList.joinToString(",")
//        val name = etHospitalName.text.toString().trim()
//        val about = etAbout.text.toString().trim()
        val formFields = mapOf(
            "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
            "about" to about.toRequestBody("text/plain".toMediaTypeOrNull()),
            "specialties" to specialtiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
            "technologies" to technologiesJson.toRequestBody("text/plain".toMediaTypeOrNull()),
            "facilities" to facilitiesString.toRequestBody("text/plain".toMediaTypeOrNull()),
            "address" to address.toRequestBody("text/plain".toMediaTypeOrNull()),
            "contact" to contact.toRequestBody("text/plain".toMediaTypeOrNull()),
            "emergency_services" to emergency.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        val techNames = technologies.map { it.name.trim() }.filter { it.isNotEmpty() }
        val techUris = technologies.mapNotNull { it.imageUri?.toString() }
        val logoPart = selectedImageUri?.let {
            val file = File(FileUtils.getPathFromUri(this, it) ?: return@let null)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("logo", file.name, requestFile)
        }
//        val logoPart = selectedImageUri?.let {
//            val filePath = FileUtils.getPathFromUri(this, it) ?: return@let null
//            val file = File(filePath)
//            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//
//            // ✅ use 'fileName' to avoid overwriting 'name'
//            val fileName = file.name
//            MultipartBody.Part.createFormData("logo", fileName, requestFile)
//        }


        val techImageParts = technologies.mapNotNull { tech ->
            tech.imageUri?.let {
                val file = File(FileUtils.getPathFromUri(this, it) ?: return@let null)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("technologies_images[]", file.name, requestFile)
            }
        }

        val api = RetrofitClient.instance.create(ApiService::class.java)
        val call = api.uploadHospitalProfile(formFields, logoPart, null, techImageParts)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@HospitalHomeActivity, "Saved successfully", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@HospitalHomeActivity, DashboardActivity::class.java))
                    val intent = Intent(this@HospitalHomeActivity, HospitalPreviewActivity::class.java).apply {
                        putExtra("name", name)
                        putExtra("about", about)
                        putExtra("address", address)
                        putExtra("contact", contact)
                        putExtra("emergency", emergency)
                        putExtra("logoUri", selectedImageUri.toString())
                        putExtra("specialties", specialtiesJson)
                        putExtra("technologies", technologiesJson)
                        putExtra("facilities", facilitiesString)
                        putStringArrayListExtra("technologyNames", ArrayList(techNames))
                        putStringArrayListExtra("technologyUris", ArrayList(techUris))
                    }
                    startActivity(intent)

                    finish()
                } else {
                    Toast.makeText(this@HospitalHomeActivity, "Failed to save", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@HospitalHomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}










// HospitalHomeActivity.kt...................................................
//package com.saveetha.smarthealthcareapp
//
//import android.app.ProgressDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Specialty
//import com.saveetha.smarthealthcareapp.models.Technology
//import com.saveetha.smarthealthcareapp.network.RetrofitClient
//import com.saveetha.smarthealthcareapp.utils.FileUtils
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//
//class HospitalHomeActivity : AppCompatActivity() {
//
//    private lateinit var imgHospital: ImageView
//    private lateinit var btnUploadImage: ImageView
//    private lateinit var etHospitalName: EditText
//    private lateinit var etAbout: EditText
//    private lateinit var etAddress: EditText
//    private lateinit var etContact: EditText
//    private lateinit var cbEmergency: CheckBox
//    private lateinit var btnSave: TextView
//    private lateinit var btnAddSpecialty: Button
//    private lateinit var btnAddTechnology: Button
//    private lateinit var btnAddFacility: Button
//    private lateinit var rvSpecialties: RecyclerView
//    private lateinit var specialtiesAdapter: SpecialtyAdapter
//    private val specialties = mutableListOf<Specialty>()
//    private lateinit var rvTechnologies: RecyclerView
//    private lateinit var techAdapter: TechnologyAdapter
//    private val technologies = mutableListOf<Technology>()
//    private lateinit var facilityContainer: LinearLayout
//    private var selectedImageUri: Uri? = null
//    private val PICK_IMAGE_REQUEST = 1001
//    private lateinit var techImageLauncher: ActivityResultLauncher<Intent>
//    private var currentTechPosition: Int = -1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_home)
//
//        imgHospital = findViewById(R.id.imgHospital)
//        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
//        etHospitalName = findViewById(R.id.etHospitalName)
//        etAbout = findViewById(R.id.etAbout)
//        etAddress = findViewById(R.id.etAddress)
//        etContact = findViewById(R.id.etContact)
//        cbEmergency = findViewById(R.id.cbEmergency)
//        btnSave = findViewById(R.id.btnSave)
//        btnAddSpecialty = findViewById(R.id.btnAddSpecialty)
//        btnAddTechnology = findViewById(R.id.btnAddTechnology)
//        btnAddFacility = findViewById(R.id.btnAddFacility)
//        facilityContainer = findViewById(R.id.facilityContainer)
//
//        specialties.add(Specialty(""))
//        specialtiesAdapter = SpecialtyAdapter(specialties)
//        rvSpecialties = findViewById(R.id.rvSpecialties)
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = specialtiesAdapter
//
//        technologies.add(Technology())
//        techAdapter = TechnologyAdapter(technologies) { position ->
//            currentTechPosition = position
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            techImageLauncher.launch(intent)
//        }
//        rvTechnologies = findViewById(R.id.rvTechnologies)
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rvTechnologies.adapter = techAdapter
//
//        techImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null && currentTechPosition in technologies.indices) {
//                    technologies[currentTechPosition].imageUri = uri
//                    techAdapter.updateTechnologyImage(currentTechPosition, uri)
//                }
//            }
//        }
//
//        btnUploadImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        btnAddSpecialty.setOnClickListener {
//            specialties.add(Specialty(""))
//            specialtiesAdapter.notifyItemInserted(specialties.size - 1)
//        }
//
//        btnAddTechnology.setOnClickListener {
//            technologies.add(Technology())
//            techAdapter.notifyItemInserted(technologies.size - 1)
//        }
//
//        btnAddFacility.setOnClickListener { addFacilityRow() }
//        addFacilityRow()
//
//        btnSave.setOnClickListener {
//            if (isFormValid()) {
//                val name = etHospitalName.text.toString()
//                val about = etAbout.text.toString()
//                val address = etAddress.text.toString()
//                val contact = etContact.text.toString()
//                val emergency = if (cbEmergency.isChecked) "1" else "0"
//                val specialtiesJson = specialties.map { it.name.trim() }.filter { it.isNotEmpty() }.joinToString(",")
//                val technologiesJson = technologies.map { it.name.trim() }.filter { it.isNotEmpty() }.joinToString(",")
////                val techNames = technologies.map { it.name.trim() }.filter { it.isNotEmpty() }
////                val techUris = technologies.map { it.imageUri.toString() }.filter { it.isNotEmpty() }
//
//                val facilityList = mutableListOf<String>()
//                for (i in 0 until facilityContainer.childCount) {
//                    val row = facilityContainer.getChildAt(i)
//                    val nameView = row.findViewById<EditText>(R.id.etFacilityName)
//                    val countView = row.findViewById<EditText>(R.id.etFacilityCount)
//                    val fName = nameView?.text?.toString()?.trim()
//                    val fCount = countView?.text?.toString()?.trim()
//                    if (!fName.isNullOrEmpty() && !fCount.isNullOrEmpty()) {
//                        facilityList.add("$fName:$fCount")
//                    }
//                }
//                val facilitiesString = facilityList.joinToString(",")
//
//                val intent = Intent(this@HospitalHomeActivity, HospitalPreviewActivity::class.java)
//                intent.putExtra("name", name)
//                intent.putExtra("about", about)
//                intent.putExtra("address", address)
//                intent.putExtra("contact", contact)
//                intent.putExtra("emergency", emergency)
//                intent.putExtra("logoUri", selectedImageUri?.toString() ?: "")
//                intent.putExtra("specialties", specialtiesJson)
//                intent.putExtra("technologies", technologiesJson)
//                intent.putExtra("facilities", facilitiesString)
//                 // intent.putStringArrayListExtra("technologyNames", ArrayList(techNames))
//                 // intent.putStringArrayListExtra("technologyUris", ArrayList(techUris))
//
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun addFacilityRow() {
//        val inflater = LayoutInflater.from(this)
//        val facilityView = inflater.inflate(R.layout.facility_input_row, null)
//        facilityView.findViewById<ImageView>(R.id.btnRemoveFacility).setOnClickListener {
//            facilityContainer.removeView(facilityView)
//        }
//        facilityContainer.addView(facilityView)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            val uri = data?.data
//            if (uri != null) {
//                selectedImageUri = uri
//                imgHospital.setImageURI(uri)
//            }
//        }
//    }
//
//    private fun isFormValid(): Boolean {
//        return when {
//            etHospitalName.text.isNullOrEmpty() -> { etHospitalName.error = "Enter hospital name"; false }
//            etAbout.text.isNullOrEmpty() -> { etAbout.error = "Enter about hospital"; false }
//            etAddress.text.isNullOrEmpty() -> { etAddress.error = "Enter address"; false }
//            etContact.text.isNullOrEmpty() -> { etContact.error = "Enter contact number"; false }
//            else -> true
//        }
//    }
//}

//
//
//package com.saveetha.smarthealthcareapp
//
//import android.app.ProgressDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Specialty
//import com.saveetha.smarthealthcareapp.models.Technology
//
//class HospitalHomeActivity : AppCompatActivity() {
//
//    private lateinit var imgHospital: ImageView
//    private lateinit var btnUploadImage: ImageView
//    private lateinit var etHospitalName: EditText
//    private lateinit var etAbout: EditText
//    private lateinit var etAddress: EditText
//    private lateinit var etContact: EditText
//    private lateinit var cbEmergency: CheckBox
//    private lateinit var btnSave: TextView
//    private lateinit var btnAddSpecialty: Button
//    private lateinit var btnAddTechnology: Button
//    private lateinit var btnAddFacility: Button
//    private lateinit var rvSpecialties: RecyclerView
//    private lateinit var specialtiesAdapter: SpecialtyAdapter
//    private val specialties = mutableListOf<Specialty>()
//    private lateinit var rvTechnologies: RecyclerView
//    private lateinit var techAdapter: TechnologyAdapter
//    private val technologies = mutableListOf<Technology>()
//    private lateinit var facilityContainer: LinearLayout
//    private var selectedImageUri: Uri? = null
//    private val PICK_IMAGE_REQUEST = 1001
//    private lateinit var techImageLauncher: ActivityResultLauncher<Intent>
//    private var currentTechPosition: Int = -1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_home)
//
//        imgHospital = findViewById(R.id.imgHospital)
//        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
//        etHospitalName = findViewById(R.id.etHospitalName)
//        etAbout = findViewById(R.id.etAbout)
//        etAddress = findViewById(R.id.etAddress)
//        etContact = findViewById(R.id.etContact)
//        cbEmergency = findViewById(R.id.cbEmergency)
//        btnSave = findViewById(R.id.btnSave)
//        btnAddSpecialty = findViewById(R.id.btnAddSpecialty)
//        btnAddTechnology = findViewById(R.id.btnAddTechnology)
//        btnAddFacility = findViewById(R.id.btnAddFacility)
//        facilityContainer = findViewById(R.id.facilityContainer)
//
//        specialties.add(Specialty(""))
//        specialtiesAdapter = SpecialtyAdapter(specialties)
//        rvSpecialties = findViewById(R.id.rvSpecialties)
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = specialtiesAdapter
//
//        technologies.add(Technology())
//        techAdapter = TechnologyAdapter(technologies) { position ->
//            currentTechPosition = position
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            techImageLauncher.launch(intent)
//        }
//        rvTechnologies = findViewById(R.id.rvTechnologies)
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rvTechnologies.adapter = techAdapter
//
//        techImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null && currentTechPosition in technologies.indices) {
//                    technologies[currentTechPosition].imageUri = uri
//                    techAdapter.updateTechnologyImage(currentTechPosition, uri)
//                }
//            }
//        }
//
//        btnUploadImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        btnAddSpecialty.setOnClickListener {
//            specialties.add(Specialty(""))
//            specialtiesAdapter.notifyItemInserted(specialties.size - 1)
//        }
//
//        btnAddTechnology.setOnClickListener {
//            technologies.add(Technology())
//            techAdapter.notifyItemInserted(technologies.size - 1)
//        }
//
//        btnAddFacility.setOnClickListener { addFacilityRow() }
//        addFacilityRow()
//
//        btnSave.setOnClickListener {
//            if (isFormValid()) {
//                val name = etHospitalName.text.toString()
//                val about = etAbout.text.toString()
//                val address = etAddress.text.toString()
//                val contact = etContact.text.toString()
//                val emergency = if (cbEmergency.isChecked) "1" else "0"
//
//                val specialtiesList = specialties.map { it.name.trim() }.filter { it.isNotEmpty() }
//                val techNames = technologies.map { it.name.trim() }.filter { it.isNotEmpty() }
//                val techUris = technologies.map { it.imageUri.toString() }.filter { it.isNotEmpty() }
//
//                val facilityList = mutableListOf<String>()
//                for (i in 0 until facilityContainer.childCount) {
//                    val row = facilityContainer.getChildAt(i)
//                    val nameView = row.findViewById<EditText>(R.id.etFacilityName)
//                    val countView = row.findViewById<EditText>(R.id.etFacilityCount)
//                    val fName = nameView?.text?.toString()?.trim()
//                    val fCount = countView?.text?.toString()?.trim()
//                    if (!fName.isNullOrEmpty() && !fCount.isNullOrEmpty()) {
//                        facilityList.add("$fName:$fCount")
//                    }
//                }
//
//                val intent = Intent(this@HospitalHomeActivity, HospitalPreviewActivity::class.java)
//                intent.putExtra("name", name)
//                intent.putExtra("about", about)
//                intent.putExtra("address", address)
//                intent.putExtra("contact", contact)
//                intent.putExtra("emergency", emergency)
//                intent.putExtra("logoUri", selectedImageUri?.toString() ?: "")
//                intent.putStringArrayListExtra("specialties", ArrayList(specialtiesList))
//                intent.putStringArrayListExtra("technologyNames", ArrayList(techNames))
//                intent.putStringArrayListExtra("technologyUris", ArrayList(techUris))
//                intent.putStringArrayListExtra("facilities", ArrayList(facilityList))
//
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun addFacilityRow() {
//        val inflater = LayoutInflater.from(this)
//        val facilityView = inflater.inflate(R.layout.facility_input_row, null)
//        facilityView.findViewById<ImageView>(R.id.btnRemoveFacility).setOnClickListener {
//            facilityContainer.removeView(facilityView)
//        }
//        facilityContainer.addView(facilityView)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            val uri = data?.data
//            if (uri != null) {
//                selectedImageUri = uri
//                imgHospital.setImageURI(uri)
//            }
//        }
//    }
//
//    private fun isFormValid(): Boolean {
//        return when {
//            etHospitalName.text.isNullOrEmpty() -> { etHospitalName.error = "Enter hospital name"; false }
//            etAbout.text.isNullOrEmpty() -> { etAbout.error = "Enter about hospital"; false }
//            etAddress.text.isNullOrEmpty() -> { etAddress.error = "Enter address"; false }
//            etContact.text.isNullOrEmpty() -> { etContact.error = "Enter contact number"; false }
//            else -> true
//        }
//    }
//}
//package com.saveetha.smarthealthcareapp
//
//import android.app.ProgressDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Specialty
//import com.saveetha.smarthealthcareapp.models.Technology
//
//class HospitalHomeActivity : AppCompatActivity() {
//
//    private lateinit var imgHospital: ImageView
//    private lateinit var btnUploadImage: ImageView
//    private lateinit var etHospitalName: EditText
//    private lateinit var etAbout: EditText
//    private lateinit var etAddress: EditText
//    private lateinit var etContact: EditText
//    private lateinit var cbEmergency: CheckBox
//    private lateinit var btnSave: TextView
//    private lateinit var btnAddSpecialty: Button
//    private lateinit var btnAddTechnology: Button
//    private lateinit var btnAddFacility: Button
//    private lateinit var rvSpecialties: RecyclerView
//    private lateinit var specialtiesAdapter: SpecialtyAdapter
//    private val specialties = mutableListOf<Specialty>()
//    private lateinit var rvTechnologies: RecyclerView
//    private lateinit var techAdapter: TechnologyAdapter
//    private val technologies = mutableListOf<Technology>()
//    private lateinit var facilityContainer: LinearLayout
//    private var selectedImageUri: Uri? = null
//    private val PICK_IMAGE_REQUEST = 1001
//    private lateinit var techImageLauncher: ActivityResultLauncher<Intent>
//    private var currentTechPosition: Int = -1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_home)
//
//        imgHospital = findViewById(R.id.imgHospital)
//        btnUploadImage = findViewById(R.id.btnUploadHospitalImage)
//        etHospitalName = findViewById(R.id.etHospitalName)
//        etAbout = findViewById(R.id.etAbout)
//        etAddress = findViewById(R.id.etAddress)
//        etContact = findViewById(R.id.etContact)
//        cbEmergency = findViewById(R.id.cbEmergency)
//        btnSave = findViewById(R.id.btnSave)
//        btnAddSpecialty = findViewById(R.id.btnAddSpecialty)
//        btnAddTechnology = findViewById(R.id.btnAddTechnology)
//        btnAddFacility = findViewById(R.id.btnAddFacility)
//        facilityContainer = findViewById(R.id.facilityContainer)
//
//        specialties.add(Specialty(""))
//        specialtiesAdapter = SpecialtyAdapter(specialties)
//        rvSpecialties = findViewById(R.id.rvSpecialties)
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = specialtiesAdapter
//
//        technologies.add(Technology())
//        techAdapter = TechnologyAdapter(technologies) { position ->
//            currentTechPosition = position
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            techImageLauncher.launch(intent)
//        }
//        rvTechnologies = findViewById(R.id.rvTechnologies)
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rvTechnologies.adapter = techAdapter
//
//        techImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null && currentTechPosition in technologies.indices) {
//                    technologies[currentTechPosition].imageUri = uri
//                    techAdapter.updateTechnologyImage(currentTechPosition, uri)
//                }
//            }
//        }
//
//        btnUploadImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        btnAddSpecialty.setOnClickListener {
//            specialties.add(Specialty(""))
//            specialtiesAdapter.notifyItemInserted(specialties.size - 1)
//        }
//
//        btnAddTechnology.setOnClickListener {
//            technologies.add(Technology())
//            techAdapter.notifyItemInserted(technologies.size - 1)
//        }
//
//        btnAddFacility.setOnClickListener { addFacilityRow() }
//        addFacilityRow()
//
//        btnSave.setOnClickListener {
//            if (isFormValid()) {
//                val name = etHospitalName.text.toString()
//                val about = etAbout.text.toString()
//                val address = etAddress.text.toString()
//                val contact = etContact.text.toString()
//                val emergency = if (cbEmergency.isChecked) "1" else "0"
//
//                val specialtiesList = specialties.map { it.name.trim() }.filter { it.isNotEmpty() }
//                val techNames = technologies.map { it.name.trim() }.filter { it.isNotEmpty() }
//                val techUris = technologies.map { it.imageUri.toString() }.filter { it.isNotEmpty() }
//
//                val facilityList = mutableListOf<String>()
//                for (i in 0 until facilityContainer.childCount) {
//                    val row = facilityContainer.getChildAt(i)
//                    val nameView = row.findViewById<EditText>(R.id.etFacilityName)
//                    val countView = row.findViewById<EditText>(R.id.etFacilityCount)
//                    val fName = nameView?.text?.toString()?.trim()
//                    val fCount = countView?.text?.toString()?.trim()
//                    if (!fName.isNullOrEmpty() && !fCount.isNullOrEmpty()) {
//                        facilityList.add("$fName:$fCount")
//                    }
//                }
//
//                val intent = Intent(this@HospitalHomeActivity, HospitalPreviewActivity::class.java)
//                intent.putExtra("name", name)
//                intent.putExtra("about", about)
//                intent.putExtra("address", address)
//                intent.putExtra("contact", contact)
//                intent.putExtra("emergency", emergency)
//                intent.putExtra("logoUri", selectedImageUri?.toString() ?: "")
//                intent.putStringArrayListExtra("specialties", ArrayList(specialtiesList))
//                intent.putStringArrayListExtra("technologyNames", ArrayList(techNames))
//                intent.putStringArrayListExtra("technologyUris", ArrayList(techUris))
//                intent.putStringArrayListExtra("facilities", ArrayList(facilityList))
//
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun addFacilityRow() {
//        val inflater = LayoutInflater.from(this)
//        val facilityView = inflater.inflate(R.layout.facility_input_row, null)
//        facilityView.findViewById<ImageView>(R.id.btnRemoveFacility).setOnClickListener {
//            facilityContainer.removeView(facilityView)
//        }
//        facilityContainer.addView(facilityView)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            val uri = data?.data
//            if (uri != null) {
//                selectedImageUri = uri
//                imgHospital.setImageURI(uri)
//            }
//        }
//    }
//
//    private fun isFormValid(): Boolean {
//        return when {
//            etHospitalName.text.isNullOrEmpty() -> { etHospitalName.error = "Enter hospital name"; false }
//            etAbout.text.isNullOrEmpty() -> { etAbout.error = "Enter about hospital"; false }
//            etAddress.text.isNullOrEmpty() -> { etAddress.error = "Enter address"; false }
//            etContact.text.isNullOrEmpty() -> { etContact.error = "Enter contact number"; false }
//            else -> true
//        }
//    }
//}
