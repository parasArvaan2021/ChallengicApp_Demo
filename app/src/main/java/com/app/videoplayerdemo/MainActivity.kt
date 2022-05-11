package com.app.videoplayerdemo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.genius.multiprogressbar.MultiProgressBar


class MainActivity : AppCompatActivity(),MultiProgressBar.ProgressStepChangeListener,MultiProgressBar.ProgressFinishListener {

    private lateinit var btnPickVideo: Button
    private lateinit var imageView: ImageView
    private lateinit var btnFlipImage: Button
    private lateinit var showBottomSheet: Button
    private lateinit var multiProgress: MultiProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        imageView = findViewById(R.id.image)
        btnFlipImage = findViewById(R.id.btnFlipImage)
        showBottomSheet = findViewById(R.id.showBottomSheet)
        multiProgress = findViewById(R.id.multiProgress)

        multiProgress.setListener(this)
        multiProgress.setFinishListener(this)

        findViewById<Button>(R.id.rvScrollBarStyle).setOnClickListener {
            startActivity(Intent(this, ScrollBarStyleActivity::class.java))
        }


        btnFlipImage.setOnClickListener {
            imageView.rotation = imageView.rotation + 90f
        }
        btnPickVideo.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            resultLauncher.launch(
                Intent.createChooser(intent, "Select Video")
            )
        }

        showBottomSheet.setOnClickListener {
            val bottomSheet = BottomSheetDialog()
            bottomSheet.show(
                supportFragmentManager,
                "ModalBottomSheet"
            )
        }

    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {

                    val selectedImageUri: Uri = data.data!!
                    Log.e("VideoPathData", "${data.data}:\n$selectedImageUri ")

                    val intent = Intent(
                        this,
                        VideoPlayerActivity::class.java
                    )
                    intent.putExtra("path", selectedImageUri.toString())
                    startActivity(intent)

                }
            }
        }

    override fun onProgressStepChange(newStep: Int) {
        Log.e("STEP", "Current step is $newStep")
    }

    override fun onProgressFinished() {
        Log.e("PROGRESS", "Progress finished")
    }
}