//package com.saveetha.smarthealthcareapp.model
//
//data class Clinic(
//    val id: Int,
//    val doctor_name: String,
//    val specialization: String,
//    val clinic_name: String,
//    val address: String,
//    val contact: String,
//    val state: String,
//    val district: String,
//    val division: String,
//    val mandal: String,
//    val gp: String,
//    val slots: String
//)


package com.saveetha.smarthealthcareapp.model

data class Clinic(
    val doctor_name: String,
    val specialization: String,
    val clinic_name: String,
    val clinic_address: String,
    val contact_number: String,
    val profile_pic: String?,
    val appointment_count: Int,
    val doctor_rating: Float,
    val review_count: Int
)
