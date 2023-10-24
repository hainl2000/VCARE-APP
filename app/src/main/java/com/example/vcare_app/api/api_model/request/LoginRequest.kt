package com.example.vcare_app.api.api_model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("username") val userName:String, val password:String)