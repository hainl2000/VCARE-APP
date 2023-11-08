package com.example.vcare_app.present.personal.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.api.api_model.response.HistoryAppointment
import com.example.vcare_app.databinding.ItemHistoryAppointmentBinding
import com.example.vcare_app.onclickinterface.OnAppointmentClick

class HistoryAppointmentAdapter(
    var list: List<HistoryAppointment>,
    val onAppointmentClick: OnAppointmentClick
) :
    RecyclerView.Adapter<HistoryAppointmentAdapter.HistoryAppointmentViewHolder>() {
    inner class HistoryAppointmentViewHolder(val binding: ItemHistoryAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyAppointment: HistoryAppointment) {
            binding.history = historyAppointment
            binding.onItemClick = onAppointmentClick
        }
    }

    fun updateData(newList: List<HistoryAppointment>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAppointmentViewHolder {
        val binding = ItemHistoryAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryAppointmentViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryAppointmentViewHolder, position: Int) {
        holder.bind(list[position])
    }


}