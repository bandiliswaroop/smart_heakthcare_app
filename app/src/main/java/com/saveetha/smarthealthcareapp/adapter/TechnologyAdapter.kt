//package com.saveetha.smarthealthcareapp.adapters
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.models.Technology
//
//class TechnologyAdapter(
//    private val technologies: MutableList<Technology>,
//    private val activity: Activity
//) : RecyclerView.Adapter<TechnologyAdapter.TechViewHolder>() {
//
//    inner class TechViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val etTechName: EditText = itemView.findViewById(R.id.etTechName)
//        val imgTech: ImageView = itemView.findViewById(R.id.imgTech)
//        val btnPickImage: ImageView = itemView.findViewById(R.id.btnPickTechImage)
//        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveTech)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_technology, parent, false)
//        return TechViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
//        val tech = technologies[position]
//        holder.etTechName.setText(tech.name)
//
//        // Set image only if not null
//        tech.imageUri?.let {
//            holder.imgTech.setImageURI(it)
//        }
//
//        holder.btnPickImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            activity.startActivityForResult(intent, position + 2000) // Unique request code
//        }
//
//        holder.etTechName.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                val newName = holder.etTechName.text.toString()
//                if (position in technologies.indices) {
//                    technologies[position].name = newName
//                }
//            }
//        }
//
//        holder.btnRemove.setOnClickListener {
//            technologies.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, technologies.size)
//        }
//    }
//
//    override fun getItemCount(): Int = technologies.size
//
//    fun updateTechnologyImage(position: Int, uri: Uri) {
//        if (position in technologies.indices) {
//            technologies[position].imageUri = uri
//            notifyItemChanged(position)
//        }
//    }
//}
package com.saveetha.smarthealthcareapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.models.Technology

class TechnologyAdapter(
    private val technologies: MutableList<Technology>,
    private val onPickImage: (position: Int) -> Unit
) : RecyclerView.Adapter<TechnologyAdapter.TechViewHolder>() {

    inner class TechViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etTechName: EditText = itemView.findViewById(R.id.etTechName)
        val imgTech: ImageView = itemView.findViewById(R.id.imgTech)
        val btnPickImage: ImageView = itemView.findViewById(R.id.btnPickTechImage)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveTech)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_technology, parent, false)
        return TechViewHolder(view)
    }

    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        val tech = technologies[holder.adapterPosition]

        holder.etTechName.setText(tech.name)
        holder.imgTech.setImageURI(tech.imageUri)

        holder.btnPickImage.setOnClickListener {
            onPickImage(holder.adapterPosition)
        }

        holder.etTechName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                technologies[holder.adapterPosition].name = holder.etTechName.text.toString()
            }
        }

        holder.btnRemove.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION && technologies.size > 1) {
                technologies.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, technologies.size)
            }
        }
    }

    override fun getItemCount(): Int = technologies.size

    fun updateTechnologyImage(position: Int, uri: Uri) {
        if (position in technologies.indices) {
            technologies[position].imageUri = uri
            notifyItemChanged(position)
        }
    }
}

