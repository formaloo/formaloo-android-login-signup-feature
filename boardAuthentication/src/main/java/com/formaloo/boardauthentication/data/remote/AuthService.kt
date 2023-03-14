package com.formaloo.boardauthentication.data.remote

import com.formaloo.boardauthentication.data.model.formDetail.FormDetailRes
import com.formaloo.boardauthentication.data.model.login.LoginRes
import com.formaloo.boardauthentication.data.model.passReset.ResetRes
import com.formaloo.boardauthentication.data.model.submitForm.SubmitFormRes
import com.formaloo.boardauthentication.data.model.submitForm.edit.EditRowRes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    companion object {

        private const val SHARED_BOARDS_PROFILE =
            "v2.0/shared-boards/{board_share_address}/profile/"

        private const val displayProfileForm = "v3.1/form-displays/address/{address}/"

        private const val LOGOUT = "v4/end-users/logout/"
        private const val LOGIN = "v4/end-users/login/"

        /*
        {
          "username": "string",
          "password": "string"
        }
         */
        private const val SIGNUP = "v4/end-users/signup/"

        /*
        {
        "first_name": "string",
        "email": "user@example.com",
        "phone_number": "string",
        "password": "string"
        }
         */
        private const val REQUEST_PASS_RESET = "v1.0/end-users/reset-password-requests/"

        /**REQUEST_PASS_RESETBody:
        {
        "contact": "string",
        "domain": "string"//boardSlug
        }
         */
        private const val CONFIRM_PASS_RESET = "v1.0/end-users/reset-password-confirms/"
        /** CONFIRM_PASS_RESETBody:
        {
        "contact": "string",
        "token": "string",
        "web_token": "string",
        "password": "string",
        "domain": "string"//boardSlug
        }
         */
    }

    @POST(LOGOUT)
    fun logout(
        @Body req: HashMap<String, String>,
        @Header("Cookie") cookie: String
    ): Call<LoginRes>

    @POST(LOGIN)
    suspend fun login(
        @Body req: HashMap<String, String>,
    ): Response<LoginRes>

    @POST(SIGNUP)
    suspend fun signUp(
        @Body req: HashMap<String, String>,
    ): Response<LoginRes>

    @GET(SHARED_BOARDS_PROFILE)
    fun getProfile(
        @Path("board_share_address") board_slug: String,
        @Header("Cookie") cookie: String
    ): Call<SubmitFormRes>

    @PATCH(SHARED_BOARDS_PROFILE)
    suspend fun editProfile(
        @Path("board_share_address") board_slug: String,
        @Body req: HashMap<String, String>,
        @Header("Cookie") cookie: String
    ): Response<EditRowRes>

    @GET(displayProfileForm)
    fun displayProfileForm(
        @Path("address") formAddress: String,
        @Header("Cookie") cookie: String
    ): Call<FormDetailRes>

    @POST(REQUEST_PASS_RESET)
    suspend  fun resetPassReq(
        @Body req: HashMap<String, String>,
    ): Response<ResetRes>

    @POST(CONFIRM_PASS_RESET)
    suspend fun confirmPassReq(
        @Body req: HashMap<String, String>,
    ): Response<ResetRes>

}
