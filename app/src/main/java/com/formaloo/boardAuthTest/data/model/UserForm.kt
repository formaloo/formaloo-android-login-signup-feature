package com.formaloo.boardAuthTest.data.model

import com.google.gson.JsonObject
import java.io.Serializable

data class UserForm(
    var slug: String,
    var form_fields: JsonObject? = null,
    var captcha_enabled: Boolean? = null,
    var paid_membership: Boolean? = null,
    var signup_enabled: Boolean? = null,
    var otp_enabled: Boolean? = null,
    var login_enabled: Boolean? = null,
) : Serializable
