package com.formaloo.boardauthentication.data.model.login

import java.io.Serializable

data class LoginRes(
    var status: Int? = null,
    var data: LoginData? = null
) : Serializable {
    companion object {
        fun empty() = LoginRes(0, null)

    }

    fun toLoginRes() = LoginRes(status, data)
}
