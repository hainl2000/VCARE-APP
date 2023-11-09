package com.example.vcare_app.utilities

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.vcare_app.R
import com.google.android.material.snackbar.Snackbar

object CustomSnackBar {

    private val handler = Handler(Looper.getMainLooper())
    fun showCustomSnackbar(view: View, message: String,viewOnAnchor:Boolean = true) {
        val inflater = LayoutInflater.from(view.context)
        val customView = inflater.inflate(R.layout.snackbar_view, null)

        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val layout = snackbar.view as Snackbar.SnackbarLayout
        snackbar.setBackgroundTint(view.resources.getColor(R.color.snack_bar_background))
        if (viewOnAnchor) {
            snackbar.setAnchorView(R.id.bottomNavigationView)
        }

        val textView = customView.findViewById<TextView>(R.id.error_txt)
        val closeButton = customView.findViewById<ImageView>(R.id.clear_btn)
        textView.text = message

        closeButton.setOnClickListener {
            snackbar.dismiss()
        }

        layout.addView(customView)

        snackbar.show()

        // Dismiss the Snackbar after 3 seconds
        handler.postDelayed({
            snackbar.dismiss()
        }, 3000)
    }
}