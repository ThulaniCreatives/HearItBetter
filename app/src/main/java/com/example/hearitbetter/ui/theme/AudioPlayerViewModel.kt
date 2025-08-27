package com.example.hearitbetter.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearitbetter.R
import com.example.hearitbetter.audioManager.AudioPlayer
import com.example.hearitbetter.data.NoiseTestUIState
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

    init {
        usedDigits.clear()
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

    suspend fun playNoise(){
        audioPlayer.playAudio(selectRandomNoiseAndShuffle())
    }

    suspend fun playDigit(){

        val selected =selectDigits(_uiState.value.playingNoise)

        audioPlayer.playAudio(selected.digitSource)
    }

    private fun selectRandomNoiseAndShuffle(): Int {
        // Continue picking up a new random word until you get one that hasn't been used before
        val selectedNoice = getNoise().random()
        _uiState.value = NoiseTestUIState(
            selectedNoice.noiseId,
            selectedNoice.noiseSource,
            selectedNoice.score,
            selectedNoice.isDifficult
        )
        Log.i("ViewModel", "" + selectedNoice)
        if (usedDigits.contains(selectedNoice.noiseId)) {
            // selectRandomNoiseAndShuffle()
        } else {
            usedDigits.add(selectedNoice.noiseId)
        }

        return selectedNoice.noiseSource
    }


}