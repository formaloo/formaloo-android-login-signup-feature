package com.formaloo.boardauthentication.data.model.passReset

import java.io.Serializable

data class Token(
    var web_token: String? = null,
    var contact: String? = null
): Serializable
