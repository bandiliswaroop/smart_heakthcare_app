package com.saveetha.smarthealthcareapp.models

data class BookAppointmentRequest(
    val name: String,
    val email: String,
    val phone: String,
    val date: String,
    val message: String,
    val doctor: Int,
    val hospital: Int
)
