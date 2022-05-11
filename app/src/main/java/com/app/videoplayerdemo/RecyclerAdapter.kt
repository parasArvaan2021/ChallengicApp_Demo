package com.app.videoplayerdemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivData: AppCompatImageView = itemView.findViewById(R.id.image)
        val text_title: TextView = itemView.findViewById(R.id.text_title)
        val text_description: TextView = itemView.findViewById(R.id.text_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_data, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Log.e("TAG", "onBindViewHolder: $position")
    }

    override fun getItemCount(): Int {
        return 30
    }
}