package com.example.hearitbetter.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearitbetter.audioManager.AudioPlayer
import com.example.hearitbetter.data.MAX_NO_OF_NOISE_PLAY
import com.example.hearitbetter.data.NoiseTestUIState
import com.example.hearitbetter.data.Round
import com.example.hearitbetter.data.getNoise
import com.example.hearitbetter.data.selectDigits
import com.example.hearitbetter.include.TestResultsState
import com.example.hearitbetter.repository.TestResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer, private val repository: TestResultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoiseTestUIState())
    val uiState: StateFlow<NoiseTestUIState> = _uiState.asStateFlow()

    private val _testResultsState:MutableStateFlow<TestResultsState<ResponseBody>> = MutableStateFlow(TestResultsState.Loading)
    val testResultsState:StateFlow<TestResultsState<ResponseBody>> = _testResultsState

    private var usedDigits: MutableSet<Int> = mutableSetOf()
    private var tripletPlayed: String = ""
    private var tripletPlayedUpdated: String = ""
    private var tripletPlayedList: MutableList<String> = mutableListOf()


    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> = _showSnackbar.asStateFlow()

    var userTripletAnswer by mutableStateOf("")
        private set

    var roundAnswerList = mutableListOf<Round>()
    var scorePerRound = 0
    var scoreList = mutableListOf<Int>()

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

    suspend fun playNoise() {

        try {
            audioPlayer.playAudio(selectRandomNoiseAndShuffle())
        } catch (e: Exception) {
            Log.e(TAG, "Error playing Noise: ${e.message}")
        }
    }

    suspend fun playDigit() {

        val selected = selectDigits(_uiState.value.playingNoise)

        audioPlayer.playAudio(selected.digitSource)
        tripletPlayed += selected.digitId
        if (tripletPlayed.length == 3) {
            tripletPlayedList.add(tripletPlayed)
            tripletPlayedUpdated = tripletPlayed

        }
    }

    fun updateTestResultsState(noiseList: MutableList<String>, updatedScore: Int) {
        if (noiseList.size == MAX_NO_OF_NOISE_PLAY) {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore, isGameOver = true
                )
            }
            sendTestResults(roundAnswerList)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore
                )
            }
        }
    }

    fun sendTestResults(roundAnswerList1: MutableList<Round>) {
        viewModelScope.launch {
            try {
//                val response = repository.sendTestResults()
//                Log.i("response", "responseBody$response")

                repository.sendTestResults().collect {
                    Log.i(TAG, "$it")
                }
                //_testResultsState.emit(TestResultsState.Success(response))
            }catch (e: Exception) {
                //_testResultsState.emit(TestResultsState.Error(e.message))
                Log.i(TAG, "api error ${e.message}")
            }

        }
    }

    fun updateTripletAnswer(answer: String) {
        userTripletAnswer = answer
    }

    fun submitTripletAnswer() {
        val isDifficult = if (_uiState.value.playingNoise > 5) 1 else 0
        val testResultPerRound = Round(isDifficult, userTripletAnswer, tripletPlayedUpdated)
        roundAnswerList.add(testResultPerRound)

        val updateScore = _uiState.value.score.plus(_uiState.value.playingNoise)
        scorePerRound = if (tripletPlayedUpdated == userTripletAnswer) updateScore else 0

        scoreList.add(scorePerRound)
        val squareSum = scoreList.sum()
        updateTestResultsState(tripletPlayedList, squareSum)
        clearData()
    }

    fun clearData() {
        tripletPlayed = ""
    }

    private fun selectRandomNoiseAndShuffle(): Int {
        val selectedNoice = getNoise().random()
        _uiState.value = NoiseTestUIState(
            selectedNoice.noiseId, audioPlayer.isPlayingAudio(),
            _uiState.value.currentAudioCount.plus(1),
            _uiState.value.score,
            selectedNoice.isDifficult
        )

        if (usedDigits.contains(selectedNoice.noiseId)) {
            // selectRandomNoiseAndShuffle()
        } else {
            usedDigits.add(selectedNoice.noiseId)
        }
        return selectedNoice.noiseSource
    }

    companion object {
        const val TAG = "AudioPlayerViewModel"
    }
}