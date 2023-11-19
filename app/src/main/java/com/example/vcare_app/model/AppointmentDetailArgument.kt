package com.example.vcare_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentDetailArgument(
    val appointmentId: Int
):Parcelable
