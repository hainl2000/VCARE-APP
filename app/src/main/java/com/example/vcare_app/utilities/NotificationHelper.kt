package com.example.vcare_app.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.vcare_app.mainactivity.MainActivity
import com.example.vcare_app.R
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.present.login.LoginActivity

object NotificationHelper {
    private const val CHANNEL_ID = "1"
    private const val CHANNEL_NAME = "Notification"

    fun showNotification(
        context: Context,
        title: String,
        message: String,
        appointmentID: Int
    ) {
        val intent: Intent
        AppDeepLink.isFromNotification = true
        val appointmentArgument =
            AppointmentDetailArgument(
                appointmentID,
            )
        if (CheckAppAlive.isAppAlive) {
            intent = Intent(context, MainActivity::class.java).apply {
                this.putExtra(AppDeepLink.appointmentDetailArgumentName, appointmentArgument)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            Log.d("login", "showNotification: ${appointmentArgument.appointmentId}")
        } else {
            intent = Intent(context, LoginActivity::class.java).apply {
                this.putExtra(AppDeepLink.appointmentDetailArgumentName, appointmentArgument)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent,
                 PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_vcare)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the Android version is Oreo or higher, and if so, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, builder.build())
    }
}