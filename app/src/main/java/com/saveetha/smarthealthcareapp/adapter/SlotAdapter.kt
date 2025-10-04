//package com.saveetha.smarthealthcareapp.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.saveetha.smarthealthcareapp.R
//import com.saveetha.smarthealthcareapp.adapter.DoctorSlot
//
//class SlotAdapter(
//    private val slots: List<DoctorSlot>,
//    private val onDeleteClick: (Int) -> Unit
//) : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {
//
//    class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val txtSlot: TextView = view.findViewById(R.id.txtSlotTime)
//        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteSlot)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_slot, parent, false)
//        return SlotViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
//        holder.txtSlot.text = slots[position].time
//        holder.btnDelete.setOnClickListener {
//            onDeleteClick(position)
//        }
//    }
//
//    override fun getItemCount(): Int = slots.size
//}


package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.model.DoctorSlot

class SlotAdapter(
    private val slotList: MutableList<com.saveetha.smarthealthcareapp.adapter.DoctorSlot>,
    private val onSlotSelected: (Int) -> Unit // Pass position instead of DoctorSlot
) : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slotList[position]
        holder.slotText.text = slot.time
        holder.card.setCardBackgroundColor(
            if (position == selectedPosition) 0xFF6200EE.toInt() else 0xFFFFFFFF.toInt()
        )
        holder.slotText.setTextColor(
            if (position == selectedPosition) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()
        )
        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onSlotSelected(position) // <-- send position
        }
    }

    override fun getItemCount() = slotList.size

    class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val slotText: TextView = view.findViewById(R.id.textSlot)
        val card: CardView = view.findViewById(R.id.cardSlot)
    }


}

