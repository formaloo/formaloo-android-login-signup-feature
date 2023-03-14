package com.formaloo.boardauthentication.ui.profile.fields.holder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.formaloo.boardauthentication.databinding.LayoutUiFieldHeaderBinding
import com.formaloo.boardauthentication.databinding.LayoutUiNumberItemBinding
import com.google.android.material.textfield.TextInputEditText
import com.formaloo.boardauthentication.databinding.LayoutUiFieldFooterBinding

class NumberViewHolder(itemView: View) : BaseVH(itemView) {

    val binding = LayoutUiNumberItemBinding.bind(itemView)
    val footerBinding = LayoutUiFieldFooterBinding.bind(binding.root)
val headerBinding = LayoutUiFieldHeaderBinding.bind(binding.root)


    override fun initView(){
        initPlusViews(binding.editLay,binding.errLay, footerBinding.errorTxv)

        fillHeaderData(headerBinding.keyTxv,headerBinding.descTxv)


        val context = binding.errLay.context

        val value = fieldRenderedData?.value
        if (value != null) {
            binding.valueEdt.setText(value.toString())
            viewmodel.addKeyValueToReq(field.slug!!, value.toString())

        }
        else{

        }

        val decimalPlace = field.decimal_places
        if (decimalPlace?.toString()?.isEmpty()==true){
            setInputType(binding.valueEdt,InputType.TYPE_CLASS_NUMBER)
        }


        val default = field.default
        if (default != null)  {
            binding.valueEdt.setText(default.toString())
            viewmodel.addKeyValueToReq(field.slug!!, default.toString())

        }else{

        }

        if (fromEdit) {
            binding.valueEdt.keyListener = null
            binding.valueEdt.movementMethod = null
        } else {

        }


        val myTextWatcher = object : TextWatcher {
            private var position: Int = position_

            fun updatePosition(position: Int) {
                this.position = position
            }

            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i2: Int,
                i3: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                hideErr(binding.errLay, viewmodel)

            }

            override fun afterTextChanged(editable: Editable) {
                val input = editable.toString()
                if (field.type == "number" && input.isNotEmpty()) {
                    if (checkNumberInput(input)) {
                        viewmodel.addKeyValueToReq(field.slug!!, input)

                    } else {
                        viewmodel.setErrorToField(
                            field.slug?:"",
                            "Min value : ${field.min_value ?: "-"}" + "  " + "Max value : ${field.max_value ?: ""}"
                        )
                    }
                }else{
                    viewmodel.addKeyValueToReq(field.slug!!, input)

                }
            }

            private fun checkNumberInput(input: String): Boolean {
                val maxValue = field.max_value
                val minValue = field.min_value
                val isBigger = maxValue != null && input.toLong() > maxValue.toLong()
                val isSmaller = minValue != null && input.toLong() < minValue.toLong()

                return ! (isBigger || isSmaller)

            }
        }

        myTextWatcher.updatePosition(position_)
        binding.valueEdt.addTextChangedListener(myTextWatcher)



    }



    private fun setInputType(edt: TextInputEditText, type: Int) {
        edt.inputType = type
        edt.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        edt.gravity = Gravity.TOP or Gravity.START
        edt.text?.length?.let {
            edt.setSelection(it)
        }

    }

}
