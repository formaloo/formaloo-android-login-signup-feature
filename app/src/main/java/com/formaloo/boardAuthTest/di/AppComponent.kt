package com.formaloo.boardAuthTest.di

import com.formaloo.boardAuthTest.BuildConfig.BASE_URL
import com.formaloo.boardAuthTest.BuildConfig.X_API_KEY

val appComponent = listOf(
    createRemoteBoardModule(BASE_URL, X_API_KEY),
    repositoryModule,
    boardModule
)
