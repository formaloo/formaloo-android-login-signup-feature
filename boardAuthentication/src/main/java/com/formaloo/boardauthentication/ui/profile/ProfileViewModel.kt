package com.formaloo.boardauthentication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formaloo.boardauthentication.data.model.Fields
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.Result
import com.formaloo.boardauthentication.data.model.submitForm.RenderedData
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow
import com.formaloo.boardauthentication.data.repository.AuthRepo
import com.formaloo.boardauthentication.ui.getJSONArray
import com.formaloo.boardauthentication.ui.getJSONObject
import com.formaloo.boardauthentication.ui.retrieveErr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

data class ProfileUiState(
    val profile: SubmitedRow? = null,
    val profileForm: Form? = null,
    val profileSaved: Boolean = false,
    val profileChanged: Boolean = false,
    val fieldsWithError: HashMap<String, String> = hashMapOf(),
    val loading: Boolean = false,
    val editLoading: Boolean = false,
    val errMsg: String = ""
)

class ProfileViewModel(private val repository: AuthRepo) : ViewModel() {

    private val _profileUIState = MutableStateFlow(ProfileUiState())
    val profileUIState: StateFlow<ProfileUiState> = _profileUIState.asStateFlow()

    val req = HashMap<String, String>()
    fun getProfileForm(formAddress: String) {
        _profileUIState.update { it.copy(loading = true) }

        viewModelScope.launch {
            repository.getFormData(formAddress)?.let { form ->
                _profileUIState.update {
                    it.copy(
                        profileForm = form,
                        profileSaved = true,
                        loading = false
                    )
                }
            }
            val result = repository.displayProfileForm(formAddress)
            _profileUIState.update {
                when (result) {
                    is Result.Success -> {
                        val form = result.data.data?.form
                        form?.let {
                            saveProfileForm(form)
                        }

                        it.copy(
                            profileForm = form,
                            loading = false
                        )
                    }

                    is Result.Error -> {
                        it.copy(loading = false)
                    }

                }
            }
        }
    }

    fun getProfile(boardAddress: String) {
        _profileUIState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.getProfile(boardAddress)
            _profileUIState.update {
                when (result) {
                    is Result.Success -> {
                        val row = result.data.data?.row
                        row?.let {
                            saveUserRow(row)
                        }

                        it.copy(
                            profile = row,
                            loading = false
                        )
                    }

                    is Result.Error -> {
                        it.copy(loading = false)
                    }

                }
            }
        }
    }

    fun editProfile(boardAddress: String) {
        _profileUIState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = repository.editProfile(boardAddress, req)

            _profileUIState.update {
                when (result) {
                    is Result.Success -> {
                        val response = result.data
                        if (response.isSuccessful) {
                            if (response.code() == 200 || response.code() == 201) {
                                val _row = response.body()?.data?.row

                                if (_row != null) {
                                    val rd = HashMap<String, RenderedData>()
                                    _row.rendered_data?.forEach {
                                        rd[it.slug ?: ""] = it
                                    }
                                    val userRow = SubmitedRow(
                                        slug = _row.slug,
                                        form = _row.form,
                                        rendered_data = rd
                                    )

                                    saveUserRow(userRow)

                                    it.copy(
                                        profile = userRow,
                                        profileChanged = true,
                                        editLoading = false
                                    )
                                } else {
                                    it.copy(
                                        editLoading = false,
                                        profileChanged = false,

                                        )
                                }


                            } else {
                                it.copy(
                                    editLoading = false,
                                    profileChanged = false,

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

                                var err = "$gErrors"
                                if (err.contains("[\"")) {
                                    err = err.replace("[\"", "")
                                }
                                if (err.contains("\"]")) {
                                    err = err.replace("\"]", "")
                                }

                                errMsg = err

                                formErrors.keys().forEach { key ->
                                    val err = retrieveErr(formErrors, key)

                                    setErrorToField(key, err)

                                }

                                it.copy(
                                    editLoading = false,
                                    profileChanged = false,
                                    errMsg = errMsg
                                )
                            } catch (e: Exception) {
                                errMsg = e.message ?: ""

                                it.copy(
                                    editLoading = false,
                                    errMsg = errMsg,
                                    profileChanged = false,

                                    )
                            }
                            it.copy(
                                editLoading = false,
                                profileChanged = false,

                                )

                        }

                    }

                    is Result.Error -> {
                        Timber.e("row ${result.exception?.message}")

                        it.copy(
                            editLoading = false,
                            profileChanged = false,

                            )
                    }

                }
            }
        }
    }

    fun saveUserRow(row: SubmitedRow) = viewModelScope.launch {
        repository.saveUserRow(row)
    }

    fun saveProfileForm(form: Form) = viewModelScope.launch {
        repository.saveProfileForm(form)
    }

    fun addKeyValueToReq(fieldSlug: String, fieldValue: String) {
        req[fieldSlug] = fieldValue

    }


    fun reuiredField(field: Fields) {


    }

    fun isLogin(): Boolean {
        return false

    }

    fun setErrorToField(fieldSlug: String, error: String) {
        val fieldsWithError = profileUIState.value.fieldsWithError
        fieldsWithError[fieldSlug] = error

        Timber.e("setErrorToField ${error}")

        _profileUIState.update {
            it.copy(fieldsWithError = fieldsWithError)
        }

    }


}
