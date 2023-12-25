package com.example.vcare_app.api

import com.example.vcare_app.api.api_model.HealthStatus
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.api.api_model.request.LoginRequest
import com.example.vcare_app.api.api_model.request.PostPatientProfileRequest
import com.example.vcare_app.api.api_model.request.SignUpRequest
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.api.api_model.response.AppointmentDetailResponse
import com.example.vcare_app.api.api_model.response.AppointmentResponse
import com.example.vcare_app.api.api_model.response.DepartmentResponse
import com.example.vcare_app.api.api_model.response.GetPatientProfileResponse
import com.example.vcare_app.api.api_model.response.HistoryResponse
import com.example.vcare_app.api.api_model.response.HospitalListResponse
import com.example.vcare_app.api.api_model.response.LoginResponse
import com.example.vcare_app.api.api_model.response.PostPatientProfileResponse
import com.example.vcare_app.api.api_model.response.Profile
import com.example.vcare_app.api.api_model.response.UploadFileResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("auth/register")
    fun registerUser(@Body signUpRequest: SignUpRequest): Single<LoginResponse>

    @GET("department")
    fun getDepartmentList(@Query("hospital_id") hospitalId: Int): Single<DepartmentResponse>

    @GET("user/profile")
    fun getUserProfile(): Single<Profile>

    @PUT("user")
    fun updateUserProfile(@Body profile: UpdateUserRequest): Single<Profile>

    @GET("hospital")
    fun getHospitalList(
        @Query("pageSize") pageSize: Int,
        @Query("pageIndex") pageIndex: Int
    ): Observable<HospitalListResponse>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part file: MultipartBody.Part): Single<UploadFileResponse>

    @POST("appointment")
    fun createAppointment(@Body appointmentRequest: AppointmentRequest): Single<AppointmentResponse>

    @GET("appointment/detail/{id}")
    fun getAppointment(@Path("id") id: Int): Single<AppointmentDetailResponse>

    @GET("appointment")
    fun getAppointmentHistory(
        @Query("pageSize") pageSize: Int,
        @Query("pageIndex") pageIndex: Int
    ): Single<HistoryResponse>

    @POST("health-status")
    fun updateHealthStatus(@Body healthStatus: HealthStatus): Single<HealthStatus>

    @GET("health-status")
    fun getHealthStatus(): Single<HealthStatus>

    @GET("user/profile-patient")
    fun getPatientProfile(): Single<GetPatientProfileResponse>

    @POST("user/profile-patient")
    fun postPatientProfile(@Body postPatientProfileRequest: PostPatientProfileRequest): Single<PostPatientProfileResponse>
}