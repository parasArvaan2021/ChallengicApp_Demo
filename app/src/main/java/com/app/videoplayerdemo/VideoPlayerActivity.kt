package com.app.videoplayerdemo

import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: VideoView
    private lateinit var playerViewExoPlayer: PlayerView
    private lateinit var exoplayer: ExoPlayer
    private lateinit var url: String

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "VideoTrimmer")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        playerView = findViewById(R.id.playerView)
        url = intent.getStringExtra("path").toString()
        Log.e("TAG", "onCreate: $url")
        /* initializeExoPlayer()*/
        videoViewInitialize()

    }

    private fun videoViewInitialize() {
        playerView.setVideoPath(url)

        playerView.setOnPreparedListener {
            it.start()
        }
    }
/*
    private fun initializeExoPlayer() {

        // Initialize ExoPlayer
        exoplayer = ExoPlayer.Builder(this).build()

        // Set the exoPlayer to the playerView
        playerViewExoPlayer.player=exoplayer

        // Create a MediaItem
        val mediaItem = createMediaItem()

        exoplayer.addMediaItem(mediaItem)

        //only play video 30 sec in exoplayer
        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
            .let {
                ClippingMediaSource(
                    it,
                    0 * 1000L,
                    30000 * 1000L
                )
            }
        exoplayer.playWhenReady = true
        exoplayer.prepare(source)

    }

    private fun createMediaItem(): MediaItem {
        val mediaUri = Uri.parse(url)
        return MediaItem.fromUri(mediaUri)
    }*/

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
//        exoplayer.stop()
//        exoplayer.release()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}