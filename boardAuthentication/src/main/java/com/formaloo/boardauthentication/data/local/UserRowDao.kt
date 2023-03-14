package com.formaloo.boardauthentication.data.local

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow

@Dao
abstract class UserRowDao : BaseDao<SubmitedRow>() {

    @Query("SELECT * FROM SubmitedRow ")
    abstract fun getBoardList(): List<SubmitedRow>

    @Query("SELECT * FROM SubmitedRow WHERE slug = :slug")
    abstract suspend fun getBoard(slug: String): SubmitedRow

    @Query("DELETE FROM SubmitedRow WHERE slug = :slug")
    abstract suspend fun deleteBoard(slug: String)

    // ---
    @Query("DELETE FROM SubmitedRow")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(boards: SubmitedRow) {
        insert(boards)
    }

    suspend fun save(boards: List<SubmitedRow>) {
        insert(boards)
    }

}
