package com.example.bustrackingapp.core.util

import com.example.bustrackingapp.core.data.remote.dto.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

/**
 * Wraps every suspend API call in a Flow<Resource<T>>:
 *   • emits Loading → Success  OR  Loading → Error
 *   • gracefully handles non‑JSON error bodies (HTML, plain‑text, etc.)
 */
abstract class ApiHandler {

    private val logger = LoggerUtil(c = "ApiHandler")

    fun <T> makeRequest(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (suspend (T) -> Unit)? = null,
    ): Flow<Resource<T>> = flow {
        try {
            emit(Resource.Loading())

            val apiResponse = apiCall()
            logger.info("Response => $apiResponse")

            onSuccess?.invoke(apiResponse.data)
            emit(Resource.Success(apiResponse.data, apiResponse.message))

        } catch (httpEx: HttpException) {

            // --- Parse the error body only if it looks like JSON -----------------
            val rawBody = httpEx.response()?.errorBody()?.string()
            val parsedMsg = try {
                if (!rawBody.isNullOrBlank() && rawBody.trimStart().startsWith("{")) {
                    val err = Gson().fromJson(rawBody, ApiResponse::class.java)
                    err.message
                } else null
            } catch (_: Exception) {
                null
            }

            val finalMsg = parsedMsg ?: httpEx.message() ?: "Network error"
            logger.error("HttpException => $finalMsg")
            emit(Resource.Error(finalMsg))

        } catch (ex: Exception) {
            logger.error("Exception => ${ex.message}")
            emit(Resource.Error(ex.message ?: ex.toString()))
        }
    }
}
