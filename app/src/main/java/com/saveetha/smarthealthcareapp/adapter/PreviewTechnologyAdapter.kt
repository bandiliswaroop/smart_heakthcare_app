package com.saveetha.smarthealthcareapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.models.TechnologyItem

class PreviewTechnologyAdapter(private val items: List<TechnologyItem>) :
    RecyclerView.Adapter<PreviewTechnologyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgTech: ImageView = view.findViewById(R.id.imgTechnologyPreview)
        val tvTechName: TextView = view.findViewById(R.id.tvTechnologyName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_technology_preview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tech = items[position]
        holder.tvTechName.text = tech.name
//        Glide.with(holder.itemView.context)
//            .load(Uri.parse(tech.imageUri))
//            .placeholder(R.drawable.ic_placeholder_image)
//            .into(holder.imgTech)
        Glide.with(holder.itemView.context)
            .load(tech.imageUri.trim())
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_placeholder_image)
            .into(holder.imgTech)


    }

    override fun getItemCount(): Int = items.size
}




