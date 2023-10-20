package com.example.vcare_app.login.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.repository.AppRepository
import com.example.vcare_app.repository.CurrentUser
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignInFragmentViewModel:BaseViewModel() {
    private val _errorMsg = MutableLiveData("")
    val error: LiveData<String> get() = _errorMsg
    private val _loadingStatus = MutableLiveData(LoadingStatus.Initial)
    val loadingStatus: LiveData<LoadingStatus> get() = _loadingStatus

    private val repository = AppRepository(ApiClient.apiService)
    fun login(userName: String, password: String) {
        _loadingStatus.postValue(LoadingStatus.Loading)
        compositeDisposable.add(repository.loginUser(userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _errorMsg.postValue("")
                    CurrentUser.setUserInformation(response.profile)
                    _loadingStatus.postValue(LoadingStatus.Success)
                }, { error ->
                    _errorMsg.postValue(handleError(error))
                    _loadingStatus.postValue(LoadingStatus.Error)
                }
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}