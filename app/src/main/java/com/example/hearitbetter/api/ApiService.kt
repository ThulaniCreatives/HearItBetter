package com.example.hearitbetter.api

import com.example.hearitbetter.api.Constants.Companion.BASE_URL
import com.example.hearitbetter.data.Payload
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(BASE_URL)
    suspend fun sendTestResults(@Body testResults: Payload): ResponseBody
}