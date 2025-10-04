package com.saveetha.smarthealthcareapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R

class PreviewSpecialtyAdapter(private val items: List<String>) :
    RecyclerView.Adapter<PreviewSpecialtyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSpecialty: TextView = view.findViewById(R.id.tvSpecialty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_specialty_display, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSpecialty.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
