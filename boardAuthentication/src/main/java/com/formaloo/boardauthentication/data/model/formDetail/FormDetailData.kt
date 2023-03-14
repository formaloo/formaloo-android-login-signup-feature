package com.formaloo.boardauthentication.data.model.formDetail

import com.formaloo.boardauthentication.data.model.Form
import java.io.Serializable

data class FormDetailData(
    var form: Form? = null,
) : Serializable
