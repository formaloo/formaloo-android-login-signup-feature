package com.formaloo.boardauthentication.data.local

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.boardauthentication.data.model.Form

@Dao
abstract class FormDao : BaseDao<Form>() {

    @Query("SELECT * FROM Form ")
    abstract fun getFormList(): List<Form>

    @Query("SELECT * FROM Form WHERE address = :slug")
    abstract suspend fun getForm(slug: String): Form

    @Query("DELETE FROM Form WHERE slug = :slug")
    abstract suspend fun deleteForm(slug: String)

    // ---
    @Query("DELETE FROM Form")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(boards: Form) {
        insert(boards)
    }

    suspend fun save(boards: List<Form>) {
        insert(boards)
    }

}
