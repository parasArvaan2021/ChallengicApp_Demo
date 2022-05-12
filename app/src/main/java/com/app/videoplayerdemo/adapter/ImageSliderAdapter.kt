package com.app.videoplayerdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.videoplayerdemo.R
import com.app.videoplayerdemo.interf.VideoPlayerPrepareInterface

class ImageSliderAdapter(
    private val context: Context, private val images: ArrayList<String>,
    private val videoPlayerPrepare: VideoPlayerPrepareInterface
) :
    RecyclerView.Adapter<ImageSliderAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*val images: AppCompatImageView = itemView.findViewById(R.id.images)*/
        val videoPlayer: VideoView = itemView.findViewById(R.id.videoPlayer)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun setData() {
            /* Glide.with(context)
 .load(images[position])
 .into(holder.images)*/
            Log.e("TAG", "setData: ${videoPlayer.isPlaying}")

            videoPlayer.setVideoPath(images[layoutPosition])

            videoPlayer.setOnPreparedListener {

                videoPlayerPrepare.videoPlayerPrepareOrNot(layoutPosition)
                it.start()
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.i("TAG", "onViewAttachedToWindow: ${holder.progressBar.isVisible}")
        holder.progressBar.isVisible = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        return DataViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_slider, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}