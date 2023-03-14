package com.formaloo.boardauthentication.data.repository

import com.formaloo.boardauthentication.data.model.Form

import com.formaloo.boardauthentication.data.model.Result
import com.formaloo.boardauthentication.data.model.formDetail.FormDetailRes
import com.formaloo.boardauthentication.data.model.login.LoginRes
import com.formaloo.boardauthentication.data.model.login.Profile
import com.formaloo.boardauthentication.data.model.passReset.ResetRes
import com.formaloo.boardauthentication.data.model.submitForm.SubmitFormRes
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow
import com.formaloo.boardauthentication.data.model.submitForm.edit.EditRowRes
import retrofit2.Response

interface AuthRepo {

    suspend fun saveProfileForm(form: Form)
    suspend fun getFormData(slug: String): Form?
    suspend fun getProfile(shared_board_slug: String): Result<SubmitFormRes>
    suspend fun editProfile(shared_board_slug: String,req:HashMap<String,String>): Result<Response<EditRowRes>>
    suspend fun getProfileData(username: String): Profile?
    suspend fun saveCookie(cookie:String)
    suspend fun saveProfile(profile: Profile)
    suspend fun clearRoom()

    suspend fun logOut(req: HashMap<String, String>): Result<LoginRes>

    suspend fun saveUserRow(row: SubmitedRow)

    suspend fun displayProfileForm(formAddress: String): Result<FormDetailRes>
     fun retrieveCookie(): String?
    suspend fun resetPassReq(req: HashMap<String, String>): Result<Response<ResetRes>>
    suspend fun confirmPassReq(req: HashMap<String, String>): Result<Response<ResetRes>>
    suspend fun login(req: HashMap<String, String>): Result<Response<LoginRes>>
    suspend fun signUp(
        req: HashMap<String, String>
    ): Result<Response<LoginRes>>

}
