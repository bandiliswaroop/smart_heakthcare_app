package com.saveetha.smarthealthcareapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.models.Review

class ReviewsAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReviewerName: TextView = itemView.findViewById(R.id.tvReviewerName)
        val tvReviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        val tvDate: TextView = itemView.findViewById(R.id.tvReviewDate)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvReviewerName.text = review.patient_name
        holder.tvReviewText.text = review.review
        holder.tvDate.text = review.date
        holder.ratingBar.rating = review.rating
    }

    override fun getItemCount(): Int = reviews.size
}
