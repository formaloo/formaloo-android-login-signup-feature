package com.formaloo.boardAuthTest.di

import com.formaloo.boardAuthTest.BoardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val boardModule = module {
    viewModel { BoardViewModel(get(named(boardsRepoConstants.RepoName))) }
}
