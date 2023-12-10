package com.example.vcare_app.present.personal.healthstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.HealthStatus
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HealthStatusViewModel : BaseViewModel() {
    val repository = AppRepository(apiService = ApiClient.apiService)
    private val healthStatus = MutableLiveData<HealthStatus>()
    val _healthStatus:LiveData<HealthStatus> get() = healthStatus
    fun getHealthStatus(){
        compositeDisposable.add(repository.getHealthStatus().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            status.value = LoadingStatus.Loading
        }.subscribe(
            {
                healthStatus.value = it
                status.value = LoadingStatus.Success
                errorMsg.value = ""
            },{
                errorMsg.value = handleError(it)
                status.value = LoadingStatus.Error
            }
        ))
    }
}