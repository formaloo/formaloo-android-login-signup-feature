package com.formaloo.boardauthentication.data.model.submitForm

import java.io.Serializable

data class TopFields(
    var title: String? = null,
    var slug: String? = null
): Serializable
