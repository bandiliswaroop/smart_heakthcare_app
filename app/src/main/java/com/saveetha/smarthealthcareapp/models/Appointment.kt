package com.saveetha.smarthealthcareapp.model

data class Appointment(
    val id: String,
    val doctor_name: String,
    val clinic_address: String,
    val date: String,
    val slot_time: String,
    val purpose: String,
    val patient_name: String,
    val contact_number: String,
    val status: String,
    var rating: Float? = null,
    var review: String? = null,
    var ratingCount: Int? = 0,
    val doctor_image: String? = null // Make sure this field exists
)