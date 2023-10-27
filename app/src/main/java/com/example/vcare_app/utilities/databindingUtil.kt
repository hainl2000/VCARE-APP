package com.example.vcare_app.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.vcare_app.R

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String) {
    if (!imageUrl.isNullOrBlank()) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .error(R.drawable.error_icon)
            .into(imageView)
    }
}