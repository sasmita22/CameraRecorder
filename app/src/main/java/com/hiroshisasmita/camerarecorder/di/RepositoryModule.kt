package com.hiroshisasmita.camerarecorder.di

import com.hiroshisasmita.camerarecorder.data.repository.RepositoryImpl
import com.hiroshisasmita.camerarecorder.data.service.ApiService
import com.hiroshisasmita.camerarecorder.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesRepository(service: ApiService): Repository {
        return RepositoryImpl(service)
    }
}