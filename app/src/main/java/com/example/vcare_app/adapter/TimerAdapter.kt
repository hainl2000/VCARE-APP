package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.databinding.TimerItemBinding
import com.example.vcare_app.model.Timer
import com.example.vcare_app.onclickinterface.OnTimerClick

class TimerAdapter(val listTimer:List<Timer>,val onTimerClick: OnTimerClick) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {
    inner class  TimerViewHolder(val binding:TimerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(timer: Timer){
            binding.timer = timer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val binding = TimerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TimerViewHolder(binding)
    }

    override fun getItemCount(): Int = listTimer.size

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(listTimer[position])
        holder.binding.timerLayout.setOnClickListener {
            listTimer[position].isChoose = !listTimer[position].isChoose
//            onTimerClick.onTimerClick(listTimer[position])
        }
    }
}