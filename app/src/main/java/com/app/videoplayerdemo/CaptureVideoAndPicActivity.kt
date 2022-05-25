package com.app.videoplayerdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.videoplayerdemo.camerabutton.CameraVideoButton
import com.xlythe.view.camera.CameraView
import com.xlythe.view.camera.PermissionChecker
import java.io.File
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

class CaptureVideoAndPicActivity : AppCompatActivity() {

    lateinit var cameraView: CameraView
    private lateinit var videoRecordButton: CameraVideoButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_video_and_pic)

        cameraView = findViewById(R.id.cameraView)

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
                cameraView.apply {
                    open()
                    /* takePicture(saveImage())*/
                }
            }
        }

        videoRecordButton = findViewById(R.id.videoRecordButton)
        videoRecordButton.setVideoDuration(30000)
        videoRecordButton.enableVideoRecording(true)
        videoRecordButton.enablePhotoTaking(true)


        videoRecordButton.actionListener = object : CameraVideoButton.ActionListener {
            override fun onStartRecord() {
                Log.e("TEST", "Start recording video")
//                cameraView.startRecording(saveImage(isImage = false))
            }

            override fun onEndRecord() {
                Log.e("TEST", "onEndRecord: ")
               cameraView.stopRecording()
            }

            override fun onDurationTooShortError() {
//                Log.e("TEST", "Toast or notify user")
            }

            override fun onSingleTap() {
                Log.e("TEST", "onSingleTap: ")
                cameraView.takePicture(saveImage(true))

            }

            override fun onCancelled() {
                Log.e("TEST", "Cancelled")
            }
        }

    }

    private fun saveImage(isImage: Boolean): File {
        var savedImagePath: String? = null

        // Create the new file in the external storage
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = if (isImage) "JPEG_$timeStamp.jpg" else "VIDEO_$timeStamp.mp4"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/MyCamera"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }

        return File(storageDir, imageFileName)
    }



}