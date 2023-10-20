package com.example.vcare_app.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("username") val userName:String, val password:String)