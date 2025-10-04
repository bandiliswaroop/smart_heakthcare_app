package com.saveetha.smarthealthcareapp.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.adapter.ReviewsAdapter
import com.saveetha.smarthealthcareapp.models.ReviewsResponse
import com.saveetha.smarthealthcareapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDoctorName: TextView
    private lateinit var tvReviewCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        recyclerView = findViewById(R.id.recyclerReviews)
        tvDoctorName = findViewById(R.id.tvDoctorName)
        tvReviewCount = findViewById(R.id.tvReviewCount)

        val doctorName = intent.getStringExtra("doctor_name")
        tvDoctorName.text = "Reviews for Dr. $doctorName"

        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!doctorName.isNullOrEmpty()) {
            ApiClient.instance.getReviews(doctorName)
                .enqueue(object : Callback<ReviewsResponse> {
                    override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            val reviews = response.body()?.reviews ?: emptyList()
                            // NEW: Update the review count text
                            val count = reviews.size
                            tvReviewCount.text = "$count ${if (count == 1) "Review" else "Reviews"}"
                            recyclerView.adapter = ReviewsAdapter(reviews)
                        } else {
                            // NEW: Update count for no reviews
                            tvReviewCount.text = "No reviews found"
                            Toast.makeText(this@ReviewsActivity, "No reviews found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                        // NEW: Update count on network error
                        tvReviewCount.text = "Failed to load reviews"
                        Toast.makeText(this@ReviewsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
