package com.example.vcare_app.login.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignUpFragmentViewModel : BaseViewModel() {
    private val _errorMsg = MutableLiveData("")
    val error: LiveData<String> get() = _errorMsg
    private val _loadingStatus = MutableLiveData(LoadingStatus.Initial)
    val loadingStatus: LiveData<LoadingStatus> get() = _loadingStatus

    private val repository = AppRepository(ApiClient.apiService)

    fun signUp(email: String, phone: String, sin: String, idn: String, password: String) {
        _loadingStatus.postValue(LoadingStatus.Loading)
        compositeDisposable.add(repository.signUp(email, phone, sin, idn, password)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                _loadingStatus.postValue(LoadingStatus.Success)
                _errorMsg.postValue("")

            }, {
                _errorMsg.postValue(handleError(it))
                _loadingStatus.postValue(LoadingStatus.Error)
            }
        ))
    }
}