package com.sirdave.buildspace.di

import android.content.Context
import com.sirdave.buildspace.data.local.AuthManager
import com.sirdave.buildspace.data.local.BuildSpaceDatabase
import com.sirdave.buildspace.data.remote.Api
import com.sirdave.buildspace.data.repository.AuthRepositoryImpl
import com.sirdave.buildspace.data.repository.SubscriptionRepositoryImpl
import com.sirdave.buildspace.domain.repository.AuthRepository
import com.sirdave.buildspace.domain.repository.SubscriptionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: Api,
        @ApplicationContext context: Context,
        authManager: AuthManager
    ): AuthRepository{
        return AuthRepositoryImpl(api, context, authManager)
    }

    @Provides
    @Singleton
    fun provideSubscriptionRepository(
        api: Api,
        @ApplicationContext context: Context,
        db: BuildSpaceDatabase
    ): SubscriptionRepository{
        return SubscriptionRepositoryImpl(api, context, db)
    }
}