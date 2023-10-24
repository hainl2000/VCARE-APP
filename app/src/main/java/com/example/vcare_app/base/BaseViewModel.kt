package com.example.vcare_app.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vcare_app.api.api_model.ErrorResponse
import com.example.vcare_app.utilities.LoadingStatus
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException
import kotlin.Exception

open class BaseViewModel(argument: Any? = null) : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val status = MutableLiveData(LoadingStatus.Initial)
    val errorMsg = MutableLiveData("")

    fun handleError(error: Throwable): String {
        if (error is HttpException) {
            return try {
                val errorBody = error.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                Log.e("Error", errorResponse.message.toString())
                errorResponse.message.joinToString()

            } catch (e: Exception) {
                "Lỗi: Không xác định"
            }
        }
        return "Lỗi: Không xác định"
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}