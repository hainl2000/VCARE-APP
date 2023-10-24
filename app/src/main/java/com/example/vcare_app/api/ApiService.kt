package com.example.vcare_app.api

import com.example.vcare_app.api.api_model.request.LoginRequest
import com.example.vcare_app.api.api_model.request.SignUpRequest
import com.example.vcare_app.api.api_model.response.DepartmentResponse
import com.example.vcare_app.api.api_model.response.LoginResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("auth/register")
    fun registerUser(@Body signUpRequest: SignUpRequest): Single<LoginResponse>

    @GET("department")
    fun getDepartmentList(): Single<DepartmentResponse>
}