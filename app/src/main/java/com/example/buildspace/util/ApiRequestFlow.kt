package com.example.buildspace.util

import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun<T> apiRequestFlow(call: suspend () -> Response<T>): Flow<Resource<out Any>> = flow {
    emit(Resource.Loading(true))

    withTimeoutOrNull(10000L) {
        val response = call()

        try {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(Resource.Success(data))
                }
            }
            else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError = Gson().fromJson(error.charStream(), ApiResponse::class.java)
                    emit(Resource.Error(parsedError.message, parsedError.httpStatusCode))
                }
            }
        }
        catch (e: Exception) {
            emit(Resource.Error(e.message ?: e.toString(), 400))
        }
    } ?: emit(Resource.Error("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)