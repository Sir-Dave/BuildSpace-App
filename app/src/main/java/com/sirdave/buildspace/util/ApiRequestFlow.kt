package com.sirdave.buildspace.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sirdave.buildspace.data.remote.dto.response.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRequestFlow(
    context: Context,
    call: suspend () -> Response<T>
): Flow<Resource<T>> = flow<Resource<T>> {
    if (!isNetworkAvailable(context)) {
        emit(Resource.Error(message = "No internet connection!"))
        return@flow
    }

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
                    emit(Resource.Error(message = parsedError.message))
                }
            }
        }
        catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    } ?: emit(Resource.Error(message = "Timeout! Please try again."))
}.flowOn(Dispatchers.IO)


private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (connectivityManager != null) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    return false
}




