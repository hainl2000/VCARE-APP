package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.api.api_model.response.ServiceDetail
import com.example.vcare_app.databinding.ItemConclusionImageBinding
import com.example.vcare_app.onclickinterface.OnImageOrUrlClick

class ConclusionAdapter
    (private var listConclusion: List<ServiceDetail>,val onImageOnlyClick: OnImageOrUrlClick) :
    RecyclerView.Adapter<ConclusionAdapter.ConclusionViewHolder>() {
    inner class ConclusionViewHolder(val binding: ItemConclusionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conclusionResponse: ServiceDetail) {
            binding.conclusion = conclusionResponse
            binding.imageRecyclerview.adapter = ImageAndUrlAdapter(conclusionResponse.resultImage,onImageOnlyClick)
        }
    }

    fun updateData(newList: List<ServiceDetail>) {
        this.listConclusion = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConclusionViewHolder {
        val binding =
            ItemConclusionImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConclusionViewHolder(binding)
    }

    override fun getItemCount(): Int = listConclusion.size

    override fun onBindViewHolder(holder: ConclusionViewHolder, position: Int) {
        holder.bind(listConclusion[position])
    }
}