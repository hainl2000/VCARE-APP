package com.example.vcare_app.base

import NetworkManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.vcare_app.present.networkerror.NetworkErrorActivity
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.NetworkCheckUtilities


open class BaseActivity(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        NetworkCheckUtilities(this).isInternetAvailable().subscribe(
            {
            Log.i("NW","chekc internet available")
            },
            {
                val intent = Intent(this, NetworkErrorActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        )

        NetworkManager(this).observeNetworkStatus().subscribe({
            if (it) {
                Log.i("NW","check network status")
            } else {
                val intent = Intent(this, NetworkErrorActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }, {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
        })

    }

    override fun onResume() {
        super.onResume()

    }
}