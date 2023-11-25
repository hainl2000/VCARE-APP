package com.example.vcare_app.present.booking.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.api.api_model.response.AppointmentResponse
import com.example.vcare_app.api.api_model.response.Profile
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AppointmentViewModel : BaseViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _createSuccess = MutableLiveData(false)

    val createSuccess: LiveData<Boolean> get() = _createSuccess

    var scheduleTime = ""

    private val repository = AppRepository(ApiClient.apiService)
    lateinit var appointmentDetail: AppointmentResponse
    fun getCurrentProfile() {
        compositeDisposable.add(repository.getUserProfile().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    _profile.postValue(it)
                }, {
                    status.postValue(LoadingStatus.Error)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }

    fun setErrorMsg(err: String) {
        errorMsg.postValue(err)
    }

    fun createAppointment(appointmentRequest: AppointmentRequest) {
        compositeDisposable.add(repository.createAppointment(appointmentRequest)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                status.postValue(LoadingStatus.Loading)
            }.subscribe(
                {
                    appointmentDetail = it
                    _createSuccess.postValue(true)
                    scheduleTime = it.timeInString
                    status.postValue(LoadingStatus.Success)
                    errorMsg.value = ""
                }, {
                    _createSuccess.postValue(false)
                    status.postValue(LoadingStatus.Error)
                    errorMsg.postValue(handleError(it))
                }
            ))
    }
}