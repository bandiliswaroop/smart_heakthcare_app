package com.saveetha.smarthealthcareapp.models

import com.saveetha.smarthealthcareapp.model.Appointment

data class AppointmentResponse(
    val success: Boolean,
    val appointments: List<Appointment>
)
