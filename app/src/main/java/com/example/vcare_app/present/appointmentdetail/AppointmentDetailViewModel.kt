package com.example.vcare_app.present.appointmentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.response.AppointmentDetailResponse
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AppointmentDetailViewModel : BaseViewModel() {
    val repository = AppRepository(ApiClient.apiService)
    private val _appointmentDetailResponse = MutableLiveData<AppointmentDetailResponse>()
    val appointmentDetailResponse: LiveData<AppointmentDetailResponse> get() = _appointmentDetailResponse
    fun getAppointmentDetail(id: Int) {
        compositeDisposable.add(repository.getAppointmentDetail(id)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    _appointmentDetailResponse.postValue(it)
                }, {
                    errorMsg.postValue(handleError(it))
                    status.postValue(LoadingStatus.Error)
                }
            ))
    }
}