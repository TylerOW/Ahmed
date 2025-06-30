package com.example.bustrackingapp.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.bustrackingapp.core.domain.repository.UserPrefsRepository
import com.example.bustrackingapp.core.util.Constants
import com.example.bustrackingapp.core.util.LoggerUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class UserPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefsRepository {
    private val logger = LoggerUtil(c = "UserPrefsRepositoryImpl")

    private object PreferencesKey {
        val userToken     = stringPreferencesKey("user_token")
        val userType      = stringPreferencesKey("user_type")
        // store favorites as a string set in DataStore
        val favoriteStops = stringSetPreferencesKey("favorite_stops")
    }

    override suspend fun updateToken(token: String?) {
        logger.info("$token", "updateToken")
        dataStore.edit { prefs ->
            prefs[PreferencesKey.userToken] = token ?: ""
        }
    }

    override val getToken: Flow<String> = dataStore.data
        .catch { e ->
            if (e is Exception) {
                logger.info(e.message ?: "unknown", "getToken")
                emit(emptyPreferences())
            }
        }
        .map { prefs ->
            prefs[PreferencesKey.userToken] ?: ""
        }

    override suspend fun updateUserType(userType: String?) {
        logger.info("$userType", "updateUserType")
        dataStore.edit { prefs ->
            prefs[PreferencesKey.userType] = userType ?: Constants.UserType.passenger
        }
    }

    override val getUserType: Flow<String> = dataStore.data
        .catch { e ->
            if (e is Exception) {
                logger.info(e.message ?: "unknown", "getUserType")
                emit(emptyPreferences())
            }
        }
        .map { prefs ->
            prefs[PreferencesKey.userType] ?: Constants.UserType.passenger
        }

    // ─── Favorites implementation ───────────────────────────────────────────

    override fun getFavoriteStops(): Flow<Set<String>> =
        dataStore.data
            .catch { e ->
                if (e is Exception) {
                    logger.info(e.message ?: "unknown", "getFavoriteStops")
                    emit(emptyPreferences())
                }
            }
            .map { prefs ->
                prefs[PreferencesKey.favoriteStops] ?: emptySet()
            }

    override suspend fun toggleFavoriteStop(stopNo: String) {
        dataStore.edit { prefs ->
            val current = prefs[PreferencesKey.favoriteStops]?.toMutableSet() ?: mutableSetOf()
            if (current.contains(stopNo)) current.remove(stopNo)
            else current.add(stopNo)
            prefs[PreferencesKey.favoriteStops] = current
            logger.info("Toggled favorite for $stopNo; now = $current", "toggleFavoriteStop")
        }
    }
}
