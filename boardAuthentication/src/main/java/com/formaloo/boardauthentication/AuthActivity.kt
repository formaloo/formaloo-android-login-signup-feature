package com.formaloo.boardauthentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.formaloo.boardauthentication.databinding.ActivityAuthBinding
import com.formaloo.boardauthentication.databinding.ActivityProfileBinding
import com.formaloo.boardauthentication.ui.login.LoginFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentSign = LoginFragment().newInstance(
            intent.extras?.getString("form_slug") ?: "",
            intent.extras?.getString("board_address") ?: "",
            intent.extras?.getString("board_slug") ?: "",
            intent.extras?.getString("board_title") ?: "",
        )
        supportFragmentManager.beginTransaction()
            .add(R.id.auth_container, fragmentSign, "fragmentSign")
            .commit()


        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }
}

