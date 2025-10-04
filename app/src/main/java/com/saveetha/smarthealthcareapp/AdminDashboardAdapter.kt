package com.saveetha.smarthealthcareapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminDashboardAdapter(
    private val requestList: List<AdminRequest>,
    private val listener: OnActionClickListener
) : RecyclerView.Adapter<AdminDashboardAdapter.ViewHolder>() {

    interface OnActionClickListener {
        fun onApprove(request: AdminRequest)
        fun onReject(request: AdminRequest)
        fun onViewDetails(request: AdminRequest)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val tvDateRequested: TextView = itemView.findViewById(R.id.tvDateRequested)
        val btnApprove: Button = itemView.findViewById(R.id.btnApprove)
        val btnReject: Button = itemView.findViewById(R.id.btnReject)
        val tvViewDetails: TextView = itemView.findViewById(R.id.tvViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_request_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = requestList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]

        holder.tvHospitalName.text = request.hospitalName
        holder.tvDateRequested.text = "🕒 ${request.submittedAt}"

        holder.btnApprove.setOnClickListener {
            listener.onApprove(request)
        }
        holder.btnReject.setOnClickListener {
            listener.onReject(request)
        }
        holder.tvViewDetails.setOnClickListener {
            listener.onViewDetails(request)
        }
    }
}
