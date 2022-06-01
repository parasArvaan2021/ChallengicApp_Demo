package com.app.videoplayerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.bumptech.glide.Glide
import com.daasuu.imagetovideo.EncodeListener
import com.daasuu.imagetovideo.ImageToVideoConverter
import com.xlythe.view.camera.VideoView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PhotoEditingActivity : AppCompatActivity() {
    lateinit var imagePreview: AppCompatImageView
    private lateinit var videoView: VideoView
    private var path: String? = ""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editing)

        imagePreview = findViewById(R.id.imagePreview)
        videoView = findViewById(R.id.videoView)
        path = try {
            intent.getStringExtra("IMAGE_PATH")
        } catch (ex: Exception) {
            ""
        }

        Log.e("TAG", "onCreate: $path")

        findViewById<AppCompatButton>(R.id.btnConvertImgToVideo).setOnClickListener {

            if (path != null)
                convertImageToVideo()
        }

        findViewById<AppCompatButton>(R.id.btnShowPreview).setOnClickListener {
            findViewById<LinearLayoutCompat>(R.id.llButton).visibility = View.GONE
            if (path != null && path!!.contains(".mp4")) {
                videoView.visibility = View.VISIBLE
                videoView.setVolume(0.3f)
                videoView.file = File(path)
                videoView.play()
            } else {
                imagePreview.visibility = View.VISIBLE
                Glide.with(this).load(path).into(imagePreview)
            }
        }

    }

    private fun convertImageToVideo() {
        val imageToVideo = ImageToVideoConverter(
            outputPath = storeVideoPath(),
            inputImagePath = (if (path != null && !path!!.contains(".mp4")) path else "").toString(),
            size = Size(1024, 1024),
            duration = TimeUnit.SECONDS.toMicros(15),
            listener = object : EncodeListener {
                override fun onCompleted() {
                    runOnUiThread {
                        Log.e("TAG", "onCompleted: ")
                        Toast.makeText(this@PhotoEditingActivity, "Complete", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailed(exception: Exception) {
                    Log.e("TAG", "onFailed: ")

                }

                override fun onProgress(progress: Float) {
                    runOnUiThread {
                        Log.e("TAG", "onProgress: $progress")
                    }


                }

            }
        )

        imageToVideo.start()
    }

    private fun getAndroidMoviesFolder(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    }

    private fun storeVideoPath(): String {
        return getAndroidMoviesFolder().absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(Date()) + "image_video.mp4"

    }
}