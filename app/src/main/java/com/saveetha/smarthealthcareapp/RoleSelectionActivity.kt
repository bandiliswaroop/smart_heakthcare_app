package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class RoleSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_selection)

        findViewById<LinearLayout>(R.id.btn_patient).setOnClickListener {
            openRegisterActivity("patient")
        }

        findViewById<LinearLayout>(R.id.btn_doctor).setOnClickListener {
            openRegisterActivity("doctor")
        }

        findViewById<LinearLayout>(R.id.btn_hospital).setOnClickListener {
            openRegisterActivity("hospital")
        }
    }

    private fun openRegisterActivity(role: String) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("ROLE", role)
        startActivity(intent)
    }
}
