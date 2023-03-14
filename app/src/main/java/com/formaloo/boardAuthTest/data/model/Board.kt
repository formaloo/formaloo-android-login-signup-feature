package com.formaloo.boardAuthTest.data.model

import com.google.gson.JsonObject
import java.io.Serializable

data class Board(
    var slug: String,
    var blocks: JsonObject? = null,
    var is_primary: Boolean? = null,
    var is_public: Boolean? = null,
    var row_update_push_notif: Boolean? = null,
    var submit_push_notif: Boolean? = null,
    var title: String? = null,
    var share_address: String? = null,
    var description: String? = null,
    var config: JsonObject? = null,
    var user_form: UserForm? = null,
) : Serializable
