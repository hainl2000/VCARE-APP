package com.example.vcare_app.utilities

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.UiThread
import com.example.vcare_app.R

object SuccessDialog {
    private var loadingDialog: Dialog? = null

    @UiThread
    fun showDialog(context: Context) {
        if (loadingDialog == null) {
            loadingDialog = Dialog(context)
            loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            loadingDialog?.setContentView(R.layout.fragment_success)
            loadingDialog?.show()
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(
                Runnable {

                    dissmissSuccessDialog()

                }, 2000
            )
        }
    }

    @UiThread
    fun dissmissSuccessDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}