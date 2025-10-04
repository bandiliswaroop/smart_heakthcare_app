package com.saveetha.smarthealthcareapp.models

data class ReviewsResponse(
    val success: Boolean,
    val reviews: List<Review>?
)
