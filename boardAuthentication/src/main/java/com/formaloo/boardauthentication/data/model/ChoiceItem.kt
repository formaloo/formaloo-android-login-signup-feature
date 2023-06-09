package com.formaloo.boardauthentication.data.model

import java.io.Serializable

data class ChoiceItem(
    var title: String? = null,
    var image: String?=null,
    var slug: String? = null,
    var type: String? = null
): Serializable
