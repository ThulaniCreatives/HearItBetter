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

fun selectDigits(selectedNoise: Int): Digit {
    val digitList = mutableListOf<Digit>(
        Digit(1, R.raw.digit_1),
        Digit(2, R.raw.digit_2),
        Digit(3, R.raw.digit_3),
        Digit(4, R.raw.digit_4),
        Digit(5, R.raw.digit_5),
        Digit(6, R.raw.digit_6),
        Digit(7, R.raw.digit_7),
        Digit(8, R.raw.digit_8),
        Digit(9, R.raw.digit_9)


    )

  val filteredDigits = when (selectedNoise) {
        in 1..5 -> {
            digitList.filter { it.digitId <= 5 }
        }
        else -> {
            digitList.filter { it.digitId > 5 }
        }
    }

    return filteredDigits.random()
}


