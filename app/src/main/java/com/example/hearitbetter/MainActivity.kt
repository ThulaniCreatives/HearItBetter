package com.example.hearitbetter

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hearitbetter.ui.theme.HearItBetterTheme
import com.example.hearitbetter.ui.theme.viewmodel.AudioPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearItBetterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TestScreen()
                }
            }
        }
    }
}

@Composable
fun TestScreen(modifier: Modifier = Modifier,audioPlayerViewModel: AudioPlayerViewModel = viewModel()) {
    val uiState by audioPlayerViewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.title), style = typography.titleLarge)
        Text("Score ${uiState.score}", style = typography.titleLarge)

        TestResultsComponent(
            uiState.currentAudioCount,
            tripletAnswer = audioPlayerViewModel.userTripletAnswer,
            onUserAnswerChanged = { audioPlayerViewModel.updateTripletAnswer(it) },
            onKeyboardDone = {audioPlayerViewModel.submitTripletAnswer()},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )

        // Button section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { audioPlayerViewModel.playNoises() }
            ) {
                Text(
                    text = stringResource(R.string.button_play),
                    fontSize = 16.sp
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { audioPlayerViewModel.submitTripletAnswer() }
            ) {
                Text(
                    text = stringResource(R.string.button_submit),
                    fontSize = 16.sp
                )
            }

            OutlinedButton(
                onClick = {  },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_back),
                    fontSize = 16.sp
                )
            }
        }
        val showSnackbar = audioPlayerViewModel.showSnackbar.collectAsState().value
        if (uiState.isGameOver) {
            FinalScoreDialog(
                score = uiState.score,
                onTestAgain = { audioPlayerViewModel.clearData() }
            )
        }
    }

}

@Composable
fun TestResultsComponent(
    noiseCount: Int,
    tripletAnswer: String,
    onUserAnswerChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = stringResource(R.string.noise_counter, noiseCount),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )

            Text(
                text = stringResource(R.string.instruction),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            OutlinedTextField(
                value = tripletAnswer,
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                onValueChange = onUserAnswerChanged,
                label = {
                    Text(stringResource(R.string.enter_triplet_label))
                },
                keyboardActions = KeyboardActions(onDone = { onKeyboardDone() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )

            )

        }
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onTestAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    //val activity = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = stringResource(R.string.dialog_title)) },
        text = { Text(text = stringResource(R.string.dialog_score, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    //activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.button_exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onTestAgain) {
                Text(text = stringResource(R.string.button_start))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HearItBetterTheme {
        TestScreen()
    }
}