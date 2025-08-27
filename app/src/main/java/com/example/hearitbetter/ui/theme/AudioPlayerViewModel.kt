package com.example.hearitbetter.ui.theme

import androidx.lifecycle.ViewModel
import com.example.hearitbetter.data.NoiseTestUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoiseTestUIState())
    val uiState: StateFlow<NoiseTestUIState> = _uiState.asStateFlow()

    private var usedDigits: MutableSet<Int> = mutableSetOf()

    init {

        //reset usedDigits
        usedDigits.clear()
        //
    }



}