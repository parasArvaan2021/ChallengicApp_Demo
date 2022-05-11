package com.app.videoplayerdemo


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.videoplayerdemo.adapter.ImageSliderAdapter
import com.genius.multiprogressbar.MultiProgressBar


class ProgressBarActivity : AppCompatActivity(), MultiProgressBar.ProgressStepChangeListener,
    MultiProgressBar.ProgressFinishListener {

    private lateinit var multiProgress: MultiProgressBar
    private lateinit var viewPager: ViewPager2
    private lateinit var viewForward: View
    private lateinit var viewBackward: View
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
        multiProgress.setProgressStepsCount(images.size)
        multiProgress.start(0)

        viewPager.adapter = ImageSliderAdapter(this, images)
        viewPager()
        multiProgress.setListener(this)
        multiProgress.setFinishListener(this)

    }

    private fun viewPager() {
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                viewForward.setOnClickListener {
                    Log.e(
                        "TAG",
                        "onPageScrolled: $position==${multiProgress.getProgressStepsCount()}"
                    )
                    if (position + 1 == multiProgress.getProgressStepsCount()) {
                        finish()
                    } else {
                        Log.e("TAG", "else: ${multiProgress.getCurrentStep()}")
                        viewPager.setCurrentItem(position + 1, true)
                        if (multiProgress.getCurrentStep() < multiProgress.getProgressStepsCount() - 1)
                            multiProgress.next()
                    }

                }
                viewBackward.setOnClickListener {
                    viewPager.setCurrentItem(position - 1, true)
                    multiProgress.previous()
                    multiProgress.start(position - 1)
                }

            }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }

            // triggered when there is
            // scroll state will be changed
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
        multiProgress.setSingleDisplayTime(multiProgress.getSingleDisplayTime() + 1F)
        viewPager.currentItem = newStep

    }

    override fun onProgressFinished() {
        Log.e("TAG", "onProgressFinished: ")
        finish()
    }
}