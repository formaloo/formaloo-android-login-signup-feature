package com.formaloo.boardauthentication.data.model.submitForm

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.formaloo.boardauthentication.data.model.Form

@Entity
data class SubmitedRow(
    @PrimaryKey
    var slug: String,
    var form: Form? = null,
    var rendered_data: HashMap<String, RenderedData>?=null,
) : Serializable
