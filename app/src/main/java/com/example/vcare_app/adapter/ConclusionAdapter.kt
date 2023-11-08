package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.api.api_model.response.ConclusionResponse
import com.example.vcare_app.databinding.ItemConclusionImageBinding
import com.example.vcare_app.onclickinterface.OnConclusionClick

class ConclusionAdapter(private var listConclusion: List<ConclusionResponse>, val onConclusionClick: OnConclusionClick) :
    RecyclerView.Adapter<ConclusionAdapter.ConclusionViewHolder>() {
    inner class ConclusionViewHolder(val binding: ItemConclusionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conclusionResponse: ConclusionResponse) {
            binding.conclusion = conclusionResponse
            binding.onClick = onConclusionClick
        }
    }

    fun updateData(newList: List<ConclusionResponse>){
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