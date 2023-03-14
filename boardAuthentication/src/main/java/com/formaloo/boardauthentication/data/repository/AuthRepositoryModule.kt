package com.formaloo.boardauthentication.data.repository

import com.formaloo.boardauthentication.data.remote.remoteAuthModulConstant
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authRepositoryModule = module(override = true) {

    single<AuthRepo>(named(authRepoConstants.RepoName)) {
        AuthRepoImpl(
            get(named(remoteAuthModulConstant.DataSourceName)),
            get(),
            get(), get(), get()
        )
    }

}


object authRepoConstants {
    val RepoName = "AuthRepo"

}



