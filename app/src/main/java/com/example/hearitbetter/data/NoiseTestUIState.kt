package com.example.hearitbetter.data



data class NoiseTestUIState(
    val playingNoise: Int = 0,
    val currentAudioCount: Int = 1,
    val score: Int = 0,
    val isEnteredDigitsCorrect: Boolean = false,
    val selectedFDigit:Int = 0,
    val isGameOver: Boolean = false
)



