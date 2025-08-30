package com.example.hearitbetter.include

sealed class TestResultsState<out T> {
    data class Success<T>(val data: T) : TestResultsState<T>()
    data class Error<T>(val message: String?) : TestResultsState<Nothing>()
    object Loading : TestResultsState<Nothing>()
}