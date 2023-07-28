package com.example.buildspace.data.remote

import com.example.buildspace.data.local.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(private val authManager: AuthManager): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            authManager.getToken().first()
        }
        return runBlocking {

            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED){
                //token expired, so restart the login process
                authManager.deleteToken()
            }

            token?.let {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }
        }
    }
}