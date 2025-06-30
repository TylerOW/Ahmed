package com.example.bustrackingapp.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {
    // ─── Existing auth/token settings ───
    suspend fun updateToken(token: String?)
    val getToken: Flow<String>

    suspend fun updateUserType(userType: String?)
    val getUserType: Flow<String>

    // ─── New favorites API ───
    /** Observe the current set of favorited stopNos */
    fun getFavoriteStops(): Flow<Set<String>>

    /** Toggle a stopNo in the favorites set */
    suspend fun toggleFavoriteStop(stopNo: String)
}
