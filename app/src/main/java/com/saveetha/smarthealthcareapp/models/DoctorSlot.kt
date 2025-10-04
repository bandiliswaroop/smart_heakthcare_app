package com.saveetha.smarthealthcareapp.model

data class DoctorSlot(
    val time: String,          // Format: "HH:MM AM/PM - HH:MM AM/PM"
    val remaining: Int = 0       // Available slots count
)