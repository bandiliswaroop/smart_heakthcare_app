//package com.saveetha.smarthealthcareapp.adapters
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.HospitalPreviewActivity
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.models.Hospital
//
//class HospitalAdapter(
//    private val context: Context,
//    private val hospitals: List<Hospital>
//) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {
//
//    inner class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.findViewById(R.id.tvHospitalName)
//        val address: TextView = itemView.findViewById(R.id.tvHospitalAddress)
//        val btnDetails: LinearLayout = itemView.findViewById(R.id.btnViewDetails)
//        val btnDirection: LinearLayout = itemView.findViewById(R.id.btnViewDirection)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_hospital_card, parent, false)
//        return HospitalViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
//        val hospital = hospitals[position]
//
//        holder.name.text = hospital.name
//        holder.address.text = hospital.address
//
//        holder.btnDetails.setOnClickListener {
//            val intent = Intent(context, HospitalPreviewActivity::class.java)
//            intent.putExtra("hospital", hospital)  // ✅ Make sure Hospital implements Serializable
//            context.startActivity(intent)
//        }
//
//        holder.btnDirection.setOnClickListener {
//            val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(hospital.address))
//            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            if (mapIntent.resolveActivity(context.packageManager) != null) {
//                context.startActivity(mapIntent)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = hospitals.size
//}


package com.saveetha.smarthealthcareapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.HospitalPreviewActivity
import com.saveetha.smarthealthcareapp.PatientHospitalViewActivity
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.models.Hospital
import java.io.Serializable

class HospitalAdapter(
    private val context: Context,
    private var hospitals: MutableList<Hospital>
) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    inner class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvHospitalName)
        val address: TextView = itemView.findViewById(R.id.tvHospitalAddress)
        val btnDetails: LinearLayout = itemView.findViewById(R.id.btnViewDetails)
        val btnDirection: LinearLayout = itemView.findViewById(R.id.btnViewDirection)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital_card, parent, false)
        return HospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]

        holder.name.text = hospital.name
        holder.address.text = hospital.address

        holder.btnDetails.setOnClickListener {
            val intent = Intent(context, PatientHospitalViewActivity::class.java)
            intent.putExtra("hospital", hospital as Serializable) // ✅ Hospital must implement Serializable
            context.startActivity(intent)
        }


        holder.btnDirection.setOnClickListener {
            val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(hospital.address))
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            }
        }
    }

    override fun getItemCount(): Int = hospitals.size

    // ✅ Called from PatientHomeActivity to update the filtered list
    fun updateList(newList: List<Hospital>) {
        hospitals = newList.toMutableList()
        notifyDataSetChanged()
    }
}
