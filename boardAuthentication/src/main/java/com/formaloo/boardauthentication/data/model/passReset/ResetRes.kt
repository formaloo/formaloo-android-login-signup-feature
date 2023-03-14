package com.formaloo.boardauthentication.data.model.passReset

import java.io.Serializable

data class ResetRes(
    var status: Int? = null,
    var data: ResetData? = null
) : Serializable {
    companion object {
        fun empty() = ResetRes(0, null)

    }

    fun toResetRes() = ResetRes(status, data)
}
