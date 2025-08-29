package com.example.hearitbetter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hearitbetter.audioManager.AudioPlayer
import com.example.hearitbetter.ui.theme.viewmodel.AudioPlayerViewModel
import com.example.hearitbetter.ui.theme.HearItBetterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearItBetterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestScreen(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestScreen(name: String, modifier: Modifier = Modifier) {
 val audioPlayerViewModel: AudioPlayerViewModel = viewModel()
    val uiState by audioPlayerViewModel.uiState.collectAsState()


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Difficult : ${uiState.playingNoise > 5}")
        Text("Progress ${uiState.currentAudioCount}/10")
        Text("Audio is playing:  ${uiState.isPlayingNoise}")
        Button(onClick = {
            audioPlayerViewModel.playNoises()

        }) { Text("Play Audio") }

        Button(onClick = {
            audioPlayerViewModel.stopPlaying()

        }) { Text("Stop Audio") }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HearItBetterTheme {
        //TestScreen("Android", playAudio = playAudio)
    }
}