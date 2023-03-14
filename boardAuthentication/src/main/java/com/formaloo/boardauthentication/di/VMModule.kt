package com.formaloo.boardauthentication.di


import com.formaloo.boardauthentication.data.repository.authRepoConstants
import com.formaloo.boardauthentication.ui.login.LoginViewModel
import com.formaloo.boardauthentication.ui.login.PasswordViewModel
import com.formaloo.boardauthentication.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authVMModule = module {
    viewModel { LoginViewModel(get(named(authRepoConstants.RepoName))) }
    viewModel { ProfileViewModel(get(named(authRepoConstants.RepoName))) }
    viewModel { PasswordViewModel(get(named(authRepoConstants.RepoName))) }

}
