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
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi


class MainActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var imageView: ImageView
    private lateinit var btnFlipImage: Button
    private lateinit var showBottomSheet: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        imageView = findViewById(R.id.image)
        btnFlipImage = findViewById(R.id.btnFlipImage)
        showBottomSheet = findViewById(R.id.showBottomSheet)
        findViewById<Button>(R.id.rvScrollBarStyle).setOnClickListener {
            startActivity(Intent(this, ScrollBarStyleActivity::class.java))
        }

        findViewById<Button>(R.id.btnStartProgress).setOnClickListener {
            startActivity(Intent(this, ProgressBarActivity::class.java))
        }


        btnFlipImage.setOnClickListener {
            imageView.rotation = imageView.rotation + 90f
        }
        btnPickVideo.setOnClickListener {

//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//            intent.type = "video/*"
//            intent.putExtra("android.intent.extra.durationLimit", 30);
//            resultLauncher.launch(
//                Intent.createChooser(intent, "Select Video")
//            )

            pickVideo()

        }
        showBottomSheet.setOnClickListener {
            val bottomSheet = BottomSheetDialog()
            bottomSheet.show(
                supportFragmentManager,
                "ModalBottomSheet"
            )
        }

    }


    private fun pickVideo() {
        val intent = Lassi(this)
            .with(LassiOption.CAMERA_AND_GALLERY)
            .setMediaType(MediaType.VIDEO)
            .setMaxTime(30)
            .setGridSize(3)
            .setStatusBarColor(R.color.colorPrimaryDark)
            .setToolbarResourceColor(R.color.colorPrimary)
            .setProgressBarColor(R.color.colorAccent)
            .setSupportedFileTypes("mp4", "mkv", "webm", "avi", "flv", "3gp")
            .build()
        receiveData.launch(
            intent
        )
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

    private val receiveData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia =
                    it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    Log.e("TAG", "pickVideo:$selectedMedia ")
                    val intent = Intent(
                        this,
                        VideoPlayerActivity::class.java
                    )
                    intent.putExtra("path", selectedMedia[0].path.toString())
                    startActivity(intent)
                }
            }
        }

}