package com.example.hearitbetter.audioManager

interface IAudioPlayer {
    fun playAudio(audio:Int)
    fun stopAudio()
    fun isPlayingAudio() : Boolean
}