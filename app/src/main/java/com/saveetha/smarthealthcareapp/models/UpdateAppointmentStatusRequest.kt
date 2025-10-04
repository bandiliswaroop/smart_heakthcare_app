package com.saveetha.smarthealthcareapp.models

// UpdateAppointmentStatusRequest.kt
data class UpdateAppointmentStatusRequest(
    val doctor_name: String,
    val patient_name: String,
    val date: String,
    val slot_time: String,
    val status: String
)