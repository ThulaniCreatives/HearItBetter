package com.example.hearitbetter.repository

import com.example.hearitbetter.api.ApiService
import com.example.hearitbetter.data.payload
import com.example.hearitbetter.include.TestResultsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

class TestResultsRepository @Inject constructor(private val apiService: ApiService) {


    fun sendTestResults(): Flow<TestResultsState<ResponseBody>> = flow {
        val response = apiService.sendTestResults(payload)
        emit(TestResultsState.Loading)
        emit(TestResultsState.Success(response))
    }.catch {  error->
        emit(TestResultsState.Error<ResponseBody>(error.message))
    }.flowOn(Dispatchers.IO)
}
