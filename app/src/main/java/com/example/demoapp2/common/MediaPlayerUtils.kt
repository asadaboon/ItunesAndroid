package com.example.demoapp2.common

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class MediaPlayerUtils(context: Context, maxStreams: Int) {
    private val context: Context = context.applicationContext

    private val mediaPlayerPool = mutableListOf<MediaPlayer>().also {
        for (i in 0..maxStreams) it += buildPlayer()
    }
    private val playersInUse = mutableListOf<MediaPlayer>()

    private fun buildPlayer() = MediaPlayer().apply {
        setOnPreparedListener {
            start()
        }

        setOnCompletionListener {
            recyclePlayer(it)
        }
    }

    /**
     * Returns a [MediaPlayer] if one is available,
     * otherwise null.
     */
    private fun requestPlayer(): MediaPlayer? {
        return if (mediaPlayerPool.isNotEmpty()) {
            mediaPlayerPool.removeAt(0).also {
                playersInUse += it
            }
        } else null
    }

    private fun recyclePlayer(mediaPlayer: MediaPlayer) {
        mediaPlayer.reset()
        playersInUse -= mediaPlayer
        mediaPlayerPool += mediaPlayer
    }

    fun playSound(rawResId: String, currentPosition: Int) {
        val mediaPlayer = requestPlayer() ?: return
        mediaPlayer.run {
            setDataSource(rawResId)
            prepareAsync()
        }
        Log.e("currentPosition", "playSound: $currentPosition ")
    }
}