package com.formaloo.boardauthentication.data.local

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.boardauthentication.data.model.login.Profile
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow

@Dao
abstract class ProfileDao : BaseDao<Profile>() {

    @Query("SELECT * FROM Profile ")
    abstract fun getProfileList(): List<Profile>

    @Query("SELECT * FROM Profile WHERE username = :username")
    abstract suspend fun getProfile(username: String): Profile

    @Query("DELETE FROM Profile WHERE username = :username")
    abstract suspend fun deleteProfile(username: String)

    // ---
    @Query("DELETE FROM Profile")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(boards: Profile) {
        insert(boards)
    }

    suspend fun save(boards: List<Profile>) {
        insert(boards)
    }

}
