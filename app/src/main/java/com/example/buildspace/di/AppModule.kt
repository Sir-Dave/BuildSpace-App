package com.example.buildspace.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.local.BuildSpaceDatabase
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
    fun provideApi(okHttpClient: OkHttpClient): Api{
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager = AuthManager(context)

    @Singleton
    @Provides
    fun provideAuthInterceptor(authManager: AuthManager): AuthInterceptor =
        AuthInterceptor(authManager)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

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

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BuildSpaceDatabase{
        return Room.databaseBuilder(
            app,
            BuildSpaceDatabase::class.java,
            "build_space_db"
        ).fallbackToDestructiveMigration().build()
    }
}