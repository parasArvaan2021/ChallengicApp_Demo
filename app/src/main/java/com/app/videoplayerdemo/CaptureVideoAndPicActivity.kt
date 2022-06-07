package com.app.videoplayerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.videoplayerdemo.camerabutton.CameraVideoButton
import com.xlythe.view.camera.CameraView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CaptureVideoAndPicActivity : AppCompatActivity(), CameraView.OnImageCapturedListener,
    CameraView.OnVideoCapturedListener {

    lateinit var cameraView: CameraView
    private lateinit var videoRecordButton: CameraVideoButton
    private lateinit var clPArent: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_video_and_pic)

        cameraView = findViewById(R.id.cameraView)
        clPArent = findViewById(R.id.clPArent)

        cameraView.setOnImageCapturedListener(this)
        cameraView.setOnVideoCapturedListener(this)
        cameraView.apply {
            if (ContextCompat.checkSelfPermission(
                    this@CaptureVideoAndPicActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@CaptureVideoAndPicActivity,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    0
                )

            } else {
                open()
                /* takePicture(saveImage())*/
            }
        }
        videoRecordButton = findViewById(R.id.videoRecordButton)
        videoRecordButton.setVideoDuration(10000)
        cameraView.maxVideoDuration = 10000L
        videoRecordButton.enableVideoRecording(true)
        videoRecordButton.enablePhotoTaking(true)


        videoRecordButton.actionListener = object : CameraVideoButton.ActionListener {
            override fun onStartRecord() {
                Log.e("TEST", "Start recording video")
                cameraView.startRecording(
                    createTempImageFile(
                        this@CaptureVideoAndPicActivity,
                        isImage = false
                    )
                )
            }

            override fun onEndRecord() {
                Log.e("TEST", "onEndRecord: ")
                cameraView.stopRecording()

            }

            override fun onDurationTooShortError() {
                Log.e("TEST", "Toast or notify user")
            }

            override fun onSingleTap() {
                Log.e("TEST", "onSingleTap: ")
                cameraView.takePicture(createTempImageFile(this@CaptureVideoAndPicActivity, true))

            }

            override fun onCancelled() {
                Log.e("TEST", "Cancelled")
            }
        }

    }

    @Throws(IOException::class)
    fun createTempImageFile(context: Context, isImage: Boolean): File? {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = if (isImage) "JPEG_" + timeStamp + "_" else "VIDEO_$timeStamp"
        val storageDir: File? = context.externalCacheDir
        return if (isImage) File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        ) else File.createTempFile(
            imageFileName,  /* prefix */
            ".mp4",  /* suffix */
            storageDir /* directory */
        )
    }

    override fun onImageConfirmation() {
        Log.e("TAG", "onImageConfirmation: ")
    }

    override fun onImageCaptured(file: File?) {
        Log.e("TAG", "onImageCaptured: $file")

        startActivity(
            Intent(this, PhotoEditingActivity::class.java)
                .putExtra("IMAGE_PATH", file.toString())
        )
    }

    override fun onVideoConfirmation() {
        Log.e("TAG", "onVideoConfirmation: ")
    }

    override fun onVideoCaptured(file: File?) {
        Log.e("TAG", "onVideoCaptured: $file")
        startActivity(
            Intent(this, PhotoEditingActivity::class.java)
                .putExtra("IMAGE_PATH", file.toString())
        )
    }

    override fun onFailure() {}


    private var permissionRequestLancher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.e("TAG", "permission: ${result.data?.data}")

            }
        }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    if(i==grantResults.size-1){
                        cameraView.open()
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}