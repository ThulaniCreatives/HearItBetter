package com.example.hearitbetter.data



data class NoiseTestUIState(
    val playingNoise: Int = 0,
    val isPlayingNoise: Boolean = false,
    var currentAudioCount: Int = 0,
    var score: Int = 0,
    var isEnteredDigitsCorrect: Boolean = false,
    val selectedFDigit:Int = 0,
    var isGameOver: Boolean = false
)



