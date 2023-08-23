package com.sirdave.buildspace.data.remote

import com.sirdave.buildspace.data.local.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val authManager: AuthManager ): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            authManager.getToken().first()
        }
        val request = chain.request().newBuilder()
        if (token != null)
            request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}