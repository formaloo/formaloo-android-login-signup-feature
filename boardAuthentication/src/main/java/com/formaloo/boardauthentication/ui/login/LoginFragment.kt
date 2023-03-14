package com.formaloo.boardauthentication.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.formaloo.boardauthentication.R
import com.formaloo.boardauthentication.databinding.FragmentAuthBinding
import com.formaloo.boardauthentication.databinding.LayoutSignInEmailBinding
import com.formaloo.boardauthentication.databinding.LayoutSignUpEmailBinding
import com.formaloo.boardauthentication.ui.profile.ProfileActivity
import com.formaloo.boardauthentication.ui.setErrorMessage
import com.formaloo.boardauthentication.ui.showMsg
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var boardSlug: String? = null
    private var boardAddress: String? = null
    private var boardTitle: String? = null
    private var formSlug: String? = null

    private val loginVM: LoginViewModel by viewModel()
    private lateinit var binding: FragmentAuthBinding
    private lateinit var signInBinding: LayoutSignInEmailBinding
    private lateinit var signUpBinding: LayoutSignUpEmailBinding


    fun newInstance(
        formSlug: String,
        boardAddress: String,
        boardSlug: String,
        boardTitle: String
    ): LoginFragment {
        val args = Bundle()
        args.putString("form_slug", formSlug)
        args.putString("board_address", boardAddress)
        args.putString("board_slug", boardSlug)
        args.putString("board_title", boardTitle)
        val fragment = LoginFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.signInEmailLay.isVisible) {
                    openSignUp()
                } else {
                    isEnabled = false
                    if (activity != null)
                        (requireActivity()).onBackPressed()

                }
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        signInBinding = LayoutSignInEmailBinding.bind(binding.root)
        signUpBinding = LayoutSignUpEmailBinding.bind(binding.root)

        boardSlug = arguments?.getString("board_slug")
        boardAddress = arguments?.getString("board_address")
        boardTitle = arguments?.getString("board_title")
        formSlug = arguments?.getString("form_slug")

        initViews()
        setupListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInBinding.SignInNameEdt.addTextChangedListener {
            it?.let {
                signInBinding.SignInNameLayout.error = ""
            }
        }
        signInBinding.SignInPasswordEdt.addTextChangedListener {
            it?.let {
                signInBinding.SignInPassLayout.error = ""
            }
        }
        signUpBinding.signUpNameEdt.addTextChangedListener {
            it?.let {
                signUpBinding.signUpNameLayout.error = ""
            }
        }
        signUpBinding.signUpPasswordEdt.addTextChangedListener {
            it?.let {
                signUpBinding.signUpPassLayout.error = ""
            }
        }
        signUpBinding.signUpPhoneNumberEdt.addTextChangedListener {
            it?.let {
                signUpBinding.profilePhoneNumberLayout.error = ""
            }
        }
        signUpBinding.signUpEmailEdt.addTextChangedListener {
            it?.let {
                signUpBinding.profileEmailLayout.error = ""
            }
        }

        lifecycleScope.launch {
            loginVM.signUpUIState.collectLatest {
                if (it.openProfile) {
                    openProfile()
                }

                setErrorMessage(signUpBinding.signUpNameLayout, it.firsNameErr)
                setErrorMessage(signUpBinding.signUpPassLayout, it.passErr)
                setErrorMessage(signUpBinding.profilePhoneNumberLayout, it.phoneErr)
                setErrorMessage(signUpBinding.profileEmailLayout, it.emailErr)

                signUpBinding.upProgress.visibility = View.GONE

                if (it.errMsg.isNotEmpty()) {
                    showMsg(binding.titleTxv, it.errMsg)
                }
            }
        }
        lifecycleScope.launch {
            loginVM.loginUIState.collectLatest {
                if (it.openProfile) {
                    openProfile()

                }
                setErrorMessage(signInBinding.SignInNameLayout, it.userErr)
                setErrorMessage(signInBinding.SignInPassLayout, it.passErr)

                signInBinding.inProgress.visibility = View.GONE

                if (it.errMsg.isNotEmpty()) {
                    showMsg(binding.titleTxv, it.errMsg)
                }
            }

        }
    }

    private fun openProfile() {
        requireActivity().finish()
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("board_address", boardAddress ?: "")
        startActivity(intent)

    }

    private fun initViews() {
        signInBinding.forgotPassBtn.setOnClickListener {
            openForgotPassPage()
        }

        changeTitle("Signup", boardTitle)

    }

    private fun openForgotPassPage() {
        val fragmentResetPass = ForgotPassFragment().newInstance(formSlug ?: "", boardSlug ?: "")
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.auth_container, fragmentResetPass, "fragmentResetPass")
            .addToBackStack("fragmentResetPass")
            .commit()

    }


    private fun setupListeners() {
        val signUpPasswordEdt = signUpBinding.signUpPasswordEdt
        val signUpEmailEdt = signUpBinding.signUpEmailEdt
        val signUpPhoneNumberEdt = signUpBinding.signUpPhoneNumberEdt
        val signUpNameEdt = signUpBinding.signUpNameEdt

        val signInPasswordEdt = signInBinding.SignInPasswordEdt
        val signInNameEdt = signInBinding.SignInNameEdt


        signInBinding.SignInBtn.setOnClickListener {
            signInBinding.inProgress.visibility = View.VISIBLE

            val req = HashMap<String, String>()
            req["username"] = signInNameEdt.text.toString()
            req["password"] = signInPasswordEdt.text.toString()
            req["originator"] = boardSlug ?: ""
            loginVM.login(req)
        }

        signUpBinding.signUpBtn.setOnClickListener {
            signUpBinding.upProgress.visibility = View.VISIBLE

            val req = HashMap<String, String>()
            req["first_name"] = signUpNameEdt.text.toString()

            if (signUpPhoneNumberEdt.text.toString().isNotEmpty() == true)
                req["phone_number"] = signUpPhoneNumberEdt.text.toString()

            if (signUpEmailEdt.text.toString().isNotEmpty() == true)
                req["email"] = signUpEmailEdt.text.toString()

            req["password"] = signUpPasswordEdt.text.toString()

            req["originator"] = boardSlug ?: ""


            loginVM.signUp(req)
        }

        signInBinding.openSignUpPageBtn.setOnClickListener {
            openSignUp()
        }

        binding.signInEmailIncludeLay.openSignUpPageBtn.setOnClickListener {
            openSignUp()
        }




        signUpBinding.termsTxt.movementMethod = LinkMovementMethod.getInstance()

        signUpBinding.openSignPageBtn.setOnClickListener {
            openLogin()
        }

    }

    private fun openSignUp() {

        binding.signUpEmailLay.visibility = VISIBLE
        binding.signInEmailLay.visibility = GONE

        changeTitle("Signup", boardTitle)

    }

    private fun openLogin() {
        binding.signInEmailLay.visibility = VISIBLE
        binding.signUpEmailLay.visibility = GONE

        changeTitle("Login", boardTitle)
    }

    private fun changeTitle(fixedTitle: String, boardTitle: String?) {

        binding.boardTitleTxv.isVisible = boardTitle?.isNotEmpty() == true

         if (boardTitle?.isNotEmpty() == true) {
            binding.titleTxv.text =   fixedTitle + " to "
             binding.boardTitleTxv.text =boardTitle
        } else {
            binding.titleTxv.text =  fixedTitle
        }



    }


}
