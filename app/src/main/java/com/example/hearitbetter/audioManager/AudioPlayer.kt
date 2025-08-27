package com.example.hearitbetter.audioManager

import android.content.Context
import android.media.MediaPlayer

class AudioPlayer(private val context: Context) : IAudioPlayer {

    private var player: MediaPlayer? = null

    override fun playAudio(audio: Int) {

        MediaPlayer.create(context, audio).apply {
            player = this
            start()
        }
    }

    override fun stopAudio() {
        player?.stop()
        player?.release()
        player = null
    }
}