package com.formaloo.boardAuthTest.di

import com.formaloo.boardAuthTest.data.BoardRepo
import com.formaloo.boardAuthTest.data.BoardRepoImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module(override = true) {

    single<BoardRepo>(named(boardsRepoConstants.RepoName)) {
        BoardRepoImpl(
            get(named(remoteBoardsModulConstant.DataSourceName)),
        )
    }

}


object boardsRepoConstants {
    val RepoName = "BoardsRepo"

}



