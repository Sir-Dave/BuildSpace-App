package com.example.buildspace.di

import com.example.buildspace.data.repository.AuthRepositoryImpl
import com.example.buildspace.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}