package com.example.vcare_app.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.vcare_app.R

class Utilities {
    companion object {
        fun extractFileNameFromUrl(url: String): String {
            val uri = java.net.URI(url)
            val path = uri.path
            return path.substring(path.lastIndexOf('/') + 1)
        }
    }
}
