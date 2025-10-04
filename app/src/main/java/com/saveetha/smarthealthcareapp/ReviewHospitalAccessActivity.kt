//package com.saveetha.smarthealthcareapp
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class ReviewHospitalAccessActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_review_hospital_access)
//
//        // Get the AdminRequest object passed from the previous activity
//        val request = intent.getParcelableExtra<AdminRequest>("request_data")
//
//        // Check for null and display data
//        request?.let {
//            findViewById<TextView>(R.id.textHospitalName).text = it.hospitalName
//            findViewById<TextView>(R.id.textRegistrationNumber).text = it.registrationNumber
//            findViewById<TextView>(R.id.textAddress).text = it.hospitalAddress
//            findViewById<TextView>(R.id.textContact).text = it.contactNumber
//            findViewById<TextView>(R.id.textAdminName).text = it.adminName
//            findViewById<TextView>(R.id.textAdminEmail).text = it.adminEmail
//            findViewById<TextView>(R.id.textAdminPosition).text = it.adminPosition
//            findViewById<TextView>(R.id.textDepartment).text = it.department
//            findViewById<TextView>(R.id.textExpectedUsers).text = it.expectedUsers
//            findViewById<TextView>(R.id.textBriefDescription).text = it.briefDescription
//
//
//        }
//
//        // Approve and Reject actions
//        findViewById<Button>(R.id.btnApprove).setOnClickListener {
//            Toast.makeText(this, "Access Approved", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//
//        findViewById<Button>(R.id.btnReject).setOnClickListener {
//            Toast.makeText(this, "Access Rejected", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//    }
//}

//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class ReviewHospitalAccessActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_review_hospital_access)
//
//        // Get the AdminRequest object passed from the previous activity
//        val request = intent.getParcelableExtra<AdminRequest>("request_data")
//
//        // Check for null and display data
//        request?.let { req ->
//            findViewById<TextView>(R.id.textHospitalName).text = req.hospitalName
//            findViewById<TextView>(R.id.textRegistrationNumber).text = req.registrationNumber
//            findViewById<TextView>(R.id.textAddress).text = req.hospitalAddress
//            findViewById<TextView>(R.id.textContact).text = req.contactNumber
//            findViewById<TextView>(R.id.textAdminName).text = req.adminName
//            findViewById<TextView>(R.id.textAdminEmail).text = req.adminEmail
//            findViewById<TextView>(R.id.textAdminPosition).text = req.adminPosition
//            findViewById<TextView>(R.id.textDepartment).text = req.department
//            findViewById<TextView>(R.id.textExpectedUsers).text = req.expectedUsers
//            findViewById<TextView>(R.id.textBriefDescription).text = req.briefDescription
//
//            val BASE_URL = "http://192.168.24.116/smart_healthcare_app/uploads/"
//
//            findViewById<Button>(R.id.btnViewLicense).setOnClickListener {
//                openDocument(BASE_URL + req.licensePath)
//            }
//
//            findViewById<Button>(R.id.btnViewRegistrationCertificate).setOnClickListener {
//                openDocument(BASE_URL + req.registrationCertificatePath)
//            }
//
//            findViewById<Button>(R.id.btnViewAdditionalDocuments).setOnClickListener {
//                openDocument(BASE_URL + req.additionalDocumentsPath)
//            }
//        }
//
//
//        // Approve and Reject actions
//        findViewById<Button>(R.id.btnApprove).setOnClickListener {
//            Toast.makeText(this, "Access Approved", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//
//        findViewById<Button>(R.id.btnReject).setOnClickListener {
//            Toast.makeText(this, "Access Rejected", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//    }
//
//    /**
//     * Opens the document in an external app (like a browser or PDF viewer).
//     */
//    private fun openDocument(url: String?) {
//        if (url.isNullOrEmpty()) {
//            Toast.makeText(this, "No document available", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            data = Uri.parse(url)
//        }
//
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        } else {
//            Toast.makeText(this, "No app found to open this document", Toast.LENGTH_SHORT).show()
//        }
//    }
//}
//package com.saveetha.smarthealthcareapp
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class ReviewHospitalAccessActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_review_hospital_access)
//
//        // Get the AdminRequest object passed from the previous activity
//        val request = intent.getParcelableExtra<AdminRequest>("request_data")
//
//        // Check for null and display data
//        request?.let {
//            findViewById<TextView>(R.id.textHospitalName).text = "Hospital Name: ${it.hospitalName}"
//            findViewById<TextView>(R.id.textRegistrationNumber).text = "Registration Number: ${it.registrationNumber}"
//            findViewById<TextView>(R.id.textAddress).text = it.hospitalAddress
//            findViewById<TextView>(R.id.textContact).text = "Contact Number: ${it.contactNumber}"
//            findViewById<TextView>(R.id.textAdminName).text = "Admin Name: ${it.adminName}"
//            findViewById<TextView>(R.id.textAdminEmail).text = "Admin Email: ${it.adminEmail}"
//            findViewById<TextView>(R.id.textAdminPosition).text = "Admin Position: ${it.adminPosition}"
//            findViewById<TextView>(R.id.textDepartment).text = it.department
//            findViewById<TextView>(R.id.textExpectedUsers).text = it.expectedUsers
//            findViewById<TextView>(R.id.textBriefDescription).text = it.briefDescription
//        }
//
//        // Approve and Reject actions
//        findViewById<Button>(R.id.btnApprove).setOnClickListener {
//            Toast.makeText(this, "Access Approved", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//
//        findViewById<Button>(R.id.btnReject).setOnClickListener {
//            Toast.makeText(this, "Access Rejected", Toast.LENGTH_SHORT).show()
//            // Add your logic to update status in the backend
//            finish()
//        }
//    }
//}



//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.FileProvider
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.net.HttpURLConnection
//import java.net.URL
//
//class ReviewHospitalAccessActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_review_hospital_access)
//
//        val request = intent.getParcelableExtra<AdminRequest>("request_data")
//
//        request?.let {
//            findViewById<TextView>(R.id.textHospitalName).text = it.hospitalName
//            findViewById<TextView>(R.id.textRegistrationNumber).text = it.registrationNumber
//            findViewById<TextView>(R.id.textAddress).text = it.hospitalAddress
//            findViewById<TextView>(R.id.textContact).text = it.contactNumber
//            findViewById<TextView>(R.id.textAdminName).text = it.adminName
//            findViewById<TextView>(R.id.textAdminEmail).text = it.adminEmail
//            findViewById<TextView>(R.id.textAdminPosition).text = it.adminPosition
//            findViewById<TextView>(R.id.textDepartment).text = it.department
//            findViewById<TextView>(R.id.textExpectedUsers).text = it.expectedUsers
//            findViewById<TextView>(R.id.textBriefDescription).text = it.briefDescription
//
//            val licenseText = findViewById<TextView>(R.id.btnViewsLicense)
//            val regCertText = findViewById<TextView>(R.id.btnViewsRegistrationCertificate)
//            val addDocText = findViewById<TextView>(R.id.btnViewsAdditionalDocuments)
//
//            licenseText.text = it.licensePath ?: "No file uploaded"
//            regCertText.text = it.registrationCertificatePath ?: "No file uploaded"
//            addDocText.text = it.additionalDocumentsPath ?: "No file uploaded"
//
//            val BASE_URL = "http://192.168.154.116/smart_healthcare_app/"
//            val licenseUrl = it.licensePath?.let { path -> "$BASE_URL$path" }
//            val regCertUrl = it.registrationCertificatePath?.let { path -> "$BASE_URL$path" }
//            val addDocUrl = it.additionalDocumentsPath?.let { path -> "$BASE_URL$path" }
//
//            licenseText.setOnClickListener { openDocument(licenseUrl) }
//            regCertText.setOnClickListener { openDocument(regCertUrl) }
//            addDocText.setOnClickListener { openDocument(addDocUrl) }
//        }
//
//        findViewById<Button>(R.id.btnApprove).setOnClickListener {
//            Toast.makeText(this, "Access Approved", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//
//        findViewById<Button>(R.id.btnReject).setOnClickListener {
//            Toast.makeText(this, "Access Rejected", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//    }
//
//    private fun openDocument(url: String?) {
//        if (url.isNullOrEmpty()) {
//            Toast.makeText(this, "No document available", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val fileName = url.substringAfterLast("/")
//        downloadAndOpenFile(url, fileName)
//    }
//
//    private fun downloadAndOpenFile(fileUrl: String, fileName: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val url = URL(fileUrl)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.connect()
//
//                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@ReviewHospitalAccessActivity,
//                            "Server error: ${connection.responseCode}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    return@launch
//                }
//
//                val input = connection.inputStream
//                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                if (!downloadsDir.exists()) downloadsDir.mkdirs()
//                val file = File(downloadsDir, fileName)
//                val output = FileOutputStream(file)
//
//                val buffer = ByteArray(4096)
//                var count: Int
//                while (input.read(buffer).also { count = it } != -1) {
//                    output.write(buffer, 0, count)
//                }
//
//                output.flush()
//                output.close()
//                input.close()
//
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@ReviewHospitalAccessActivity, "Downloaded: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//                    openFile(file)
//                }
//
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@ReviewHospitalAccessActivity, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    private fun openFile(file: File) {
//        val uri: Uri = FileProvider.getUriForFile(
//            this,
//            "${packageName}.fileprovider",
//            file
//        )
//
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, getMimeTypeFromUrl(file.name))
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        }
//
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        } else {
//            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getMimeTypeFromUrl(url: String): String {
//        return when {
//            url.endsWith(".pdf", true) -> "application/pdf"
//            url.endsWith(".doc", true) -> "application/msword"
//            url.endsWith(".docx", true) -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//            url.endsWith(".jpg", true) || url.endsWith(".jpeg", true) -> "image/jpeg"
//            url.endsWith(".png", true) -> "image/png"
//            else -> "*/*"
//        }
//    }
//}


package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class ReviewHospitalAccessActivity : AppCompatActivity() {

    private var requestId: String? = null
    private val BASE_URL = "http://192.168.24.116/smart_healthcare_app/" // adjust as needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_hospital_access)

        val request = intent.getParcelableExtra<AdminRequest>("request_data")

        request?.let {
            requestId = it.id // assuming `id` is available in AdminRequest for backend tracking
            findViewById<TextView>(R.id.textHospitalName).text = it.hospitalName
            findViewById<TextView>(R.id.textRegistrationNumber).text = it.registrationNumber
            findViewById<TextView>(R.id.textAddress).text = it.hospitalAddress
            findViewById<TextView>(R.id.textContact).text = it.contactNumber
            findViewById<TextView>(R.id.textAdminName).text = it.adminName
            findViewById<TextView>(R.id.textAdminEmail).text = it.adminEmail
            findViewById<TextView>(R.id.textAdminPosition).text = it.adminPosition
            findViewById<TextView>(R.id.textDepartment).text = it.department
            findViewById<TextView>(R.id.textExpectedUsers).text = it.expectedUsers
            findViewById<TextView>(R.id.textBriefDescription).text = it.briefDescription

            findViewById<TextView>(R.id.btnViewsLicense).text = it.licensePath ?: "No file uploaded"
            findViewById<TextView>(R.id.btnViewsRegistrationCertificate).text = it.registrationCertificatePath ?: "No file uploaded"
            findViewById<TextView>(R.id.btnViewsAdditionalDocuments).text = it.additionalDocumentsPath ?: "No file uploaded"

            val licenseUrl = it.licensePath?.let { path -> "$BASE_URL$path" }
            val regCertUrl = it.registrationCertificatePath?.let { path -> "$BASE_URL$path" }
            val addDocUrl = it.additionalDocumentsPath?.let { path -> "$BASE_URL$path" }

            findViewById<TextView>(R.id.btnViewsLicense).setOnClickListener { openDocument(licenseUrl) }
            findViewById<TextView>(R.id.btnViewsRegistrationCertificate).setOnClickListener { openDocument(regCertUrl) }
            findViewById<TextView>(R.id.btnViewsAdditionalDocuments).setOnClickListener { openDocument(addDocUrl) }
        }

        findViewById<Button>(R.id.btnApprove).setOnClickListener {
            updateStatus("approved")
        }

        findViewById<Button>(R.id.btnReject).setOnClickListener {
            updateStatus("rejected")
        }
    }

    private fun openDocument(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "No document available", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = url.substringAfterLast("/")
        downloadAndOpenFile(url, fileName)
    }

    private fun downloadAndOpenFile(fileUrl: String, fileName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReviewHospitalAccessActivity, "Server error: ${connection.responseCode}", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val input = connection.inputStream
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val file = File(downloadsDir, fileName)
                val output = FileOutputStream(file)

                val buffer = ByteArray(4096)
                var count: Int
                while (input.read(buffer).also { count = it } != -1) {
                    output.write(buffer, 0, count)
                }

                output.flush()
                output.close()
                input.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReviewHospitalAccessActivity, "Downloaded: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                    openFile(file)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReviewHospitalAccessActivity, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openFile(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, getMimeTypeFromUrl(file.name))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeTypeFromUrl(url: String): String {
        return when {
            url.endsWith(".pdf", true) -> "application/pdf"
            url.endsWith(".doc", true) -> "application/msword"
            url.endsWith(".docx", true) -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            url.endsWith(".jpg", true) || url.endsWith(".jpeg", true) -> "image/jpeg"
            url.endsWith(".png", true) -> "image/png"
            else -> "*/*"
        }
    }

    /**
     * Updates the status in the backend
     */
    private fun updateStatus(status: String) {
        if (requestId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid request ID", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("${BASE_URL}update_request_status.php")
                val postData = "request_id=$requestId&status=$status&action_by=admin" // adjust action_by as needed

                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    doOutput = true
                    outputStream.write(postData.toByteArray())
                }

                val responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().readText()

                withContext(Dispatchers.Main) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this@ReviewHospitalAccessActivity, "Status updated: $status", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ReviewHospitalAccessActivity, "Failed: $responseMessage", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReviewHospitalAccessActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
