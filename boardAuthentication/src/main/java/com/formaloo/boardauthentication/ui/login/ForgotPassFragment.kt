package com.formaloo.boardauthentication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.formaloo.boardauthentication.databinding.FragmentForgotPasswordBinding
import com.formaloo.boardauthentication.ui.setErrorMessage
import com.formaloo.boardauthentication.ui.showMsg
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [BaseFragment] subclass
 * that will show a list of top from Github's API.
 */
class ForgotPassFragment : Fragment() {

    private var boardSlug: String? = null
    private var formSlug: String? = null

    // FOR DATA
    private val viewModel: PasswordViewModel by viewModel()
    private lateinit var binding: FragmentForgotPasswordBinding

    fun newInstance(
        formSlug: String,
        boardSlug: String,
    ): ForgotPassFragment {
        val args = Bundle()
        args.putString("form_slug", formSlug)
        args.putString("board_slug", boardSlug)
        val fragment = ForgotPassFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formSlug=arguments?.getString("form_slug")
        boardSlug=arguments?.getString("board_slug")

        lifecycleScope.launch {
            viewModel.reqState.collectLatest {
                hideLoading()

                if (it.openConfirm) {
                    binding.confirmPassLay.visibility = View.VISIBLE
                    binding.resetPassLay.visibility = View.GONE
                }
                if (it.contactErr.isNotEmpty()) {
                    setErrorMessage(binding.changeUsernameInputLayout, it.contactErr)

                }

                if (it.errMsg.isNotEmpty()) {
                    showMsg(binding.title, it.errMsg)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.confState.collectLatest {
                hideLoading()
                if (it.confirmed) {
                    requireActivity().onBackPressed()
//                            openLogin()
                }

                if (it.passwordErr.isNotEmpty()) {
                    setErrorMessage(binding.newPasswordInputLayout, it.passwordErr)

                }
                if (it.tokenErr.isNotEmpty()) {
                    setErrorMessage(binding.confirmCodeInputLayout, it.tokenErr)

                }


                if (it.errMsg.isNotEmpty()) {
                    showMsg(binding.title, it.errMsg)
                }
            }
        }




        binding.chanePassUsernameEdt.doOnTextChanged { text, start, before, count ->
            binding.changeUsernameInputLayout.error = ""
        }
        binding.confirmCodeEdt.doOnTextChanged { text, start, before, count ->
            binding.confirmCodeInputLayout.error = ""
        }
        binding.newPasswordEdt.doOnTextChanged { text, start, before, count ->
            binding.newPasswordInputLayout.error = ""
        }

        binding.forgotPassBtn.setOnClickListener {
            showLoading()

            if (viewModel.reqState.value.openConfirm) {

                val req = hashMapOf(
                    "contact" to binding.chanePassUsernameEdt.text.toString(),
                    "token" to binding.confirmCodeEdt.text.toString(),
                    "web_token" to viewModel.reqState.value.web_token,
                    "password" to binding.newPasswordEdt.text.toString(),
                    "domain" to (formSlug?:"")

                )
                viewModel.confirmPass(req)

            } else {

                val req = hashMapOf(
                    "contact" to binding.chanePassUsernameEdt.text.toString(),
                    "domain" to (formSlug?:"")
                )
                viewModel.resetPassRequest(req)

            }
        }

    }

    private fun showLoading() {
        binding.inProgress.visibility = View.VISIBLE
        binding.forgotPassBtn.visibility = View.GONE

    }

    private fun hideLoading() {
        binding.inProgress.visibility = View.GONE
        binding.forgotPassBtn.visibility = View.VISIBLE

    }


}
