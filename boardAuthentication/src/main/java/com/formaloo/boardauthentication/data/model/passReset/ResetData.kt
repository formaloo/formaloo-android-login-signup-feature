package com.formaloo.boardauthentication.data.model.passReset

import java.io.Serializable

data class ResetData(
    var token: Token? = null
): Serializable
