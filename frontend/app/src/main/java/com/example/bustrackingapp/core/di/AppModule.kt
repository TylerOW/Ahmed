package com.example.bustrackingapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.bustrackingapp.core.data.repository.LocationRepositoryImpl
import com.example.bustrackingapp.core.data.repository.UserPrefsRepositoryImpl
import com.example.bustrackingapp.core.domain.repository.LocationRepository
import com.example.bustrackingapp.core.domain.repository.UserPrefsRepository
import com.example.bustrackingapp.core.util.AuthInterceptor
import com.example.bustrackingapp.core.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /* ── Network stack ────────────────────────────────────── */

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Retrofit instance.  **IMPORTANT**: we use Constants.baseUrl which already
     * ends with `/api/`, so service interfaces must declare paths like
     * "auth/signInUser" (no leading slash, no extra "api/").
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.baseUrl)    // ← single source of truth
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /* ── DataStore (user prefs) ───────────────────────────── */

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        migrations = listOf(SharedPreferencesMigration(appContext, Constants.userPrefs)),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { appContext.preferencesDataStoreFile(Constants.userPrefs) }
    )

    @Singleton
    @Provides
    fun provideUserPrefsRepository(
        dataStore: DataStore<Preferences>
    ): UserPrefsRepository = UserPrefsRepositoryImpl(dataStore)

    /* ── Location ─────────────────────────────────────────── */

    @Singleton
    @Provides
    fun provideFusedClient(
        @ApplicationContext appContext: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    @Singleton
    @Provides
    fun provideLocationRepository(
        @ApplicationContext appContext: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): LocationRepository = LocationRepositoryImpl(appContext, fusedLocationClient)

    /* ── Coroutine dispatcher ─────────────────────────────── */

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
