package com.formaloo.boardauthentication.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val localModule = module {
    single() { AuthDB.buildDatabase(androidContext()) }
    factory { (get() as AuthDB).profileDao() }
    factory { (get() as AuthDB).userRowDao() }
    factory { (get() as AuthDB).formDao() }
    single {
        provideSharePreferences(androidApplication())
    }

}

private fun provideSharePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
