package com.example.bustrackingapp.feature_location_tracking.di

import com.example.bustrackingapp.feature_location_tracking.data.remote.api.DriverLocationApiService
import com.example.bustrackingapp.feature_location_tracking.data.repository.DriverLocationRepositoryImpl
import com.example.bustrackingapp.feature_location_tracking.domain.repository.DriverLocationRepository
import com.example.bustrackingapp.feature_location_tracking.domain.use_case.DriverLocationUseCases
import com.example.bustrackingapp.feature_location_tracking.domain.use_case.GetDriverLocationUseCase
import com.example.bustrackingapp.feature_location_tracking.domain.use_case.SendDriverLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DriverLocationModule {

    @Provides
    @Singleton
    fun provideDriverLocationApiService(retrofit: Retrofit): DriverLocationApiService =
        retrofit.create(DriverLocationApiService::class.java)

    @Provides
    @Singleton
    fun provideDriverLocationRepository(
        api: DriverLocationApiService,
        dispatcher: CoroutineDispatcher
    ): DriverLocationRepository = DriverLocationRepositoryImpl(api, dispatcher)

    @Provides
    @Singleton
    fun provideDriverLocationUseCases(
        send: SendDriverLocationUseCase,
        get: GetDriverLocationUseCase
    ) = DriverLocationUseCases(sendLocation = send, getLocation = get)
}
