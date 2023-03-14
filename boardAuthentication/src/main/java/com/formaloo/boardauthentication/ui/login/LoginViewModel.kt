package com.formaloo.boardauthentication.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formaloo.boardauthentication.data.model.Result
import com.formaloo.boardauthentication.data.model.login.Profile
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

data class LoginUiState(
    val profile: Profile? = null,
    val loading: Boolean = false,
    val openProfile: Boolean = false,
    val userErr: String = "",
    val passErr: String = "",
    val errMsg: String = ""
)

data class LogOutUiState(
    val logout: Boolean = false,
)

data class SignUpUiState(
    val profile: Profile? = null,
    val loading: Boolean = false,
    val openProfile: Boolean = false,
    val errorMessages: List<String> = emptyList(),
    val firsNameErr: String = "",
    val phoneErr: String = "",
    val emailErr: String = "",
    val passErr: String = "",
    val errMsg: String = ""

)

class LoginViewModel(private val repository: AuthRepo) : ViewModel() {
    private val _loginUIState = MutableStateFlow(LoginUiState())
    val loginUIState: StateFlow<LoginUiState> = _loginUIState.asStateFlow()

    private val _signUpUIState = MutableStateFlow(SignUpUiState())
    val signUpUIState: StateFlow<SignUpUiState> = _signUpUIState.asStateFlow()

    private val _outUIState = MutableStateFlow(LogOutUiState())
    val outUIState: StateFlow<LogOutUiState> = _outUIState.asStateFlow()

    fun logout(boardSlug:String) {
        val req= hashMapOf("originator" to boardSlug)

        _loginUIState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.logOut(req)
            _outUIState.update {
                when (result) {
                    is Result.Success -> {
                        removeDataAfterLogout()

                        it.copy(logout = true)

                    }

                    is Result.Error -> {
                        it.copy(logout = false)
                    }

                }
            }
        }
    }

    fun login(req: HashMap<String, String>) {
        _loginUIState.update { it.copy(loading = true, errMsg = "", passErr = "", userErr = "") }
        Log.e("TAG", "login: " + req)

        viewModelScope.launch {
            val result = repository.login(req)
            _loginUIState.update {
                when (result) {
                    is Result.Success -> {
                        val response = result.data
                        if (response.isSuccessful) {
                            if (response.code() == 200 || response.code() == 201) {
                                val cookie = response.headers()["set-cookie"]
                                Log.e("TAG", "login: " + response.headers())
                                if (cookie != null) {
                                    saveToken(cookie)
                                }

                                val profile = response.body()?.data?.profile
                                profile?.let {
                                    saveProfile(profile)
                                }

                                profile?.let {
                                    saveProfile(it)

                                }

                                it.copy(
                                    profile = profile,
                                    loading = false,
                                    openProfile = true
                                )
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
                                    var passError = ""
                                    formErrors.keys().forEach { key ->
                                        val err = retrieveErr(formErrors, key)
                                        if (key == "username") {
                                            userError = err
                                        }
                                        if (key == "password") {
                                            passError = err
                                        }

                                    }

                                    it.copy(
                                        loading = false,
                                        userErr = userError,
                                        passErr = passError,
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
                                var passError = ""
                                formErrors.keys().forEach { key ->
                                    val err = retrieveErr(formErrors, key)
                                    if (key == "username") {
                                        userError = err
                                    }
                                    if (key == "password") {
                                        passError = err
                                    }

                                }

                                it.copy(
                                    loading = false,
                                    userErr = userError,
                                    passErr = passError,
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
                        it.copy(loading = false)
                    }

                }
            }
        }
    }


    fun signUp(req: HashMap<String, String>) {
        _signUpUIState.update { it.copy(loading = true) }
        _signUpUIState.update { it.copy(loading = true, errMsg = "", passErr = "", emailErr = "", phoneErr = "") }

        viewModelScope.launch {
            val result = repository.signUp(req)
            _signUpUIState.update {
                when (result) {
                    is Result.Success -> {
                        val response = result.data

                        if (response.isSuccessful) {
                            if (response.code() == 200 || response.code() == 201) {

                                val cookie = response.headers()["set-cookie"]
                                if (cookie != null) {
                                    saveToken(cookie)
                                }

                                val profile = response.body()?.data?.profile
                                profile?.let {
                                    saveProfile(profile)
                                }

                                profile?.let {
                                    //TODO saveToken

                                    saveProfile(it)

                                }

                                it.copy(
                                    profile = profile,
                                    loading = false,
                                    openProfile = true
                                )
                            } else {
                                it.copy(loading = false)

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

                                var firstError = ""
                                var passError = ""
                                var emailError = ""
                                var phoneError = ""
                                formErrors.keys().forEach { key ->
                                    val err = retrieveErr(formErrors, key)
                                    if (key == "first_name") {
                                        firstError = err
                                    }
                                    if (key == "email") {
                                        emailError = err
                                    }
                                    if (key == "password") {
                                        passError = err
                                    }
                                    if (key == "phone_number") {
                                        phoneError = err
                                    }

                                }

                                it.copy(
                                    loading = false,
                                    firsNameErr = firstError,
                                    passErr = passError,
                                    emailErr = emailError,
                                    phoneErr = phoneError,
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
                        val errorMessages = it.errorMessages
                        Timber.e("$errorMessages")

                        it.copy(errorMessages = errorMessages, loading = false)
                    }

                }
            }
        }
    }


    fun saveToken(cookie: String) = viewModelScope.launch {
        repository.saveCookie(cookie)

    }

    fun retrieveCookie(): String? {
        return repository.retrieveCookie()
    }

    fun saveProfile(profile: Profile) = viewModelScope.launch {
        repository.saveProfile(profile)

    }

    fun removeDataAfterLogout() = viewModelScope.launch {
        saveToken("")
        repository.clearRoom()
    }

}
