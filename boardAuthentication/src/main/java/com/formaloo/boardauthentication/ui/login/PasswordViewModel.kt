package com.formaloo.boardauthentication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formaloo.boardauthentication.data.model.Result
import com.formaloo.boardauthentication.data.repository.AuthRepo
import com.formaloo.boardauthentication.ui.getJSONArray
import com.formaloo.boardauthentication.ui.getJSONObject
import com.formaloo.boardauthentication.ui.removeExtraCharFromGError
import com.formaloo.boardauthentication.ui.retrieveErr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


data class ReqState(
    val loading: Boolean = false,
    val openConfirm: Boolean = false,
    val errorMessages: List<String> = emptyList(),
    val web_token: String = "",
    val contactErr: String = "",
    val errMsg: String = ""


)

data class ConfirmState(
    val loading: Boolean = false,
    val confirmed: Boolean = false,
    val errorMessages: List<String> = emptyList(),
    val tokenErr: String = "",
    val passwordErr: String = "",
    val errMsg: String = ""

    /*
    {
  "contact": "string",
  "token": "string",
  "web_token": "string",
  "password": "string"
}
     */
)

class PasswordViewModel(private val repository: AuthRepo) : ViewModel() {
    private val _reqState = MutableStateFlow(ReqState())
    val reqState: StateFlow<ReqState> = _reqState.asStateFlow()

    private val _confState = MutableStateFlow(ConfirmState())
    val confState: StateFlow<ConfirmState> = _confState.asStateFlow()

    fun resetPassRequest(req: HashMap<String, String>) {
        _reqState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.resetPassReq(req)
            _reqState.update {
                when (result) {
                    is Result.Success -> {

                        val response = result.data
                        if (response.isSuccessful) {
                            if (response.code() == 200 || response.code() == 201) {
                                it.copy(
                                    openConfirm = true,
                                    web_token = response.body()?.data?.token?.web_token ?: ""
                                )
                            } else {
                                it.copy(
                                    loading = false

                                )
                            }

                        } else {

                            var errMsg = ""

                            try {
                                val jObjError = JSONObject(response.errorBody()?.string())

                                val errors = getJSONObject(jObjError, "errors")
                                val formErrors = getJSONObject(errors, "form_errors")
                                val gErrors = getJSONArray(errors, "general_errors")

                                Timber.e("try vm formErrors-> ${formErrors}")
                                Timber.e("try vm gErrors-> ${gErrors}")


                                errMsg = removeExtraCharFromGError(gErrors)

                                var userError = ""
                                formErrors.keys().forEach { key ->
                                    val err = retrieveErr(formErrors, key)
                                    if (key == "contact") {
                                        userError = err
                                    }

                                }

                                it.copy(
                                    loading = false,
                                    contactErr = userError,
                                    errMsg = errMsg
                                )
                            } catch (e: Exception) {
                                errMsg = e.message ?: ""

                                it.copy(
                                    loading = false,
                                    errMsg = errMsg,


                                    )
                            }
                        }

                    }

                    is Result.Error -> {
                        it.copy(openConfirm = false)
                    }

                }
            }
        }
    }

    fun confirmPass(req: HashMap<String, String>) {

        _confState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.confirmPassReq(req)
            _confState.update {
                when (result) {


                    is Result.Success -> {
                        val response = result.data
                        if (response.isSuccessful) {
                            if (response.code() == 200 || response.code() == 201) {
                                it.copy(confirmed = true)

                            } else {
                                it.copy(confirmed = false)

                            }
                        } else {
                            var errMsg = ""

                            try {
                                val jObjError = JSONObject(response.errorBody()?.string())

                                val errors = getJSONObject(jObjError, "errors")
                                val formErrors = getJSONObject(errors, "form_errors")
                                val gErrors = getJSONArray(errors, "general_errors")

                                Timber.e("try vm formErrors-> ${formErrors}")
                                Timber.e("try vm gErrors-> ${gErrors}")


                                errMsg = removeExtraCharFromGError(gErrors)

                                var tokenError = ""
                                var passError = ""
                                formErrors.keys().forEach { key ->
                                    val err = retrieveErr(formErrors, key)
                                    if (key == "token") {
                                        tokenError = err
                                    }
                                    if (key == "password") {
                                        passError = err
                                    }

                                }

                                it.copy(
                                    loading = false,
                                    tokenErr = tokenError,
                                    passwordErr = passError,
                                    errMsg = errMsg
                                )
                            } catch (e: Exception) {
                                errMsg = e.message ?: ""

                                it.copy(
                                    loading = false,
                                    errMsg = errMsg,


                                    )
                            }
                        }

                    }

                    is Result.Error -> {
                        it.copy(confirmed = false)
                    }

                }
            }
        }
    }


}
