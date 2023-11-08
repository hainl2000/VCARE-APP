package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.response.Hospital
import com.example.vcare_app.databinding.ItemHospitalBinding
import com.example.vcare_app.onclickinterface.OnHospitalClick

class HospitalAdapter(private var list: List<Hospital>, val onHospitalClick: OnHospitalClick) :
    RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    lateinit var binding: ItemHospitalBinding

    inner class HospitalViewHolder(binding: ItemHospitalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hospital: Hospital, onHospitalClick: OnHospitalClick) {
            binding.hospital = hospital
            binding.onClick = onHospitalClick
            Glide.with(binding.hospitalAvatar.context).load(hospital.image)
                .error(R.drawable.logo_vcare)
                .into(binding.hospitalAvatar)
        }
    }

    fun setData(newList: List<Hospital>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        binding = ItemHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        holder.bind(list[position], onHospitalClick)
    }
}