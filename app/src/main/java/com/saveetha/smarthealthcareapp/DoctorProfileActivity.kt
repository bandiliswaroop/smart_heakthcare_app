package com.saveetha.smarthealthcareapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.models.UploadResponse
import com.saveetha.smarthealthcareapp.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var cameraIcon: ImageView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_PERMISSIONS = 103
    private lateinit var currentPhotoPath: String

    private val userId: String by lazy {
        intent.getStringExtra("user_id") ?: "-1"
    }

    private val userEmail: String by lazy {
        intent.getStringExtra("user_email") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile) // Same layout as patient

        initializeViews()
        loadProfileImage()
        setupClickListeners()

        // Set up navigation items
        findViewById<TextView>(R.id.textViewHome).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

//        findViewById<TextView>(R.id.Logout).setOnClickListener {
//            val intent = Intent(this, PatientLoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
        findViewById<TextView>(R.id.Logout).setOnClickListener {
            // 1. Get the shared preferences object used for storing the session
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

            // 2. Clear all the saved data from the session file
            sharedPreferences.edit().clear().apply()

            // 3. Redirect to the login activity and clear the activity stack
            val intent = Intent(this, PatientLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<TextView>(R.id.Appointments).setOnClickListener {
            val intent = Intent(this, DoctorAppointmentsActivity::class.java)
            intent.putExtra("user_email", userEmail)
            finish()
        }

        val profileTextView = findViewById<TextView>(R.id.Doctor)
        profileTextView.setOnClickListener {
            Log.d("ProfileClick", "Profile clicked, user_id: $userId") // Add this line
            val intent = Intent(this, DoctorPersonalActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("doctor_id", userId)
            Log.d("ProfileClick", "Starting DoctorPersonalActivity with user_id: $userId")
            startActivity(intent)
        }

    }

    private fun initializeViews() {
        profileImage = findViewById(R.id.profileImage)
        cameraIcon = findViewById(R.id.cameraIcon)
    }

    private fun setupClickListeners() {
        cameraIcon.setOnClickListener {
            if (checkPermissions()) {
                showImageSourceDialog()
            } else {
                requestPermissions()
            }
        }
    }

    private fun getProfileImageKey(): String {
        return "profile_image_doctor_$userId"
    }

    private fun loadProfileImage() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val imageUrl = sharedPref.getString(getProfileImageKey(), null)

        Glide.with(this)
            .load(imageUrl ?: R.drawable.ic_profile)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .into(profileImage)
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSIONS
        )
    }

    private fun showImageSourceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Update Profile Picture")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> dispatchPickFromGalleryIntent()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show()
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchPickFromGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showImageSourceDialog()
            } else {
                Toast.makeText(this, "Permissions are required to update profile picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> handleGalleryResult(data)
                REQUEST_CAMERA -> handleCameraResult()
            }
        }
    }

    private fun handleGalleryResult(data: Intent?) {
        data?.data?.let { uri ->
            uploadImageToServer(uri)
        } ?: run {
            Toast.makeText(this, "Failed to get image from gallery", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCameraResult() {
        File(currentPhotoPath).let { file ->
            if (file.exists()) {
                uploadImageToServer(Uri.fromFile(file))
            } else {
                Toast.makeText(this, "Photo file not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToServer(imageUri: Uri) {
        getRealPathFromURI(imageUri)?.let { filePath ->
            val file = File(filePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
            val userIdPart = MultipartBody.Part.createFormData("doctor_id", userId)


            showProgress("Uploading image...")

            ApiClient.instance.uploadDoctorProfileImage(userIdPart, body).enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    dismissProgress()
                    if (response.isSuccessful) {
                        response.body()?.let { uploadResponse ->
                            if (uploadResponse.status == "success") {
                                handleUploadSuccess(uploadResponse)
                            } else {
                                showError(uploadResponse.message ?: "Upload failed")
                            }
                        } ?: showError("Empty response from server")
                    } else {
                        showError("Server error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    dismissProgress()
                    showError("Network error: ${t.message}")
                }
            })
        } ?: showError("Could not get image path")
    }

    private fun handleUploadSuccess(uploadResponse: UploadResponse) {
        val imageUrl = "http://192.168.24.116/smart_healthcare_app/doctor_profiles/${uploadResponse.image}"

        // Update SharedPreferences
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit()
            .putString(getProfileImageKey(), imageUrl)
            .apply()

        // Update image view
        Glide.with(this)
            .load(imageUrl)
            .circleCrop()
            .into(profileImage)

        // Notify parent activity
        setResult(Activity.RESULT_OK)
        Toast.makeText(this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    private fun showProgress(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun dismissProgress() {
        // Dismiss progress if needed
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e("DoctorProfileUpload", message)
    }
}