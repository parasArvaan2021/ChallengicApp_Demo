package com.app.videoplayerdemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var imageView: ImageView
    private lateinit var imageCapture: AppCompatImageView
    private lateinit var btnFlipImage: Button
    private lateinit var showBottomSheet: Button

    var photoFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        imageView = findViewById(R.id.image)
        btnFlipImage = findViewById(R.id.btnFlipImage)
        imageCapture = findViewById(R.id.imageCapture)
        showBottomSheet = findViewById(R.id.showBottomSheet)


        findViewById<Button>(R.id.rvScrollBarStyle).setOnClickListener {
            startActivity(Intent(this, ScrollBarStyleActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnCaptureVideo).setOnClickListener {
            startActivity(Intent(this, CaptureVideoAndPicActivity::class.java))
        }

        findViewById<Button>(R.id.btnStartProgress).setOnClickListener {
            startActivity(Intent(this, ProgressBarActivity::class.java))
        }

        findViewById<Button>(R.id.btnSaveImage).setOnClickListener {
            val bm = (imageCapture.drawable as BitmapDrawable).bitmap
            saveImage(bm)
        }

        findViewById<Button>(R.id.btnMirror).setOnClickListener {
            val bm = (imageCapture.drawable as BitmapDrawable).bitmap
            Log.e("TAG", "onCreate: $bm")
            imageCapture.setImageBitmap(createFlippedBitmap(bm, xFlip = true, yFlip = false))

        }

        findViewById<Button>(R.id.btnCapture).setOnClickListener {
            captureImage()
        }

        btnFlipImage.setOnClickListener {
            imageView.rotation = imageView.rotation + 90f
        }
        btnPickVideo.setOnClickListener {
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

    private fun createFlippedBitmap(source: Bitmap, xFlip: Boolean, yFlip: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.postScale(
            if (xFlip) (-1).toFloat() else 1.toFloat(),
            if (yFlip) (-1).toFloat() else 1.toFloat(),
            source.width / 2f,
            source.height / 2f
        )
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun captureImage() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                photoFile = createTempImageFile(this)
                Log.e("TAG", "captureImage: $photoFile")
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.app.videoplayerdemo.provider",
                        photoFile!!
                    )
                    Log.e("TAG", "captureImagePhotoUri: $photoFile")
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                } else {
                    Log.e("TAG", "captureImage:else ")
                }
            } catch (ex: Exception) {
                print(ex)
            }
        }
    }

    @Throws(IOException::class)
    fun createTempImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = context.externalCacheDir
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }


    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null

        // Create the new file in the external storage
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/MyCamera"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }

        // Save the new Bitmap
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                /*fOut.write(image.byteCount)*/
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }


        }
        return savedImagePath
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
                Glide.with(this)
                    .load(photoFile!!.absolutePath)
                    .into(imageCapture)
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