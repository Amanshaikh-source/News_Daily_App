package com.example.newsdaily.ui.theme


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsdaily.R

class RecyclerAdapter(private var newsList: List<New>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val itempublished: TextView = itemView.findViewById(R.id.published)
        val itemauthor: TextView = itemView.findViewById(R.id.author)
        val itemcontent: TextView = itemView.findViewById(R.id.TV_content)

        //takes care of click events
        init {
            itemView.setOnClickListener { v: View ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val newsItem = newsList[position]
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
                    startActivity(itemView.context, intent, null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val newsItem = newsList[position]

        Log.d("IMAGE_DEBUG", "Image URL: ${newsItem.urlToImage}")
        Log.d("AUTHOR_DEBUG","Author: ${newsItem.author}")

        holder.itemTitle.text = newsItem.title ?: "No Title Available"
        holder.itemDetail.text = newsItem.description ?: "No Description Available"
        holder.itempublished.text = newsItem.publishedAt ?: "No Date Available"
        holder.itemauthor.text = newsItem.author ?: "Unknown Author"
        holder.itemcontent.text = newsItem.content ?: "No Content Available"


        if (!newsItem.urlToImage.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(newsItem.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error)
                .override(600, 400)
                .into(holder.itemPicture)
        }else {
            holder.itemPicture.setImageResource(R.drawable.placeholder_image)
        }
    }


    override fun getItemCount(): Int = newsList.size

    fun updateData(newsList: List<New>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }
}
