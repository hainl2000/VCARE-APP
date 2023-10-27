package com.example.vcare_app.api.api_model.response

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(val url: String, @SerializedName("fileName") val fileName: String)
