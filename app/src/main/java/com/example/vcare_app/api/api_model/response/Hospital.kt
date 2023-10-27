package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class Hospital(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("image") val image: String?, // Replace String with the actual type if image is not a string
    @SerializedName("information") val information: String?, // Replace String with the actual type if information is not a string
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("created_by_id") val createdById: Int,
    @SerializedName("created_at") val createdAt: String
)
