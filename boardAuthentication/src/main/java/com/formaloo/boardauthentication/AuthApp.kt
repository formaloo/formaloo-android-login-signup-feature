package com.formaloo.boardauthentication


import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import com.formaloo.boardauthentication.di.authComponent


open class AuthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
        }
    }

    //     CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidContext(this@AuthApp)
            // your modules
            modules(provideComponent())

        }

    //     PUBLIC API ---
    open fun provideComponent() = authComponent

}
