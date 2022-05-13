package com.app.videoplayerdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.videoplayerdemo.adapter.ImageSliderAdapter
import com.app.videoplayerdemo.interf.VideoPlayerPrepareInterface
import com.genius.multiprogressbar.MultiProgressBar

class ProgressBarActivity : AppCompatActivity(), MultiProgressBar.ProgressStepChangeListener,
    MultiProgressBar.ProgressFinishListener, VideoPlayerPrepareInterface {

    private lateinit var multiProgress: MultiProgressBar
    private lateinit var viewPager: ViewPager2
    private lateinit var viewForward: View
    private lateinit var viewBackward: View
    private lateinit var videoUrl: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_bar)
        multiProgress = findViewById(R.id.multiProgress)
        viewPager = findViewById(R.id.viewPager)
        viewForward = findViewById(R.id.viewForward)
        viewBackward = findViewById(R.id.viewBackward)

        videoUrl = arrayListOf(
            "http://embed.wistia.com/deliveries/ed7812225af6363e03f1fd59eb4e9902.bin",
            "http://embed.wistia.com/deliveries/ed7812225af6363e03f1fd59eb4e9902.bin",
            "http://embed.wistia.com/deliveries/74826cac48bf0b9225e41a1de12652bd.bin",
            "https://www.industrialempathy.com/img/remote/ZiClJf-1920w.jpg",
            "http://embed.wistia.com/deliveries/74826cac48bf0b9225e41a1de12652bd.bin",
            "http://embed.wistia.com/deliveries/25e5b8b4fde06afbbca11cea0caaca80.bin",
            "https://www.industrialempathy.com/img/remote/ZiClJf-1920w.jpg"
        )

        clickListenerViewPager()
        viewPager()
    }

    private fun viewPager() {
        multiProgress.setProgressStepsCount(videoUrl.size)
        multiProgress.setListener(this)
        multiProgress.setFinishListener(this)

        val adapter = ImageSliderAdapter(this, videoUrl, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                if (){
//                    multiProgress.start(position)
//                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun onProgressStepChange(newStep: Int) {
        Log.e("TAG", "onProgressStepChange: ")
        if (newStep == multiProgress.getProgressStepsCount()) {
            multiProgress.clear()
            finish()
        }
        multiProgress.setSingleDisplayTime(5F)
        viewPager.currentItem = newStep

    }

    override fun onProgressFinished() {
        Log.e("TAG", "onProgressFinished: ")
        finish()
    }

    override fun videoPlayerPrepareOrNot() {
        multiProgress.start(viewPager.currentItem)
        Log.e("TAG", "videoPlayerPrepareOrNot pos:${viewPager.currentItem}")
    }

    private fun clickListenerViewPager() {

        viewForward.setOnClickListener {

            multiProgress.pause()
            if (viewPager.currentItem + 1 == multiProgress.getProgressStepsCount() || multiProgress.getCurrentStep() == multiProgress.getProgressStepsCount() - 1) {
                finish()
            } else {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                multiProgress.next()

                if (isImageUrl(videoUrl[viewPager.currentItem]))
                    multiProgress.start(viewPager.currentItem)
            }
            Log.e("TAG", "viewForward pos:${viewPager.currentItem}")

        }
        viewBackward.setOnClickListener {

            if (viewPager.currentItem == 0) {
                finish()
            } else {
                multiProgress.pause()
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                multiProgress.previous()
                if (isImageUrl(videoUrl[viewPager.currentItem]))
                    multiProgress.start(viewPager.currentItem)
                Log.e("TAG", "viewBackward pos:${viewPager.currentItem}")
            }
        }
    }

    fun isImageUrl(url: String): Boolean {
        return url.contains(
            ".jpg",
            true
        ) || url.contains(
            ".png",
            true
        ) || url.contains(".jpeg", true)
    }
}