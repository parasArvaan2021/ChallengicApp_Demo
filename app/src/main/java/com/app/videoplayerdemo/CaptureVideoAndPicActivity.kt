package com.app.videoplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.videoplayerdemo.camerabutton.CameraVideoButton

class CaptureVideoAndPicActivity : AppCompatActivity() {


    private lateinit var videoRecordButton: CameraVideoButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_video_and_pic)

        videoRecordButton=findViewById(R.id.videoRecordButton)
        videoRecordButton.setVideoDuration(30000)
        videoRecordButton.enableVideoRecording(true)
        videoRecordButton.enablePhotoTaking(true)


        videoRecordButton.actionListener = object : CameraVideoButton.ActionListener{
            override fun onStartRecord() {
                Log.e("TEST", "Start recording video")
            }

            override fun onEndRecord() {
                Log.e("TEST", "Stop recording video")
            }

            override fun onDurationTooShortError() {
                Log.e("TEST", "Toast or notify user")
            }

            override fun onSingleTap() {
                Log.e("TEST", "Take photo here")
            }

            override fun onCancelled() {
                Log.e("TEST", "Cancelled")
            }
        }

    }
}