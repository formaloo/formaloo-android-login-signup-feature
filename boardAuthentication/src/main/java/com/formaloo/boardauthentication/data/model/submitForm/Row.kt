package com.formaloo.boardauthentication.data.model.submitForm

import com.formaloo.boardauthentication.data.model.Form
import java.io.Serializable

data class Row(
    var slug: String,
    var submit_code: String? = null,
    var submitter_referer_address: String? = null,
    var created_at: String? = null,
    var form: Form? = null,
    var id: Int? = null,
    var rendered_data: ArrayList< RenderedData>? = null,

    ) : Serializable
