package com.app.videoplayerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.xlythe.view.camera.VideoView
import java.io.File
import java.net.URI

class PhotoEditingActivity : AppCompatActivity() {
    lateinit var imagePreview: AppCompatImageView
    lateinit var videoView: VideoView

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editing)

        imagePreview = findViewById(R.id.imagePreview)
        videoView = findViewById(R.id.videoView)
        val path = intent.getStringExtra("IMAGE_PATH")
        Log.e("TAG", "onCreate: $path")

        if (path != null && path.contains(".mp4")) {
            videoView.visibility=View.VISIBLE
            videoView.setVolume(0.3f)
            videoView.file = File(path)
            videoView.play()
        } else
            Glide.with(this).load(path).into(imagePreview)
    }
}