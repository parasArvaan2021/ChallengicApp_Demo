package com.app.videoplayerdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.videoplayerdemo.R
import com.bumptech.glide.Glide


class ImageSliderAdapter(private val context: Context, private val images: IntArray) :
    RecyclerView.Adapter<ImageSliderAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val images: AppCompatImageView = itemView.findViewById(R.id.images)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_slider, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Glide.with(context)
            .load(images[position])
            .into(holder.images)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}