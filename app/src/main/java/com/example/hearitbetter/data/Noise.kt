package com.example.hearitbetter.data

import com.example.hearitbetter.R

data class Noise(val noiseId: Int, val noiseSource: Int, val isDifficult: Boolean, val score: Int)
data class Digit(val digitId: Int, val digitSource: Int)

fun getNoise(): MutableList<Noise> {

    val audioList = mutableListOf<Noise>(
        Noise(1, R.raw.noise_1, false, 1),
        Noise(2, R.raw.noise_2, false, 2),
        Noise(3, R.raw.noise_3, false, 3),
        Noise(4, R.raw.noise_4, false, 4),
        Noise(5, R.raw.noise_5, false, 5),
        Noise(6, R.raw.noise_6, true, 6),
        Noise(7, R.raw.noise_7, true, 7),
        Noise(8, R.raw.noise_8, true, 8),
        Noise(9, R.raw.noise_9, true, 9),
        Noise(10, R.raw.noise_10, true, 10)
    )

    return audioList
}

fun shuffleNoise() {
    return getNoise().shuffle()
}

fun selectDigits(selectedNoise: Int): Digit?{
    val digitList = mutableListOf<Digit>(
        Digit(1, R.raw.one)
    )

  val filteredDigits = when (selectedNoise) {
        in 1..5 -> {
            digitList.find { it.digitId <= 5 }
        }
        else -> {
            digitList.find { it.digitId > 5 }
        }
    }

    return filteredDigits
}


