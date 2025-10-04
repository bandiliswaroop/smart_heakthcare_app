package com.saveetha.smarthealthcareapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.PatientClinicBookingActivity
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.model.Clinic
import com.saveetha.smarthealthcareapp.ui.ReviewsActivity

class ClinicAdapter(private val clinicList: List<Clinic>) :
    RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder>() {

    class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val specialization: TextView = itemView.findViewById(R.id.tvHospitalType)
        val clinicName: TextView = itemView.findViewById(R.id.tvClinicName)
        val clinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
        val contact: TextView = itemView.findViewById(R.id.tvContact)
        val fullAddress: TextView = itemView.findViewById(R.id.tvFullAddress)
        val bookNow: TextView = itemView.findViewById(R.id.tvBookNow)
        val profileImage: ImageView = itemView.findViewById(R.id.imgProfile)
        val appointmentCount: TextView = itemView.findViewById(R.id.tvAppointmentCount)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvRatingValue: TextView = itemView.findViewById(R.id.tvRatingValue)
        val tvReviewCount: TextView = itemView.findViewById(R.id.tvReviewCount)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clinic, parent, false)
        return ClinicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinic = clinicList[position]
        holder.doctorName.text = "Dr. ${clinic.doctor_name}"
        holder.tvReviewCount.text = "(${clinic.review_count} Reviews)"

        holder.specialization.text = clinic.specialization
//        holder.clinicName.text = clinic.clinic_name
        holder.clinicName.text = "🏥 ${clinic.clinic_name}"
        holder.clinicAddress.text = clinic.clinic_address
//        holder.contact.text = clinic.contact_number
        holder.contact.text = "📞 ${clinic.contact_number}   📹 Video"
        holder.appointmentCount.text = "${clinic.appointment_count} Consultation"
        holder.ratingBar.rating = clinic.doctor_rating
        holder.tvRatingValue.text = String.format("%.1f", clinic.doctor_rating)




        val baseUrl = "http://192.168.24.116/smart_healthcare_app/doctor_profiles/"

        // ✅ Build full image URL
        val profileUrl = if (clinic.profile_pic.isNullOrEmpty()) {
            null
        } else {
            baseUrl + clinic.profile_pic
        }

        // ✅ Load with Glide (round crop + fallback)
        Glide.with(holder.itemView.context)
            .load(profileUrl ?: R.drawable.ic_hospital)
            .circleCrop()
            .into(holder.profileImage)

        val openMap: (String) -> Unit = { address ->
            val context = holder.itemView.context
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + Uri.encode(address))
            )
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            }
        }
        holder.tvReviewCount.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReviewsActivity::class.java)

            // Pass the doctor info to ReviewsActivity
            intent.putExtra("doctor_name", clinic.doctor_name)

            context.startActivity(intent)
        }


        holder.clinicAddress.setOnClickListener {
            openMap(clinic.clinic_address)
        }

        holder.fullAddress?.setOnClickListener {
            openMap(clinic.clinic_address)
        }
        // ✅ Popup for call options
        holder.contact.setOnClickListener {
            val context = holder.itemView.context
            val phoneNumber = clinic.contact_number.replace(" ", "").replace("+", "")

            val options = arrayOf("WhatsApp Video Call", "Google Duo Call", "Normal Call")

            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Choose Call Option")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> { // WhatsApp
                            try {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://wa.me/$phoneNumber")
                                    setPackage("com.whatsapp")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "WhatsApp not installed", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> { // Duo
                            try {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("duo://call/?number=$phoneNumber")
                                    setPackage("com.google.android.apps.tachyon")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "Google Duo not installed", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> { // Normal Phone Call
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                            context.startActivity(intent)
                        }
                    }
                }
                .show()
        }
        holder.bookNow.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PatientClinicBookingActivity::class.java)

            // Optional: Pass clinic info if needed
            intent.putExtra("doctor_name", clinic.doctor_name)
            intent.putExtra("clinic_name", clinic.clinic_name)
            intent.putExtra("clinic_address", clinic.clinic_address)
            intent.putExtra("contact_number", clinic.contact_number)

            context.startActivity(intent)
        }

    }


    override fun getItemCount() = clinicList.size
}
