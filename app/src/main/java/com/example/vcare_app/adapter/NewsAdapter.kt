package com.example.vcare_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.model.News
import com.example.vcare_app.onclickinterface.OnCardItemClick

class NewsAdapter(private val listNews: List<News>, val onCardItemClick: OnCardItemClick) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view)
        val imgUrl = itemView.findViewById<ImageView>(R.id.card_img)
        val title = itemView.findViewById<TextView>(R.id.card_title)
        val content = itemView.findViewById<TextView>(R.id.card_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_card, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = listNews.size
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = listNews[position]
        holder.title.text = item.title
        holder.content.text = item.content

        /// Mock data
//        holder.imgUrl.setImageResource(Integer.parseInt(item.imgUrl))

        holder.cardView.setOnClickListener {
            onCardItemClick.onCardItemClick(item)
        }
    }

}