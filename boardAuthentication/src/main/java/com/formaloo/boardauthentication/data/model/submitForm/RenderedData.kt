package com.formaloo.boardauthentication.data.model.submitForm

import com.formaloo.boardauthentication.data.model.ChoiceItem
import java.io.Serializable

data class RenderedData(
    var title: String? = null,
    var slug: String? = null,
    var alias: String? = null,
    var type: String? = null,
    var file_type: String? = null,
    var value: Any? = null,
    var raw_value: Any? = null,
    var choice_items: ArrayList<ChoiceItem>? = null,
    var choice_groups: ArrayList<ChoiceItem>? = null
) : Serializable
