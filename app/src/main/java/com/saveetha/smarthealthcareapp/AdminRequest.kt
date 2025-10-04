package com.saveetha.smarthealthcareapp

//data class AccessRequest(
//    val id: String,
//    val hospitalName: String,
//    val createdAt: String,
//    val status: String = "Pending"
//)
//data class AdminRequest(
//    val id: String,
//    val hospitalName: String,
//    val createdAt: String,
//    val status: String
//)
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class AdminRequest(
    val id: String,
    val hospitalName: String,
    val registrationNumber: String,
    val hospitalAddress: String,
    val contactNumber: String,
    val adminName: String,
    val adminEmail: String,
    val adminPosition: String,
    val department: String,
    val expectedUsers: String,
    val briefDescription: String,
    val licensePath: String?,                     // 🔥 FIXED
    val registrationCertificatePath: String?,     // 🔥 FIXED
    val additionalDocumentsPath: String?,         // 🔥 FIXED
    val agreedTerms: String,
    val submittedAt: String
) : Parcelable






