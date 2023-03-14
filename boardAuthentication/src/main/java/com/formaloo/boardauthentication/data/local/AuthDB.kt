package com.formaloo.boardauthentication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.formaloo.boardauthentication.data.local.convertor.AuthConverters
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.login.Profile
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow


@Database(
    entities = [ Form::class, SubmitedRow::class,Profile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AuthConverters::class)
abstract class AuthDB : RoomDatabase() {
    // DAO
    abstract fun formDao(): FormDao
    abstract fun profileDao(): ProfileDao
    abstract fun userRowDao(): UserRowDao


    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AuthDB::class.java,
                "Auth.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
