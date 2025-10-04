package com.saveetha.smarthealthcareapp.models

data class AppointmentRequest(
    val id: String,
    val doctor_name: String,
    val clinic_address: String,
    val date: String,
    val slot_time: String,
    val purpose: String,
    val patient_name: String,
    val contact_number: String,
    val status: String
)
