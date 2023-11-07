package com.example.vcare_app.utilities

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utilities {
    companion object {
        fun extractFileNameFromUrl(url: String): String {
            val uri = java.net.URI(url)
            val path = uri.path
            return path.substring(path.lastIndexOf('/') + 1)
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

