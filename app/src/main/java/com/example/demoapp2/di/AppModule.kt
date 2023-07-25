package com.example.demoapp2.di

import com.example.demoapp2.common.Constants.BASE_URL
import com.example.demoapp2.data.repository.MainRepository
import com.example.demoapp2.data.repository.MainRepositoryImpl
import com.example.demoapp2.data.service.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideService(): MainService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(service: MainService): MainRepository {
        return MainRepositoryImpl(service)
    }
}