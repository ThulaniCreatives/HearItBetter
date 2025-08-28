package com.example.hearitbetter.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearitbetter.audioManager.AudioPlayer
import com.example.hearitbetter.data.NoiseTestUIState
import com.example.hearitbetter.data.Round
import com.example.hearitbetter.data.getNoise
import com.example.hearitbetter.data.selectDigits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoiseTestUIState())
    val uiState: StateFlow<NoiseTestUIState> = _uiState.asStateFlow()

    private var usedDigits: MutableSet<Int> = mutableSetOf()
    private var tripletPlayed: String = ""
    private var tripletPlayedList: MutableList<String> = mutableListOf()


    private val _isPlayingNoise = MutableStateFlow(false)
    val isPlayingNoise: StateFlow<Boolean> = _isPlayingNoise.asStateFlow()


    init {
        usedDigits.clear()
        tripletPlayedList.clear()
    }

    fun playNoises() {
        viewModelScope.launch(Dispatchers.IO) {
            playNoise()
            delay(3000)
            playDigit()
            delay(2000)
            playDigit()
            delay(2000)
            playDigit()
        }


    }

    fun stopPlaying() {
        audioPlayer.stopAudio()
    }

    suspend fun playNoise() {

        try {
            audioPlayer.playAudio(selectRandomNoiseAndShuffle())
        } catch (e: Exception) {
            Log.i("Error", "Error playing Noise: ${e.message}")
        }

        Log.i("Error", "Is playing: ${audioPlayer.isPlayingAudio()}")

    }

    suspend fun playDigit() {

        val selected = selectDigits(_uiState.value.playingNoise)

        audioPlayer.playAudio(selected.digitSource)
        tripletPlayed += selected.digitId




        if (tripletPlayed.length == 3) {
            tripletPlayedList.add(tripletPlayed)

            val isDifficult = if (_uiState.value.playingNoise > 5) 1 else 0

            Round(isDifficult, "123", tripletPlayed)
            tripletPlayed = ""
        }

        Log.i("ViewModel", "$tripletPlayedList")

        if (tripletPlayedList.size == 10) {
            Log.i("ViewModel", "Game Over" + tripletPlayedList.size)
            // send data to the internet
            clearData()
        }
    }

    fun clearData() {
        usedDigits.clear()
        tripletPlayedList.clear()
        //_uiState.value = MutableStateFlow<>(NoiseTestUIState())clear
    }


    private fun selectRandomNoiseAndShuffle(): Int {
        val selectedNoice = getNoise().random()
        _uiState.value = NoiseTestUIState(
            selectedNoice.noiseId, audioPlayer.isPlayingAudio(),
            _uiState.value.currentAudioCount.plus(1),
            _uiState.value.score,
            selectedNoice.isDifficult
        )
        Log.i("ViewModel", "Selected Noise Object$selectedNoice")
        if (usedDigits.contains(selectedNoice.noiseId)) {
            // selectRandomNoiseAndShuffle()
        } else {
            usedDigits.add(selectedNoice.noiseId)
        }
        return selectedNoice.noiseSource
    }


}