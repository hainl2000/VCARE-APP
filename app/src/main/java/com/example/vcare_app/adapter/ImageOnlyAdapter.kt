package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.databinding.ItemImageOnlyBinding
import com.example.vcare_app.onclickinterface.OnImageOnlyClick

class ImageOnlyAdapter(var list: List<String>, val onImageOnlyClick: OnImageOnlyClick) :
    RecyclerView.Adapter<ImageOnlyAdapter.ImageOnlyViewHolder>() {
    inner class ImageOnlyViewHolder(val binding: ItemImageOnlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.imgUrl = imgUrl
            binding.onImageClick = onImageOnlyClick
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageOnlyViewHolder {
        val binding =
            ItemImageOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageOnlyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImageOnlyViewHolder, position: Int) {
        holder.bind(list[position])
    }

}