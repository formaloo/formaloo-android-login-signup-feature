package com.formaloo.boardauthentication.data.model.login

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.formaloo.boardauthentication.data.model.ChoiceItem
import java.io.Serializable

@Entity
data class Profile(
    @PrimaryKey
    var username: String,
    var first_name: String? = null,
    var last_name: String? = null,
    var email: String? = null,
    var phone_number: String? = null,
    var domain: String? = null,
    var is_active: Boolean? = null,

) : Serializable
