package com.saveetha.smarthealthcareapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.models.Specialty

class SpecialtyAdapter(private val specialties: MutableList<Specialty>) : RecyclerView.Adapter<SpecialtyAdapter.SpecialtyViewHolder>() {

    inner class SpecialtyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etSpecialty: EditText = itemView.findViewById(R.id.etSpecialty)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveSpecialty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialtyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_specialty, parent, false)
        return SpecialtyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecialtyViewHolder, position: Int) {
        val specialty = specialties[position]
        holder.etSpecialty.setText(specialty.name)

        holder.etSpecialty.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                specialties[holder.adapterPosition].name = holder.etSpecialty.text.toString()
            }
        }

        holder.btnRemove.setOnClickListener {
            specialties.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, specialties.size)
        }
    }

    override fun getItemCount(): Int = specialties.size
}
