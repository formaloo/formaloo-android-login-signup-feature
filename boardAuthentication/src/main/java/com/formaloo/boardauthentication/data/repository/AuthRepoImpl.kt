package com.formaloo.boardauthentication.data.repository


import android.content.SharedPreferences
import android.util.Log
import com.formaloo.boardauthentication.data.local.FormDao
import com.formaloo.boardauthentication.data.local.ProfileDao
import com.formaloo.boardauthentication.data.local.UserRowDao
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.Result
import com.formaloo.boardauthentication.data.model.formDetail.FormDetailRes
import com.formaloo.boardauthentication.data.model.login.LoginRes
import com.formaloo.boardauthentication.data.model.login.Profile
import com.formaloo.boardauthentication.data.model.passReset.ResetRes
import com.formaloo.boardauthentication.data.model.submitForm.SubmitFormRes
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow
import com.formaloo.boardauthentication.data.model.submitForm.edit.EditRowRes
import com.formaloo.boardauthentication.data.remote.AuthDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

const val TAG = "AuthRepoImpl"

class AuthRepoImpl(
    private val source: AuthDatasource,
    private val profileDao: ProfileDao,
    private val userRowDao: UserRowDao,
    private val formDao: FormDao,
    private val sharedPreferences: SharedPreferences

) : AuthRepo, BaseRepo() {

    override suspend fun clearRoom() {
        profileDao.deleteAllFromTable()
        formDao.deleteAllFromTable()
        userRowDao.deleteAllFromTable()

    }


    override suspend fun saveProfile(profile: Profile) {
        return profileDao.save(profile)

    }

    override suspend fun saveUserRow(row: SubmitedRow) {
        return userRowDao.save(row)

    }

    override suspend fun getProfileData(username: String): Profile {
        return profileDao.getProfile(username)

    }

    override suspend fun saveCookie(cookie: String) {
        sharedPreferences.edit().putString("cookie", cookie).apply()


    }

    override fun retrieveCookie(): String? {
        return sharedPreferences.getString("cookie", "")


    }


    override suspend fun saveProfileForm(form: Form) {
        return formDao.save(form)

    }

    override suspend fun getFormData(slug: String): Form? {
        return formDao.getForm(slug)

    }

    override suspend fun resetPassReq(req: HashMap<String, String>): Result<Response<ResetRes>> {
        return withContext(Dispatchers.IO) {
            val call = source.resetPassReq(req)
            try {
                Result.Success(call)
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun confirmPassReq(req: HashMap<String, String>): Result<Response<ResetRes>> {
        return withContext(Dispatchers.IO) {
            val call = source.confirmPassReq(req)

            try {
                Result.Success(call)
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun getProfile(shared_board_slug: String): Result<SubmitFormRes> {
        val cookie = retrieveCookie() ?: ""
        Log.e(TAG, "getProfile: " + cookie)
        return withContext(Dispatchers.IO) {
            val call = source.getProfile(shared_board_slug, cookie)
            try {
                callRequest(call, { it.toSubmitFormRes() }, SubmitFormRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun displayProfileForm(formAddress: String): Result<FormDetailRes> {
        return withContext(Dispatchers.IO) {
            val call = source.displayProfileForm(formAddress, retrieveCookie() ?: "")
            try {
                callRequest(call, { it.toFormDetailRes() }, FormDetailRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun editProfile(
        shared_board_slug: String,
        req: HashMap<String, String>
    ): Result<Response<EditRowRes>> {
        return withContext(Dispatchers.IO) {
            val call = source.editProfile(shared_board_slug, req, retrieveCookie() ?: "")
            try {
                Result.Success(call)
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun login(req: HashMap<String, String>): Result<Response<LoginRes>> {

        return withContext(Dispatchers.IO) {
            val call = source.login(req)

            try {
                Result.Success(call)
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun signUp(req: HashMap<String, String>): Result<Response<LoginRes>> {
        Log.e(TAG, "signUp req: " + req)

        return withContext(Dispatchers.IO) {
            val call = source.signUp(req)
            Log.e(TAG, "signUp: " + call.body())

            try {
                Result.Success(call)
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun logOut(req: HashMap<String, String>): Result<LoginRes> {
        return withContext(Dispatchers.IO) {
            val call = source.logout(req, retrieveCookie() ?: "")
            try {
                callRequest(call, { it.toLoginRes() }, LoginRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }


}



