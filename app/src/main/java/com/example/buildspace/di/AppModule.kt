package com.example.buildspace.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.buildspace.data.local.TokenManager
import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.AuthInterceptor
import com.example.buildspace.domain.use_cases.ValidateEmail
import com.example.buildspace.domain.use_cases.ValidateField
import com.example.buildspace.domain.use_cases.ValidatePassword
import com.example.buildspace.domain.use_cases.ValidateRepeatedPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): Api{
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideFieldValidator(): ValidateField = ValidateField()

    @Singleton
    @Provides
    fun provideEmailValidator(): ValidateEmail = ValidateEmail()

    @Singleton
    @Provides
    fun providePasswordValidator(): ValidatePassword = ValidatePassword()

    @Singleton
    @Provides
    fun provideRepeatedPasswordValidator(): ValidateRepeatedPassword = ValidateRepeatedPassword()
}