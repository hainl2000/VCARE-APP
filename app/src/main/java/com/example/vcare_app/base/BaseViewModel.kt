package com.example.vcare_app.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vcare_app.model.ErrorResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException
import kotlin.Exception

open class BaseViewModel(argument: Any? = null) : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    fun handleError(error: Throwable): String {
        if (error is HttpException) {
            return try {
                val errorBody = error.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                errorResponse.message.joinToString()
            } catch (e: Exception) {
                e.message ?: "Lỗi: Không xác định"
            }
        }
        return error.message ?: "Lỗi: Không xác định"
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}