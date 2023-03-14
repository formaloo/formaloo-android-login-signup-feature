package com.formaloo.boardAuthTest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.formaloo.boardauthentication.AuthActivity
import com.formaloo.boardauthentication.ui.login.LoginViewModel
import com.formaloo.boardauthentication.ui.profile.ProfileActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

//    const val SHAREDBOARDADDRESS = "bPLwBcyrHBBcTXT" //Business
const val SHAREDBOARDADDRESS = "UpJRSYuTZ1YAptS" //BusinessStaging

class MainActivity : AppCompatActivity(), KoinComponent {
    private val boardVM: BoardViewModel by viewModel()
    private val authVM by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boardVM.refreshBoard(SHAREDBOARDADDRESS)

        val cookie = authVM.retrieveCookie()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardVM.uiState.collectLatest {
                    BoardDataIsReady(it,cookie)


                }
            }
        }


    }

    private fun BoardDataIsReady(it: BoardUiState,cookie:String?) {
        it.board?.let {board->
            val hasLogin = board.config?.get("users_settings_enabled")?.asBoolean ?: false

            finish()

            if (hasLogin && cookie?.isNotEmpty()==true){
                val intent= Intent(this, ProfileActivity::class.java)
                intent.putExtra("board_address",board.share_address)
                startActivity(intent)
            }else{
                val intent= Intent(this, AuthActivity::class.java)
                intent.putExtra("board_address",board.share_address)
                intent.putExtra("board_slug",board.slug)
                intent.putExtra("board_title",board.title)
                intent.putExtra("form_slug",board.user_form?.slug)
                startActivity(intent)
            }
        }

    }


}
