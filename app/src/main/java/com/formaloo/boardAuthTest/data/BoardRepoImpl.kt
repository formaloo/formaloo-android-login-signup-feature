package com.formaloo.boardAuthTest.data

import android.util.Log
import com.formaloo.boardAuthTest.data.model.detail.BoardDetailRes
import com.formaloo.boardAuthTest.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val TAG = "BoardRepoImpl"


interface BoardRepo {
    suspend fun getBoard(shared_board_slug: String): Result<BoardDetailRes>

}

class BoardRepoImpl(
    private val source: BoardsDatasource,

    ) : BoardRepo {

    override suspend fun getBoard(shared_board_slug: String): Result<BoardDetailRes> {
        return withContext(Dispatchers.IO) {
            val call = source.getSharedBoardDetail(shared_board_slug)
            try {
                callRequest(call, { it.toBoardDetailRes() }, BoardDetailRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    fun <T, R> callRequest(call: Call<T>, transform: (T) -> R, default: T): Result<R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            Log.e(TAG, "request: " + response.raw())
            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Log.e("request", "Repo responseErrorBody jObjError-> $jObjError")

            } catch (e: Exception) {
                Log.e(TAG, "request: ${response.raw()}")
            }

            when (response.code()) {
                200 -> Result.Success(transform((response.body() ?: default)))
                201 -> Result.Success(transform((response.body() ?: default)))
                else -> Result.Error(null)
            }

        } catch (exception: IOException) {
            Result.Error(exception)

        } catch (exception: SocketException) {
            Result.Error(exception)

        } catch (exception: SocketTimeoutException) {
            Result.Error(exception)

        } catch (exception: UnknownHostException) {
            Result.Error(exception)

        } catch (exception: Throwable) {
            Log.e(TAG, "request: " + exception)
            Result.Error(null)
        }

    }

}



