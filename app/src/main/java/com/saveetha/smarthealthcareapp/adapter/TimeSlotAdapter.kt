package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.adapter.DoctorSlot

class TimeSlotAdapter(
    private val slots: List<DoctorSlot>,
    private val onSlotSelected: (DoctorSlot) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.SlotViewHolder>() {

    private var selectedPosition = -1

    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonSlot: Button = itemView.findViewById(R.id.buttonSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.buttonSlot.text = "${slot.time}\nRemaining Slot: ${slot.remaining}"

        holder.buttonSlot.setBackgroundResource(
            if (position == selectedPosition)
                R.drawable.bg_slot_selected else R.drawable.bg_slot_default
        )

        holder.buttonSlot.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = position
            notifyItemChanged(previous)
            notifyItemChanged(position)
            onSlotSelected(slot)
        }
    }

    override fun getItemCount(): Int = slots.size
}
