package com.formaloo.boardauthentication.data.remote


/**
 * Implementation of [AuthService] interface
 */

class AuthDatasource(private val service: AuthService) {
    suspend fun login(req: HashMap<String, String>) = service.login(req)
    suspend fun signUp(req: HashMap<String, String>) = service.signUp(req)
    fun logout(req: HashMap<String, String>, cookie: String) = service.logout(req,cookie)
    fun displayProfileForm(formAddress: String, cookie: String) = service.displayProfileForm(formAddress,cookie)
    fun getProfile(shared_board_slug: String, cookie: String) = service.getProfile(shared_board_slug,cookie)
    suspend fun editProfile(shared_board_slug: String, req: HashMap<String, String>, cookie: String) = service.editProfile(shared_board_slug,req,cookie)
    suspend fun resetPassReq(req: HashMap<String, String>) = service.resetPassReq(req)
    suspend fun confirmPassReq( req: HashMap<String, String>) = service.confirmPassReq(req)

}
