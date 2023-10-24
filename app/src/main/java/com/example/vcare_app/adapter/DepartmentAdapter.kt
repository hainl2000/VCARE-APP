package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.response.Department
import com.example.vcare_app.onclickinterface.OnDepartmentItemClick

class DepartmentAdapter(
    private var list: List<Department>,
    private val onDepartmentItemClick: OnDepartmentItemClick
) :
    RecyclerView.Adapter<DepartmentAdapter.DoctorViewHolder>() {
    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departmentName = itemView.findViewById<TextView>(R.id.department_name)
        val layout = itemView.findViewById<CardView>(R.id.doctor_item_layout)
    }

    fun setData(newList: List<Department>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.department_item, parent, false)
        return DoctorViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = list[position]
        holder.departmentName.text = item.name
        holder.layout.setOnClickListener {
            onDepartmentItemClick.onDepartmentClick(item)
        }
    }
}