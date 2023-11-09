package com.example.vcare_app.utilities

import android.content.Context
import androidx.appcompat.app.AlertDialog

object CustomInformationDialog {
    private lateinit var dialog: AlertDialog
    fun showCustomInformationDialog(context: Context, message: String, okAction: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Thông báo: ")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ ->
            okAction()
        }

        builder.setNegativeButton("Huỷ") { _, _ ->
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}