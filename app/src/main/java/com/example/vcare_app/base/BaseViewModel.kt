package com.example.vcare_app.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vcare_app.api.api_model.ErrorResponse
import com.example.vcare_app.utilities.LoadingStatus
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException

open class BaseViewModel(argument: Any? = null) : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val status = MutableLiveData(LoadingStatus.Initial)
    val errorMsg = MutableLiveData("")

    fun clearErrorMsg() {
        errorMsg.value = ""
    }

    fun handleError(error: Throwable): String {
        if (error is HttpException) {
            return try {
                val errorBody = error.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                Log.e("Error", errorResponse.toString())
                errorResponse.message.joinToString()
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                "Lỗi: ${e.message}"
            }
        }
        Log.e("Error", error.toString())
        return "Lỗi: Không xác định"
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}