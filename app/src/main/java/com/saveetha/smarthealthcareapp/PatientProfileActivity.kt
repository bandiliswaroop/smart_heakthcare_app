//package com.saveetha.smarthealthcareapp
//
//import android.Manifest
//import android.app.Activity
//import android.app.AlertDialog
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.models.UploadResponse
//import com.saveetha.smarthealthcareapp.network.ApiClient
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class PatientProfileActivity : AppCompatActivity() {
//
//    private lateinit var profileImage: ImageView
//    private lateinit var cameraIcon: ImageView
//    private val REQUEST_CAMERA = 101
//    private val REQUEST_GALLERY = 102
//    private val REQUEST_PERMISSIONS = 103
//    private lateinit var currentPhotoPath: String
//
//    // Get user ID from intent
//    private val userId: String by lazy {
//        intent.getStringExtra("user_id") ?: "-1"
//    }
//
//    // Get user role from intent
//    private val userRole: String by lazy {
//        intent.getStringExtra("user_role") ?: "patient"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_profile)
//
//        profileImage = findViewById(R.id.profileImage)
//        cameraIcon = findViewById(R.id.cameraIcon)
//
//        loadProfileImage()
//
//        cameraIcon.setOnClickListener {
//            if (checkPermissions()) {
//                openImageChooser()
//            } else {
//                requestPermissions()
//            }
//        }
//    }
//
//    private fun getProfileImageKey(): String {
//        return "profile_image_${userRole}_${userId}"
//    }
//
//    private fun loadProfileImage() {
//        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        val imageUrl = sharedPreferences.getString(getProfileImageKey(), null)
//
//        if (imageUrl != null && imageUrl.isNotEmpty()) {
//            Glide.with(this)
//                .load(imageUrl)
//                .placeholder(R.drawable.ic_profile)
//                .error(R.drawable.ic_profile)
//                .circleCrop()
//                .into(profileImage)
//        } else {
//            Glide.with(this)
//                .load(R.drawable.ic_profile)
//                .circleCrop()
//                .into(profileImage)
//        }
//    }
//
//    private fun checkPermissions(): Boolean {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_PERMISSIONS
//        )
//    }
//
//    private fun openImageChooser() {
//        val options = arrayOf("Take Photo", "Choose from Gallery")
//        AlertDialog.Builder(this)
//            .setTitle("Select Option")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> openCamera()
//                    1 -> openGallery()
//                }
//            }
//            .show()
//    }
//
//    private fun openCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    Toast.makeText(this, "Camera error", Toast.LENGTH_SHORT).show()
//                    null
//                }
//                photoFile?.also {
//                    val photoURI = FileProvider.getUriForFile(
//                        this,
//                        "$packageName.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_CAMERA)
//                }
//            }
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
//            currentPhotoPath = absolutePath
//        }
//    }
//
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_PERMISSIONS && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//            openImageChooser()
//        } else {
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_GALLERY -> data?.data?.let { uri ->
//                    uploadImageToServer(uri)
//                }
//                REQUEST_CAMERA -> Uri.fromFile(File(currentPhotoPath)).let { uri ->
//                    uploadImageToServer(uri)
//                }
//            }
//        }
//    }
//
//    private fun uploadImageToServer(imageUri: Uri) {
//        getRealPathFromURI(imageUri)?.let { filePath ->
//            val file = File(filePath)
//            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//            val body = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
//
//            // Ensure user ID is properly passed
//            val userIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())
//
//            // Add logging
//            Log.d("UploadDebug", "Attempting upload for user ID: $userId")
//
//            ApiClient.instance.uploadProfileImage(userIdPart, body).enqueue(object : Callback<UploadResponse> {
//                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
//                    if (response.isSuccessful) {
//                        response.body()?.let { uploadResponse ->
//                            if (uploadResponse.status == "success") {
//                                // Handle success
//                                val imageUrl = "http://192.168.24.116/smart_healthcare_app/patient_profiles/${uploadResponse.image}"
//
//                                // Save with user-specific key
//                                // Save with user-specific key
//                                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                                val editor = sharedPref.edit()
//                                editor.putString(getProfileImageKey(), imageUrl)
//                                editor.apply()
//
//                                Glide.with(this@PatientProfileActivity)
//                                    .load(imageUrl)
//                                    .circleCrop()
//                                    .into(profileImage)
//
//                                Toast.makeText(this@PatientProfileActivity,
//                                    "Profile updated successfully",
//                                    Toast.LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(this@PatientProfileActivity,
//                                    uploadResponse.message ?: "Upload failed",
//                                    Toast.LENGTH_LONG).show()
//                            }
//                        } ?: run {
//                            Toast.makeText(this@PatientProfileActivity,
//                                "Empty response from server",
//                                Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        try {
//                            // Try to read error body
//                            val errorBody = response.errorBody()?.string()
//                            Log.e("UploadError", "Server error: $errorBody")
//                            Toast.makeText(this@PatientProfileActivity,
//                                "Server error: ${response.code()} - $errorBody",
//                                Toast.LENGTH_LONG).show()
//                        } catch (e: Exception) {
//                            Toast.makeText(this@PatientProfileActivity,
//                                "Server error: ${response.code()}",
//                                Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                    Log.e("UploadError", "Network error", t)
//                    Toast.makeText(this@PatientProfileActivity,
//                        "Network error: ${t.message}",
//                        Toast.LENGTH_SHORT).show()
//                }
//            })
//        } ?: Toast.makeText(this, "Image path not found", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun getRealPathFromURI(uri: Uri): String? {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
//            cursor.moveToFirst()
//            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
//        }
//        return null
//    }
//}






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

class PatientProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var cameraIcon: ImageView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_PERMISSIONS = 103
    private lateinit var currentPhotoPath: String

    private val userId: String by lazy {
        intent.getStringExtra("user_id") ?: "-1"
    }

    private val userRole: String by lazy {
        intent.getStringExtra("user_role") ?: "patient"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)
        initializeViews()
        loadProfileImage()
        setupClickListeners()

        val userEmail = intent.getStringExtra("user_email")

        if (!userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Email: $userEmail", Toast.LENGTH_SHORT).show()
            // You can also store it in a variable or display it on a TextView
        }

        val textViewHome = findViewById<TextView>(R.id.textViewHome)
        textViewHome.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()  // ✅ Goes back to previous screen
        }


//        val logout = findViewById<TextView>(R.id.Logout)
//        logout.setOnClickListener {
//            val intent = Intent(this, PatientLoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
        val logout = findViewById<TextView>(R.id.Logout)
        logout.setOnClickListener {
            // Get the shared preferences for session management
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

            // Clear the session data
            sharedPreferences.edit().clear().apply()

            // Redirect to the login activity and clear the back stack
            val intent = Intent(this, PatientLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val appointmentsTextView = findViewById<TextView>(R.id.Appointments)
        appointmentsTextView.setOnClickListener {
            val intent = Intent(this, PatientBookingAppointmentActivity::class.java)
            intent.putExtra("user_email", userEmail)
            startActivity(intent)
        }
//

        val profileTextView = findViewById<TextView>(R.id.ProfilePersonal)
        profileTextView.setOnClickListener {
            Log.d("ProfileClick", "Profile clicked, user_id: $userId") // Add this line
            val intent = Intent(this, PatientPersonalActivity::class.java)
            intent.putExtra("user_id", userId)
            Log.d("ProfileClick", "Starting PatientPersonalActivity with user_id: $userId")
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
        return "profile_image_${userRole}_$userId"
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
            val userIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)

            showProgress("Uploading image...")

            ApiClient.instance.uploadProfileImage(userIdPart, body).enqueue(object : Callback<UploadResponse> {
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
        val imageUrl = "http://192.168.24.116/smart_healthcare_app/patient_profiles/${uploadResponse.image}"

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
        // Implement your progress dialog here
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun dismissProgress() {
        // Dismiss your progress dialog here
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e("ProfileUpload", message)
    }

}