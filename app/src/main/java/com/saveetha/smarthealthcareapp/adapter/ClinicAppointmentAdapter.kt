//package com.saveetha.smarthealthcareapp.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.model.ClinicAppointment
//
//class ClinicAppointmentAdapter(
//    private val appointmentList: List<ClinicAppointment>
//) : RecyclerView.Adapter<ClinicAppointmentAdapter.AppointmentViewHolder>() {
//
//    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val patientName: TextView = itemView.findViewById(R.id.tvPatientName)
//        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
//        val slotTime: TextView = itemView.findViewById(R.id.tvSlotTime)
//        val date: TextView = itemView.findViewById(R.id.tvDate)
//        val contact: TextView = itemView.findViewById(R.id.tvContact)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_clinic_appointment, parent, false)
//        return AppointmentViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
//        val appointment = appointmentList[position]
//        holder.patientName.text = "Patient: ${appointment.patient_name}"
//        holder.purpose.text = "Purpose: ${appointment.purpose}"
//        holder.slotTime.text = "Time: ${appointment.slot_time}"
//        holder.date.text = "Date: ${appointment.date}"
//        holder.contact.text = "Contact: ${appointment.contact_number}"
//    }
//
//    override fun getItemCount(): Int = appointmentList.size
//}

package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.model.ClinicAppointment

class ClinicAppointmentAdapter(
    private val appointmentList: MutableList<ClinicAppointment>,
    private val onStatusUpdate: (appointment: ClinicAppointment, newStatus: String) -> Unit
) : RecyclerView.Adapter<ClinicAppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val purpose: TextView = itemView.findViewById(R.id.tvPurpose)
        val slotTime: TextView = itemView.findViewById(R.id.tvSlotTime)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val contact: TextView = itemView.findViewById(R.id.tvContact)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val approveButton: ImageView = itemView.findViewById(R.id.ivApprove)
        val rejectButton: ImageView = itemView.findViewById(R.id.ivReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clinic_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]

        holder.patientName.text = "Patient: ${appointment.patient_name}"
        holder.purpose.text = "Purpose: ${appointment.purpose}"
        holder.slotTime.text = "Time: ${appointment.slot_time}"
        holder.date.text = "Date: ${appointment.date}"
        holder.contact.text = "Contact: ${appointment.contact_number}"

        // Set status color and buttons
        // In your onBindViewHolder method, update the when statement:
        when (appointment.status?.lowercase()) {
            "approved", "confirmed" -> { // Add "confirmed" here
                holder.status.text = "Status: Approved"
                holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                holder.approveButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE
            }
            "rejected" -> {
                holder.status.text = "Status: Rejected"
                holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                holder.approveButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE
            }
            else -> {
                holder.status.text = "Status: Pending"
                holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.amber))
                holder.approveButton.visibility = View.VISIBLE
                holder.rejectButton.visibility = View.VISIBLE
            }
        }

        holder.approveButton.setOnClickListener {
            onStatusUpdate(appointment, "approved")
        }

        holder.rejectButton.setOnClickListener {
            onStatusUpdate(appointment, "rejected")
        }
    }

    override fun getItemCount(): Int = appointmentList.size

    // Update status locally and refresh UI
    fun updateAppointmentStatusLocally(appointment: ClinicAppointment, newStatus: String) {
        val index = appointmentList.indexOfFirst {
            it.patient_name == appointment.patient_name &&
                    it.date == appointment.date &&
                    it.slot_time == appointment.slot_time
        }
        if (index != -1) {
            // For UI display, use "approved" instead of "confirmed"
            val displayStatus = if (newStatus == "confirmed") "approved" else newStatus
            appointmentList[index].status = displayStatus
            notifyItemChanged(index)
        }
    }
}
