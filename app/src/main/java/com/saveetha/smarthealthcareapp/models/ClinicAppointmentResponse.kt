package com.saveetha.smarthealthcareapp.model

data class ClinicAppointmentResponse(
    val success: Boolean,
    val message: String,
    val appointments: List<ClinicAppointment>? = null
)
data class ClinicAppointmentItem(
    val id: String,
    val doctor_name: String,
    val clinic_address: String,
    val date: String,
    val slot_time: String,
    val purpose: String,
    val patient_name: String,
    val contact_number: String
)