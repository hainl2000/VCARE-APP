package com.example.vcare_app.present.personal.edithealthstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.HealthStatus
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class EditHealthStatusViewModel : BaseViewModel() {
    val repository = AppRepository(ApiClient.apiService)
    private val healthStatus = MutableLiveData<HealthStatus>()
    val _healthStatus: LiveData<HealthStatus> get() = healthStatus

    private val updateStatus = MutableLiveData<Boolean>()
    val _updateStatus: LiveData<Boolean> get() = updateStatus
    fun getHealthStatus() {
        compositeDisposable.add(repository.getHealthStatus().subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()
        ).doOnSubscribe {
            status.value = LoadingStatus.Loading
        }.subscribe(
            {
                healthStatus.value = it
                status.value = LoadingStatus.Success
                errorMsg.value = ""
            }, {
                errorMsg.value = handleError(it)
                status.value = LoadingStatus.Error
            }
        ))
    }

    fun updateHealthStatus(healthStatus: HealthStatus) {
        compositeDisposable.add(repository.updateHealthStatus(healthStatus)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
            status.value = LoadingStatus.Loading
        }.subscribe(
            {
                updateStatus.value = true
                errorMsg.value = ""
                status.value = LoadingStatus.Success
            }, {
                errorMsg.value = handleError(it)
                status.value = LoadingStatus.Error
            }
        ))
    }
}