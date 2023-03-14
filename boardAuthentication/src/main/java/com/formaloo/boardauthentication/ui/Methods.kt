package com.formaloo.boardauthentication.ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject

fun showMsg(v: View, msg: String) {
    val snack = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT)

    val view = snack.getView()
    val params = FrameLayout.LayoutParams(view.layoutParams)
    params.gravity = Gravity.TOP
    params.gravity = Gravity.CENTER_HORIZONTAL
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    view.setLayoutParams(params)

    snack.show()
}

fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage

}

fun retrieveErr(it: JSONObject, key: String): String {
    return when {
        it.has(key) -> {
            it.getJSONArray(key)[0].toString()
        }

        else -> ""
    }

}

fun retrieveGeneralErr(gErrors: JSONArray): String {
    gErrors.let {
        return if (it.length() > 0) {
            it[0].toString()
        } else ""
    }
}


fun getJSONObject(it: JSONObject, key: String): JSONObject {
    return if (it.has(key)) {
        it.getJSONObject(key) as JSONObject
    } else {
        JSONObject()
    }
}

fun getJSONArray(it: JSONObject, key: String): JSONArray {
    return if (it.has(key)) {
        it.getJSONArray(key)
    } else {
        JSONArray()
    }
}

fun removeExtraCharFromGError(gErrors: JSONArray): String {
    var err = "$gErrors"
    if (err.contains("[\"")) {
        err = err.replace("[\"", "")
    }
    if (err.contains("\"]")) {
        err = err.replace("\"]", "")
    }
    return err
}