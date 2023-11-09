package com.example.vcare_app.utilities

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Utilities {
    companion object {
        fun extractFileNameFromUrl(url: String): String {
            val uri = java.net.URI(url)
            val path = uri.path
            return path.substring(path.lastIndexOf('/') + 1)
        }
        fun haversineDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
            val radius = 6371.01 // Earth's radius in kilometers
            val dLat = toRadians(latitude2 - latitude1)
            val dLon = toRadians(longitude2 - longitude1)
            val a = (sin(dLat / 2) * sin(dLat / 2)) + (cos(toRadians(latitude1)) * cos(toRadians(latitude2)) * (sin(dLon / 2) * sin(dLon / 2)))
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return radius * c
        }

        private fun toRadians(degrees: Double): Double {
            return degrees * Math.PI / 180
        }
    }
}

@BindingAdapter("getImage")
fun getImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url).error(R.drawable.logo_vcare).into(imageView)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.convertTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val parsedDate = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.toLocalDate().format(formatter)
}

