package com.formaloo.boardauthentication.ui.profile.fields.holder

import android.view.View
import com.formaloo.boardauthentication.databinding.LayoutUiFieldHeaderBinding
import com.formaloo.boardauthentication.databinding.LayoutUiSectionItemBinding

class SectionViewHolder(itemView: View) : BaseVH(itemView) {

    val binding = LayoutUiSectionItemBinding.bind(itemView)
    val headerBinding = LayoutUiFieldHeaderBinding.bind(binding.root)

    override fun initView() {
        fillHeaderData(headerBinding.keyTxv, headerBinding.descTxv)
        initPlusViews(
            binding.sectionLay, null, null
        )
    }


}
