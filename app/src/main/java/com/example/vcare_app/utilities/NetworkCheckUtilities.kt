package com.example.vcare_app.utilities

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkCheckUtilities(val mcontext: Context) {
    fun isInternetAvailable(): Single<Boolean> {
        return Single.fromCallable {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}