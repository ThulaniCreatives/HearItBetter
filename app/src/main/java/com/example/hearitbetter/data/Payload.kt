package com.example.hearitbetter.data

data class Payload(
    val rounds: List<Round>,
    val score: Int
)
val mockedTestResults = mutableListOf(
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123"),
    Round(1, "123", "123")
)

val payload = Payload(mockedTestResults,10)
const val MAX_NO_OF_NOISE_PLAY = 10