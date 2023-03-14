package com.formaloo.boardauthentication.ui.profile

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.submitForm.SubmitedRow
import com.formaloo.boardauthentication.databinding.ActivityProfileBinding
import com.formaloo.boardauthentication.ui.login.LoginViewModel
import com.formaloo.boardauthentication.ui.showMsg
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class ProfileActivity : AppCompatActivity() {
    private var profileRow: SubmitedRow? = null
    val profileVM: ProfileViewModel by viewModel()
    val logVM: LoginViewModel by viewModel()

    private var formAddress: String? = null
    private var boardAddress: String? = null
    private lateinit var binding: ActivityProfileBinding
    private lateinit var fieldsLM: LinearLayoutManager
    private lateinit var fieldsUiAdapter: FieldsUiAdapter
    private var formFromBundle: Form? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkBundle()

        initData()

        setUpListeners()

    }

    private fun checkBundle() {
        intent.extras?.let { extra ->
            boardAddress = extra.getString("board_address")

        }

    }

    private fun initView(form: Form, row: SubmitedRow) {
        val rowRenderedData = row.rendered_data ?: HashMap()
        fieldsUiAdapter = FieldsUiAdapter(form, profileVM, rowRenderedData)
        fieldsLM = LinearLayoutManager(this@ProfileActivity)

        binding.viewsRec.apply {
            adapter = fieldsUiAdapter
            layoutManager = fieldsLM
        }

        form.fields_list?.let {
            val fields = it.filter {
                it.type != "variable" && it.type != "hidden"
            }

            fieldsUiAdapter.setCollection(ArrayList(fields))

            it.map {
                val required = it.required
                if (required != null && required) {
//                    profileVM.reuiredField(it)

                } else {

                }

            }

            binding.fromSubmitBtn.visibility = View.VISIBLE

        }

        binding.fromSubmitBtn.visibility = View.VISIBLE


    }

    private fun initData() {
        profileVM.getProfile(boardAddress ?: "")

        this.lifecycleScope.launch {
            profileVM.profileUIState.collectLatest {
                profileDataChanged(it)
            }


        }




    }

    private fun profileDataChanged(it: ProfileUiState) {
        with(it) {
            Timber.e("profileDataChanged ")

            profile?.let { row ->
                profileRow = row
                formAddress = row.form?.address
                if (formAddress != null) {
                    if (formFromBundle == null)
                        profileVM.getProfileForm(formAddress ?: "")

                }
            }

            if (profileSaved) {
                formFromBundle = profileForm

                if (profileRow != null) {
                    initView(formFromBundle!!, profileRow!!)
                }

            }

            if (profileChanged) {
                val msg = formFromBundle?.success_message ?: "Updates Saved"
                showMsg(binding.root, msg)
            }

            hideLoading()


        }


    }

    private fun hideLoading() {
        binding.phoneProgress.visibility = View.GONE
        binding.fromSubmitBtn.visibility = View.VISIBLE

    }

    private fun showLoading() {
        binding.phoneProgress.visibility = View.VISIBLE
        binding.fromSubmitBtn.visibility = View.GONE

    }

    private fun setUpListeners() {
        binding.fromSubmitBtn.setOnClickListener {
            showLoading()
            profileVM.editProfile(boardAddress ?: "")
        }
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }

    }


}
