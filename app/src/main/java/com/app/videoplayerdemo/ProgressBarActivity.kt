package com.app.videoplayerdemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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


        val images = intArrayOf(
            R.drawable.sinchan_one,
            R.drawable.sinchan_two,
            R.drawable.sinchan_three,
            R.drawable.sinchan_four,
            R.drawable.shinchan
        )

        videoUrl = arrayListOf(
            "http://embed.wistia.com/deliveries/ed7812225af6363e03f1fd59eb4e9902.bin",
            "http://embed.wistia.com/deliveries/74826cac48bf0b9225e41a1de12652bd.bin",
            "http://embed.wistia.com/deliveries/74826cac48bf0b9225e41a1de12652bd.bin",
            "http://embed.wistia.com/deliveries/25e5b8b4fde06afbbca11cea0caaca80.bin"

        )

        multiProgress.setProgressStepsCount(videoUrl.size)

        viewPager()
        multiProgress.setListener(this)
        multiProgress.setFinishListener(this)

    }

    private fun viewPager() {
        val adapter = ImageSliderAdapter(this, videoUrl, this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                clickListnerViewPager(position)
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

        }
        multiProgress.setSingleDisplayTime(33F)
        viewPager.currentItem = newStep

    }

    override fun onProgressFinished() {
        Log.e("TAG", "onProgressFinished: ")
        finish()
    }

    override fun videoPlayerPrepareOrNot(position: Int) {
        multiProgress.start(position)
    }

    fun clickListnerViewPager(position: Int) {
        viewForward.setOnClickListener {
            multiProgress.pause()
            if (position + 1 == multiProgress.getProgressStepsCount()) {
                finish()
            } else {
                viewPager.setCurrentItem(position + 1, true)
                if (multiProgress.getCurrentStep() < multiProgress.getProgressStepsCount() - 1) {
                    multiProgress.next()
                }

            }

        }
        viewBackward.setOnClickListener {
            multiProgress.pause()
            viewPager.setCurrentItem(position - 1, true)
            multiProgress.previous()
        }
    }
}