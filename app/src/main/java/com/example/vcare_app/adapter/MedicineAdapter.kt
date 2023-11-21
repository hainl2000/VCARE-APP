package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.databinding.ItemFileOnlyBinding
import com.example.vcare_app.databinding.ItemImageOnlyBinding
import com.example.vcare_app.onclickinterface.OnMedicineResultClick
import com.example.vcare_app.utilities.Utilities

class MedicineAdapter(var list: List<String>, val onMedicineResultClick: OnMedicineResultClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_XLSX = 1
    private val TYPE_IMAGE_URL = 2

    inner class ImageOnlyViewHolder(val binding: ItemImageOnlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.imgUrl = imgUrl
            binding.onImageClick = onMedicineResultClick
        }
    }

    inner class FileOnlyViewHolder(val binding: ItemFileOnlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.url = url
            binding.onClick = onMedicineResultClick
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageOnlyViewHolder -> holder.bind(list[position])
            is FileOnlyViewHolder -> holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE_URL) {
            val binding =
                ItemImageOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImageOnlyViewHolder(binding)
        } else {
            val binding =
                ItemFileOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FileOnlyViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (Utilities.isExcelFile(list[position])) TYPE_XLSX
        else TYPE_IMAGE_URL

    }

}