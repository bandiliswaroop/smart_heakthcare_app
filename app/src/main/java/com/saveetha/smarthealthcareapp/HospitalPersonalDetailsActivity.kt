package com.saveetha.smarthealthcareapp

import ApiResponse
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.saveetha.smarthealthcareapp.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HospitalPersonalDetailsActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var etHospitalName: EditText
    private lateinit var etRegistrationNumber: EditText
    private lateinit var etHospitalAddress: EditText
    private lateinit var etContactNumber: EditText
    private lateinit var etAdminName: EditText
    private lateinit var etAdminEmail: EditText
    private lateinit var etAdminPosition: EditText
    private lateinit var etDepartment: EditText
    private lateinit var etNumberOfUsers: EditText
    private lateinit var etBriefDescription: EditText

    private lateinit var cbTerms: CheckBox
    private lateinit var btnSubmit: MaterialButton

    private lateinit var btnUploadLicense: MaterialButton
    private lateinit var btnUploadCertificate: MaterialButton
    private lateinit var btnUploadAdditionalDocs: MaterialButton

    // For storing file URIs (optional)
    private var licenseUri: Uri? = null
    private var certificateUri: Uri? = null
    private var additionalDocsUri: Uri? = null

    private val REQUEST_CODE_LICENSE = 100
    private val REQUEST_CODE_CERTIFICATE = 101
    private val REQUEST_CODE_ADDITIONAL_DOCS = 102

    // Retrofit API service
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_personal_details)

        // Initialize UI views
        etHospitalName = findViewById(R.id.etHospitalName)
        etRegistrationNumber = findViewById(R.id.etRegistrationNumber)
        etHospitalAddress = findViewById(R.id.etHospitalAddress)
        etContactNumber = findViewById(R.id.etContactNumber)
        etAdminName = findViewById(R.id.etAdminName)
        etAdminEmail = findViewById(R.id.etAdminEmail)
        etAdminPosition = findViewById(R.id.etAdminPosition)
        etDepartment = findViewById(R.id.etDepartment)
        etNumberOfUsers = findViewById(R.id.etNumberOfUsers)
        etBriefDescription = findViewById(R.id.etBriefDescription)

        cbTerms = findViewById(R.id.cbTerms)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnUploadLicense = findViewById(R.id.btnUploadLicense)
        btnUploadCertificate = findViewById(R.id.btnUploadCertificate)
        btnUploadAdditionalDocs = findViewById(R.id.btnUploadAdditionalDocs)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.24.116/smart_healthcare_app/") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Upload buttons click listeners
        btnUploadLicense.setOnClickListener {
            pickFile(REQUEST_CODE_LICENSE)
        }
        btnUploadCertificate.setOnClickListener {
            pickFile(REQUEST_CODE_CERTIFICATE)
        }
        btnUploadAdditionalDocs.setOnClickListener {
            pickFile(REQUEST_CODE_ADDITIONAL_DOCS)
        }

        btnSubmit.setOnClickListener {
            submitForm()
        }
    }

    private fun pickFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // you can limit to pdf/doc/image if you want
        startActivityForResult(Intent.createChooser(intent, "Select File"), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data
            when(requestCode) {
                REQUEST_CODE_LICENSE -> {
                    licenseUri = fileUri
                    btnUploadLicense.text = "License Selected"
                }
                REQUEST_CODE_CERTIFICATE -> {
                    certificateUri = fileUri
                    btnUploadCertificate.text = "Certificate Selected"
                }
                REQUEST_CODE_ADDITIONAL_DOCS -> {
                    additionalDocsUri = fileUri
                    btnUploadAdditionalDocs.text = "Additional Doc Selected"
                }
            }
        }
    }

    private fun submitForm() {
        val userId = intent.getStringExtra("user_id") ?: ""
        val userIdRB = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        // Validate required fields
        if (etHospitalName.text.isBlank()) {
            etHospitalName.error = "Required"
            return
        }
        if (etRegistrationNumber.text.isBlank()) {
            etRegistrationNumber.error = "Required"
            return
        }
        if (etHospitalAddress.text.isBlank()) {
            etHospitalAddress.error = "Required"
            return
        }
        if (etContactNumber.text.isBlank()) {
            etContactNumber.error = "Required"
            return
        }
        if (etAdminName.text.isBlank()) {
            etAdminName.error = "Required"
            return
        }
        if (etAdminEmail.text.isBlank()) {
            etAdminEmail.error = "Required"
            return
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please agree to the terms", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data for API call
        val hospitalName = etHospitalName.text.toString()
        val regNumber = etRegistrationNumber.text.toString()
        val hospitalAddress = etHospitalAddress.text.toString()
        val contactNumber = etContactNumber.text.toString()
        val adminName = etAdminName.text.toString()
        val adminEmail = etAdminEmail.text.toString()
        val adminPosition = etAdminPosition.text.toString()
        val department = etDepartment.text.toString()
        val numberOfUsers = etNumberOfUsers.text.toString()
        val briefDescription = etBriefDescription.text.toString()

        // Convert text fields to RequestBody
        val hospitalNameRB = hospitalName.toRequestBody("text/plain".toMediaTypeOrNull())
        val regNumberRB = regNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val hospitalAddressRB = hospitalAddress.toRequestBody("text/plain".toMediaTypeOrNull())
        val contactNumberRB = contactNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val adminNameRB = adminName.toRequestBody("text/plain".toMediaTypeOrNull())
        val adminEmailRB = adminEmail.toRequestBody("text/plain".toMediaTypeOrNull())
        val adminPositionRB = adminPosition.toRequestBody("text/plain".toMediaTypeOrNull())
        val departmentRB = department.toRequestBody("text/plain".toMediaTypeOrNull())
        val numberOfUsersRB = numberOfUsers.toRequestBody("text/plain".toMediaTypeOrNull())
        val briefDescriptionRB = briefDescription.toRequestBody("text/plain".toMediaTypeOrNull())

        // File multipart bodies - note the correct parameter names matching PHP
        val licensePart = licenseUri?.let { createMultipartFromUri("hospital_license", it) }
        val certificatePart = certificateUri?.let { createMultipartFromUri("registration_certificate", it) }
        val additionalDocsPart = additionalDocsUri?.let { createMultipartFromUri("additional_documents", it) }

        // Make API call
        val call = apiService.submitHospitalDetails(
            hospitalNameRB,
            regNumberRB,
            hospitalAddressRB,
            contactNumberRB,
            adminNameRB,
            adminEmailRB,
            adminPositionRB,
            departmentRB,
            numberOfUsersRB,
            briefDescriptionRB,
            userIdRB,
            licensePart,
            certificatePart,
            additionalDocsPart
        )

        btnSubmit.isEnabled = false
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                btnSubmit.isEnabled = true
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@HospitalPersonalDetailsActivity, "Request submitted successfully!", Toast.LENGTH_LONG).show()
//                    finish()  // Close activity after success
//                }
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@HospitalPersonalDetailsActivity, "Request submitted successfully!", Toast.LENGTH_LONG).show()

                    // ➡ Navigate to "Request Pending" screen
                    val intent = Intent(this@HospitalPersonalDetailsActivity, RequestPendingActivity::class.java)
                    intent.putExtra("user_id", userId)
                    startActivity(intent)
                    finish()
                }

                else {
                    Toast.makeText(this@HospitalPersonalDetailsActivity, "Failed to submit: ${response.body()?.message ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                btnSubmit.isEnabled = true
                Toast.makeText(this@HospitalPersonalDetailsActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    // Helper to create MultipartBody.Part from Uri
    private fun createMultipartFromUri(partName: String, fileUri: Uri): MultipartBody.Part? {
        val file = FileUtils.getFileFromUri(this, fileUri) ?: return null
        val requestFile = RequestBody.create(contentResolver.getType(fileUri)?.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}
