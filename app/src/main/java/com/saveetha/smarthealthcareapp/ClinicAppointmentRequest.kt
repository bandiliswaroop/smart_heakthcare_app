package com.saveetha.smarthealthcareapp

data class ClinicAppointmentRequest(
    val doctor_name: String,
    val clinic_address: String,
    val date: String,
    val time: String,
    val purpose: String = "Consultation",
    val patient_name: String,
    val patient_phone: String,
    val status: String = "pending"
)
