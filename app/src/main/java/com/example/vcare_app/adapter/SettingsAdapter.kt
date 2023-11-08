package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.model.SettingsItem
import com.example.vcare_app.onclickinterface.OnSettingClick

class SettingsAdapter(val list: List<SettingsItem>, val onSettingClick: OnSettingClick) :
    RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {
    inner class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView.findViewById<LinearLayout>(R.id.settings_layout)
        val icon = itemView.findViewById<ImageView>(R.id.settings_item_id)
        val title = itemView.findViewById<TextView>(R.id.settings_title_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.icon.setImageResource(list[position].resource)
        holder.title.text = list[position].title
        holder.view.setOnClickListener {
            onSettingClick.onSettingClick(list[position])
        }
    }
}