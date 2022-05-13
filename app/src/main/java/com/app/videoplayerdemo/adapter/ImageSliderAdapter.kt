package com.app.videoplayerdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.videoplayerdemo.ProgressBarActivity
import com.app.videoplayerdemo.R
import com.app.videoplayerdemo.interf.VideoPlayerPrepareInterface
import com.bumptech.glide.Glide


class ImageSliderAdapter(
    private val context: ProgressBarActivity, private val images: ArrayList<String>,
    private val videoPlayerPrepare: VideoPlayerPrepareInterface
) :
    RecyclerView.Adapter<ImageSliderAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.images)
        private val videoPlayer: VideoView = itemView.findViewById(R.id.videoPlayer)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun setData() {
            val imageLink = images[layoutPosition]
            if (context.isImageUrl(imageLink)) {
                Log.e("TAG", "Image Position: $absoluteAdapterPosition")

                imageView.visibility = View.VISIBLE
                videoPlayer.visibility = View.GONE
                Glide.with(context)
                    .load(images[layoutPosition])
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
                videoPlayer.visibility = View.VISIBLE
                videoPlayer.setVideoPath(images[layoutPosition])
                Log.e("TAG", "Video Position: $absoluteAdapterPosition")

                videoPlayer.setOnPreparedListener {
                    videoPlayerPrepare.videoPlayerPrepareOrNot()
                    it.start()
                    progressBar.visibility = View.GONE
                }
            }

            Log.e("TAG", "setData: ${videoPlayer.isPlaying}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.progressBar.isVisible = !holder.imageView.isVisible
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