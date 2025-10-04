package com.saveetha.smarthealthcareapp.model

import org.json.JSONObject

data class HospitalRequest(
    val id: String,
    val hospitalName: String,
    val status: String
) {
    companion object {
        fun fromJson(obj: JSONObject): HospitalRequest {
            return HospitalRequest(
                id = obj.optString("id"),
                hospitalName = obj.optString("hospital_name"),
                status = obj.optString("status")
            )
        }
    }
}
