package com.example.vcare_app.present.personal.patientprofile.editpatientprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vcare_app.api.ApiClient
import com.example.vcare_app.base.BaseViewModel
import com.example.vcare_app.data.repository.AppRepository
import com.example.vcare_app.utilities.LoadingStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditPatientProfileViewModel : BaseViewModel() {
    val repository = AppRepository(ApiClient.apiService)
    val successUpload = MutableLiveData(LoadingStatus.Initial)
    private val _myUrl = MutableLiveData("")
    val myUrl: LiveData<String> get() = _myUrl
    fun uploadImage(file: File) {
        val requestFile = RequestBody.create(MultipartBody.FORM, file)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        compositeDisposable.add(repository.uploadImage(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                successUpload.value = LoadingStatus.Loading
            }
            .subscribe(
                {
                    successUpload.value = LoadingStatus.Success
                    _myUrl.postValue(it.fileName)
                    errorMsg.value = ""
                }, {
                    successUpload.value = LoadingStatus.Error
                    errorMsg.postValue(handleError(it))
                }
            ))
    }

    fun postPatientProfile() {
        compositeDisposable.add(repository.postPatientProfile(_myUrl.value ?: "")
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnSubscribe {
                status.value = LoadingStatus.Loading
            }.subscribe(
                {
                    status.value = LoadingStatus.Success
                }, {
                    status.value = LoadingStatus.Error
                }
            ))
    }

    fun setDataUrl(url: String) {
        _myUrl.value = url
    }
}