package com.example.vcare_app.present.booking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.Department
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookingFragmentViewModel : BaseViewModel() {
    private val repository = AppRepository(apiService = ApiClient.apiService)
    private val _listDepartment = MutableLiveData<List<Department>>(emptyList())
    val listDepartment: LiveData<List<Department>> get() = _listDepartment
    fun getDepartmentList() {
        compositeDisposable.add(
            repository.getDepartmentList().subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.postValue(LoadingStatus.Loading)
                }
                .subscribe(
                    {
                        status.postValue(LoadingStatus.Success)
                        _listDepartment.postValue(it.data)
                    }, {
                        Log.e("ERROR", it.message.toString())
                        status.postValue(LoadingStatus.Error)
                        errorMsg.postValue(handleError(it))
                    }
                )

        )
    }
}