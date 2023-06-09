package com.formaloo.boardauthentication.data.model

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception?) : Result<Nothing>()
//    data class ErrorStr(val err:String) : Result<Nothing>()
}
