package com.example.vcare_app.present.networkerror

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.vcare_app.R
import com.example.vcare_app.present.login.LoginActivity
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.NetworkCheckUtilities
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_error)
        val btnReconnect = findViewById<TextView>(R.id.reconnect_btn)
        btnReconnect.setOnClickListener {
            NetworkCheckUtilities(this).isInternetAvailable()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    LoadingDialogManager.showDialog(this)
                }.doFinally {
                LoadingDialogManager.dismissLoadingDialog()
            }

                .subscribe(
                    {
                        Log.i("NW","btn click")
                        if (it) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }, {
                        Toast.makeText(this, "Can not connect", Toast.LENGTH_LONG).show()
                    }

                )
        }

    }

    private fun isInternetAvailable(): Boolean {
        return try {
            // Ping Google's DNS server (8.8.8.8) on port 53, which is commonly open
            LoadingDialogManager.showDialog(this)
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            LoadingDialogManager.dismissLoadingDialog()
            true
        } catch (e: IOException) {
            // An error occurred, indicating no internet connectivity
            false
        }
    }
}