package com.example.vcare_app.utilities

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NoticeWorker(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE) ?: ""
        val message = inputData.getString(NOTIFICATION_MESSAGE) ?: ""
        val appointmentID = inputData.getInt(NOTIFICATION_APPOINTMENT_ID, 0)
        NotificationHelper.showNotification(
            context = context,
            title,
            message,
            appointmentID
        )
        Log.d("TAG", "doWork: $title $message $appointmentID")
        return Result.success()
    }

    companion object {
        const val NOTIFICATION_TITLE = "notificationTITLE"
        const val NOTIFICATION_MESSAGE = "notificationMessage"
        const val NOTIFICATION_APPOINTMENT_ID = "notificationAppointmentID"
    }
}