////////////////finallllllllllllllllllllllllllllllllllllllllllll
//package com.saveetha.smarthealthcareapp.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.model.Appointment
//
//class AppointmentsAdapter(private val appointments: List<Appointment>) :
//    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
//        val clinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
//        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
//        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
//        val status: TextView = itemView.findViewById(R.id.status)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_appointment, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val appointment = appointments[position]
//        holder.doctorName.text = "Dr. ${appointment.doctor_name}"
//        holder.clinicAddress.text = appointment.clinic_address
//        holder.dateTime.text = "${appointment.date}, ${appointment.slot_time}"
//        holder.purpose.text = appointment.purpose
//        holder.status.text = "Status: ${appointment.status}"
//    }
//
//    override fun getItemCount(): Int = appointments.size
//}

//package com.saveetha.smarthealthcareapp.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.RatingBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.model.Appointment
//import com.saveetha.smarthealthcareapp.models.GenericResponse
//import com.saveetha.smarthealthcareapp.network.ApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class AppointmentsAdapter(private val appointments: List<Appointment>) :
//    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
//        val clinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
//        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
//        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
//        val status: TextView = itemView.findViewById(R.id.status)
//
//        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
//        val tvRatingValue: TextView = itemView.findViewById(R.id.tvRatingValue)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_appointment, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val appointment = appointments[position]
//        holder.doctorName.text = "Dr. ${appointment.doctor_name}"
//        holder.clinicAddress.text = appointment.clinic_address
//        holder.dateTime.text = "${appointment.date}, ${appointment.slot_time}"
//        holder.purpose.text = appointment.purpose
//        holder.status.text = "Status: ${appointment.status}"
//
//        // Bind rating
//        val rating = appointment.rating ?: 0
//        holder.ratingBar.rating = rating.toFloat()
//        holder.tvRatingValue.text = rating.toString()
//
//        // ✅ Only allow rating if status is "confirmed"
//        val isRateable = appointment.status.equals("confirmed", ignoreCase = true)
//        holder.ratingBar.setIsIndicator(!isRateable) // true = read-only, false = interactive
//
//        if (isRateable) {
//            holder.ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
//                holder.tvRatingValue.text = newRating.toString()
//                submitRating(appointment.id, newRating, holder)
//            }
//        } else {
//            holder.ratingBar.setOnRatingBarChangeListener(null) // remove listener
//        }
//    }
//
//    override fun getItemCount(): Int = appointments.size
//
//    // 🔹 Function to submit rating to backend
//    private fun submitRating(appointmentId: String, rating: Float, holder: ViewHolder) {
//        val id = appointmentId.toIntOrNull()
//        if (id == null) {
//            Toast.makeText(holder.itemView.context, "Invalid appointment ID", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        ApiClient.instance.updateAppointmentRating(id, rating)
//            .enqueue(object : Callback<GenericResponse> {
//                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
//                    if (response.isSuccessful && response.body()?.success == true) {
//                        Toast.makeText(holder.itemView.context, "Rating updated", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(holder.itemView.context, "Failed to update rating", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
//                    Toast.makeText(holder.itemView.context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//}
/////////////////////////final///////////////
//package com.saveetha.smarthealthcareapp.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.RatingBar
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.snackbar.Snackbar
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.model.Appointment
//import com.saveetha.smarthealthcareapp.models.GenericResponse
//import com.saveetha.smarthealthcareapp.network.ApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class AppointmentsAdapter(private val appointments: List<Appointment>) :
//    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
//        val clinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
//        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
//        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
//        val status: TextView = itemView.findViewById(R.id.status)
//
//        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
//        val tvRatingValue: TextView = itemView.findViewById(R.id.tvRatingValue)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_appointment, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val appointment = appointments[position]
//        holder.doctorName.text = "Dr. ${appointment.doctor_name}"
//        holder.clinicAddress.text = appointment.clinic_address
//        holder.dateTime.text = "${appointment.date}, ${appointment.slot_time}"
//        holder.purpose.text = appointment.purpose
//        holder.status.text = "Status: ${appointment.status}"
//
//        // Bind rating
//        val rating = appointment.rating ?: 0
//        holder.ratingBar.rating = rating.toFloat()
//        holder.tvRatingValue.text = rating.toString()
//
//        // Only allow rating if status is "confirmed"
//        val isRateable = appointment.status.equals("confirmed", ignoreCase = true)
//        holder.ratingBar.setIsIndicator(!isRateable)
//
//        if (isRateable) {
//            holder.ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
//                holder.tvRatingValue.text = newRating.toString()
//                submitRating(appointment.id, newRating, holder)
//            }
//        } else {
//            holder.ratingBar.setOnRatingBarChangeListener(null)
//        }
//    }
//
//    override fun getItemCount(): Int = appointments.size
//
//    // Function to submit rating with stylish Snackbar feedback
//    private fun submitRating(appointmentId: String, rating: Float, holder: ViewHolder) {
//        val id = appointmentId.toIntOrNull()
//        if (id == null) {
//            Snackbar.make(holder.itemView, "Invalid appointment ID", Snackbar.LENGTH_SHORT)
//                .setBackgroundTint(0xFFB00020.toInt()) // Red background
//                .setTextColor(0xFFFFFFFF.toInt())
//                .show()
//            return
//        }
//
//        ApiClient.instance.updateAppointmentRating(id, rating)
//            .enqueue(object : Callback<GenericResponse> {
//                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
//                    if (response.isSuccessful && response.body()?.success == true) {
//                        Snackbar.make(holder.itemView, "Rating updated successfully!", Snackbar.LENGTH_SHORT)
//                            .setBackgroundTint(0xFF4CAF50.toInt()) // Green
//                            .setTextColor(0xFFFFFFFF.toInt())
//                            .show()
//                    } else {
//                        Snackbar.make(holder.itemView, "Failed to update rating", Snackbar.LENGTH_SHORT)
//                            .setBackgroundTint(0xFFF44336.toInt()) // Red
//                            .setTextColor(0xFFFFFFFF.toInt())
//                            .show()
//                    }
//                }
//
//                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
//                    Snackbar.make(holder.itemView, "Network error: ${t.message}", Snackbar.LENGTH_SHORT)
//                        .setBackgroundTint(0xFFFF9800.toInt()) // Orange
//                        .setTextColor(0xFFFFFFFF.toInt())
//                        .show()
//                }
//            })
//    }
//}
//////////////////////////////////////////////////////

package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.model.Appointment
import com.saveetha.smarthealthcareapp.models.GenericResponse
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val clinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
        val status: TextView = itemView.findViewById(R.id.status)

        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvRatingValue: TextView = itemView.findViewById(R.id.tvRatingValue)
        val etReview: EditText = itemView.findViewById(R.id.etReview)
        val btnSubmitReview: Button = itemView.findViewById(R.id.btnSubmitReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(view)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val appointment = appointments[position]
//        holder.doctorName.text = "Dr. ${appointment.doctor_name}"
//        holder.clinicAddress.text = appointment.clinic_address
//        holder.dateTime.text = "${appointment.date}, ${appointment.slot_time}"
//        holder.purpose.text = appointment.purpose
//        holder.status.text = "Status: ${appointment.status}"
//
//        // Bind rating
//        val rating = appointment.rating ?: 0f
//        holder.ratingBar.rating = rating
//        holder.tvRatingValue.text = rating.toString()
//
//        // Only allow rating/review if status is "confirmed"
//        val isRateable = appointment.status.equals("confirmed", ignoreCase = true)
//        holder.ratingBar.setIsIndicator(!isRateable)
//        holder.btnSubmitReview.isEnabled = isRateable
//        holder.etReview.isEnabled = isRateable
//
//        // Optional: Update rating text on change
//        if (isRateable) {
//            holder.ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
//                holder.tvRatingValue.text = newRating.toString()
//            }
//        } else {
//            holder.ratingBar.setOnRatingBarChangeListener(null)
//        }
//
//        // Submit button click
//        holder.btnSubmitReview.setOnClickListener {
//            val reviewText = holder.etReview.text.toString().trim()
//            val ratingValue = holder.ratingBar.rating
//            submitRatingAndReview(appointment.id, ratingValue, reviewText, holder)
//        }
//    }
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val appointment = appointments[position]
    holder.doctorName.text = "Dr. ${appointment.doctor_name}"
    holder.clinicAddress.text = appointment.clinic_address
    holder.dateTime.text = "${appointment.date}, ${appointment.slot_time}"
    holder.purpose.text = appointment.purpose
    holder.status.text = "Status: ${appointment.status}"

    // Bind rating
    val rating = appointment.rating ?: 0f
    holder.ratingBar.rating = rating
    holder.tvRatingValue.text = rating.toString()

    // Check if appointment is rateable
    val isConfirmed = appointment.status.equals("confirmed", ignoreCase = true)
    val hasRated = appointment.rating != null || !appointment.review.isNullOrEmpty()

    if (isConfirmed && !hasRated) {
        // Allow rating and review
        holder.ratingBar.setIsIndicator(false)
        holder.btnSubmitReview.isEnabled = true
        holder.etReview.isEnabled = true

        // Update rating text on change
        holder.ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
            holder.tvRatingValue.text = newRating.toString()
        }

    } else {
        // Disable rating/review
        holder.ratingBar.setIsIndicator(true)
        holder.btnSubmitReview.isEnabled = false
        holder.etReview.isEnabled = false
        holder.etReview.setText(appointment.review ?: "")
    }

    // Submit button click
    holder.btnSubmitReview.setOnClickListener {
        val reviewText = holder.etReview.text.toString().trim()
        val ratingValue = holder.ratingBar.rating
        submitRatingAndReview(appointment.id, ratingValue, reviewText, holder)

        // Disable after submission to prevent re-rating
        holder.ratingBar.setIsIndicator(true)
        holder.btnSubmitReview.isEnabled = false
        holder.etReview.isEnabled = false
    }
}

    override fun getItemCount(): Int = appointments.size

    private fun submitRatingAndReview(
        appointmentId: String,
        rating: Float,
        review: String,
        holder: ViewHolder
    ) {
        val id = appointmentId.toIntOrNull()
        if (id == null) {
            Snackbar.make(holder.itemView, "Invalid appointment ID", Snackbar.LENGTH_SHORT).show()
            return
        }

        ApiClient.instance.updateAppointmentRating(id, rating, review)
            .enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Snackbar.make(
                            holder.itemView,
                            "Rating & review submitted successfully!",
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(0xFF4CAF50.toInt())
                            .setTextColor(0xFFFFFFFF.toInt())
                            .show()
                    } else {
                        Snackbar.make(holder.itemView, "Failed to submit", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(0xFFF44336.toInt())
                            .setTextColor(0xFFFFFFFF.toInt())
                            .show()
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    Snackbar.make(
                        holder.itemView,
                        "Network error: ${t.message}",
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(0xFFFF9800.toInt())
                        .setTextColor(0xFFFFFFFF.toInt())
                        .show()
                }
            })
    }
}
