//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//
//class RequestApprovedActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_request_approved)
//
//        val btnContinue = findViewById<Button>(R.id.btnContinue)
//
//        btnContinue.setOnClickListener {
//            val intent = Intent(this, HospitalHomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }
//}
//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class RequestApprovedActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_request_approved)
//
//        // Get values from intent
//        val approvedBy = intent.getStringExtra("approved_by") ?: "Admin"
//        val approvalDate = intent.getStringExtra("approval_date") ?: "N/A"
//        val referenceId = intent.getStringExtra("reference_id") ?: "N/A"
//
//        // Set values to TextViews
//        findViewById<TextView>(R.id.tvApprovedBy).text = "Approved by: $approvedBy"
//        findViewById<TextView>(R.id.tvApprovalDate).text = "Approval Date: $approvalDate"
//        findViewById<TextView>(R.id.tvReferenceId).text = "Reference ID: $referenceId"
//
//        // Button click to go to hospital home/dashboard
//        val btnContinue = findViewById<Button>(R.id.btnContinue)
//        btnContinue.setOnClickListener {
//            val intent = Intent(this, HospitalHomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }
//}
//package com.saveetha.smarthealthcareapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class RequestApprovedActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_request_approved)
//
//        // Get values from intent
//        val approvedBy = intent.getStringExtra("approved_by") ?: "Admin"
//        val approvalDate = intent.getStringExtra("approval_date") ?: "N/A"
//        val referenceId = intent.getStringExtra("reference_id") ?: "N/A"
//
//        // Set values to TextViews
//        findViewById<TextView>(R.id.tvApprovedBy).text = "Approved by: $approvedBy"
//        findViewById<TextView>(R.id.tvApprovalDate).text = "Approval Date: $approvalDate"
//        findViewById<TextView>(R.id.tvReferenceId).text = "Reference ID: $referenceId"
//
//        // Button click to go to hospital home/dashboard
//        val btnContinue = findViewById<Button>(R.id.btnContinue)
//        btnContinue.setOnClickListener {
//            val intent = Intent(this, HospitalHomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }
//}
package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RequestApprovedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_approved)

        val approvedBy = intent.getStringExtra("approved_by") ?: "Admin"
        val approvalDate = intent.getStringExtra("approval_date") ?: "N/A"
        val referenceId = intent.getStringExtra("reference_id") ?: "N/A"

        findViewById<TextView>(R.id.tvApprovedBy).text = "Approved by: $approvedBy"
        findViewById<TextView>(R.id.tvApprovalDate).text = "Approval Date: $approvalDate"
        findViewById<TextView>(R.id.tvReferenceId).text = "Reference ID: $referenceId"

        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            val intent = Intent(this, HospitalHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}

