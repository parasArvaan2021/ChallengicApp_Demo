package com.app.videoplayerdemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var exoplayer: ExoPlayer
    private lateinit var url:String

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "VideoTrimmer")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        playerView=findViewById(R.id.playerView)
        url = intent.getStringExtra("path").toString()
        Log.e("TAG", "onCreate: $url")
        initializePlayer()

    }

    private fun initializePlayer() {
        // Initialize ExoPlayer
        exoplayer = ExoPlayer.Builder(this)
            .build()


        // Set the exoPlayer to the playerView
        playerView.player=exoplayer

        // Create a MediaItem
        val mediaItem = createMediaItem()

        exoplayer.addMediaItem(mediaItem)

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
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoplayer.stop()
        exoplayer.release()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}