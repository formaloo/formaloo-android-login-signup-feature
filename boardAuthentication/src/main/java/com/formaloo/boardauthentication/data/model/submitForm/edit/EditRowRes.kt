package com.formaloo.boardauthentication.data.model.submitForm.edit

import java.io.Serializable

data class EditRowRes(
    var status: Int? = null,
    var data: EditRowData? = null
) : Serializable {
    companion object {
        fun empty() = EditRowRes(0, null)

    }

    fun toSubmitFormRes() = EditRowRes(status, data)
}
