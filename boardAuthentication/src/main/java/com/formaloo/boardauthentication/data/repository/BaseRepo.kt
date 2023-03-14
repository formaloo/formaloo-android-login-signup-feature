package com.formaloo.boardauthentication.data.repository

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.formaloo.boardauthentication.data.model.Result

open class BaseRepo {



    fun <T, R> callRequest(call: Call<T>, transform: (T) -> R, default: T):Result<R> {
        return try {
            val response = call.execute()

            var jObjError: JSONObject? = null
            Log.e(TAG, "raw: " + response.raw())
            Log.e(TAG, "headers: " + response.headers()["set-cookie"])

            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Log.e("try", "Repo responseErrorBody jObjError-> $jObjError")

            } catch (e: Exception) {

                Log.e(TAG, "catch: ${response.raw()}")
            }

            when (response.code()) {
                200 -> Result.Success(transform((response.body() ?: default)))
                201 -> Result.Success(transform((response.body() ?: default)))
//                400 -> Result.ErrorStr("$jObjError")
//                401 -> Result.ErrorStr("unauthorized")
//                500 -> Result.ErrorStr("ServerError")
                else -> Result.Error(null)
            }

        } catch (exception: IOException) {
            Result.Error(exception)

        } catch (exception: SocketException) {
            Result.Error(exception)

        }  catch (exception: SocketTimeoutException) {
            Result.Error(exception)

        }  catch (exception: UnknownHostException) {
            Result.Error(exception)

        } catch (exception: Throwable) {
            Log.e(TAG, "request: " + exception)
            Result.Error(null)
        }

    }

}
