package com.example.vcare_app.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {
    val allowBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
//        NetworkCheckUtilities(this).isInternetAvailable().subscribe(
//            {
//                Log.i("NW", "chekc internet available")
//            },
//            {
//                val intent = Intent(this, NetworkErrorActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            }
//        )
//
//        NetworkManager(this).observeNetworkStatus().subscribe({
//            if (it) {
//                Log.i("NW", "check network status")
//            } else {
//                val intent = Intent(this, NetworkErrorActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            }
//        }, {
//            CustomSnackBar.showCustomSnackbar(this.window.decorView,"Không thể kết nối")
//        })

    }
    override fun onBackPressed() {
        if (allowBack) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}