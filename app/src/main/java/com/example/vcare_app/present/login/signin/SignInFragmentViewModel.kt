package com.example.vcare_app.present.login.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.data.repository.CurrentUser
import com.example.vcare_app.data.sharepref.SharePrefItem
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignInFragmentViewModel : BaseViewModel() {

    private val repository = AppRepository(ApiClient.apiService)
    fun login(userName: String, password: String) {
        status.postValue(LoadingStatus.Loading)
        compositeDisposable.add(repository.loginUser(userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    errorMsg.postValue("")
                    CurrentUser.setUserInformation(response.profile)
                    SharePrefManager.saveEmail(userName)
                    SharePrefManager.savePassword(password)
                    SharePrefManager.saveAccessToken(response.token)
                    status.postValue(LoadingStatus.Success)
                }, { error ->
                    errorMsg.postValue(handleError(error))
                    status.postValue(LoadingStatus.Error)
                }
            )
        )
    }

    fun saveUserData(email: String, password: String,statusSave:Boolean = true) {
        SharePrefManager.savePassword(password)
        SharePrefManager.saveEmail(email)
        SharePrefManager.saveStatusSave(statusSave)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}