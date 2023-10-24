package com.example.vcare_app.present.login.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignUpFragmentViewModel : BaseViewModel() {

    private val repository = AppRepository(ApiClient.apiService)

    fun signUp(email: String, phone: String, sin: String, idn: String, password: String) {
        status.postValue(LoadingStatus.Loading)
        compositeDisposable.add(repository.signUp(email, phone, sin, idn, password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    status.postValue(LoadingStatus.Success)
                    errorMsg.postValue("")
                }, {
                    errorMsg.postValue(handleError(it))
                    status.postValue(LoadingStatus.Error)
                }
            ))
    }
}