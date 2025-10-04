// DoctorSlotRequest.kt
package com.saveetha.smarthealthcareapp.api  // or your package name

import com.saveetha.smarthealthcareapp.adapter.DoctorSlot

data class DoctorSlotRequest(
    val user_id: Int,
    val doctor_name: String,
    val specialization: String,
    val clinic_name: String,
    val clinic_address: String,
    val contact_number: String,
    val date: String,
    val language: String,
    val slots: List<DoctorSlot>
)
