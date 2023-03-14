package com.formaloo.boardauthentication.di


import com.formaloo.boardauthentication.BuildConfig.BASE_URL
import com.formaloo.boardauthentication.BuildConfig.X_API_KEY
import com.formaloo.boardauthentication.data.remote.createRemoteAuthModule
import com.formaloo.boardauthentication.data.local.localModule
import com.formaloo.boardauthentication.data.repository.authRepositoryModule

val authComponent = listOf(
    createRemoteAuthModule(BASE_URL, X_API_KEY),
    localModule,
    authVMModule,
    authRepositoryModule,
)
