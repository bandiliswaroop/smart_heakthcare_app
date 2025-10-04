

package com.saveetha.smarthealthcareapp.models
//
import java.io.Serializable
//
//data class Hospital(
//    val id: Int,
//    val logo_path: String,
//    val name: String,
//    val about: String,
//    val specialties: String,
//    val facilities: String,
//    val technologies: String,
//    val address: String,
//    val contact: String,
//    val emergency_services: Int,
//    val latitude: Double,
//    val longitude: Double,
//    val technology_image_uris: List<String>?
//
//): Serializable


data class Hospital(
    val id: Int,
    val logo_path: String,
    val name: String,
    val about: String,
    val specialties: String,
    val facilities: String,
    val technologies: String,
    val address: String,
    val contact: String,
    val emergency_services: Int,
    val latitude: Double,
    val longitude: Double,
    val technology_image_uris: String? = null
) : Serializable
