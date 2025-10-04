package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.model.HospitalRequest

class HospitalRequestAdapter(private val requestList: List<HospitalRequest>) :
    RecyclerView.Adapter<HospitalRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hospitalName: TextView = view.findViewById(R.id.hospitalName)
        val status: TextView = view.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]
        holder.hospitalName.text = request.hospitalName
        holder.status.text = request.status
    }

    override fun getItemCount(): Int = requestList.size
}