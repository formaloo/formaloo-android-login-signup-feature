package com.formaloo.boardauthentication.ui.profile.fields.holder

import android.view.View
import com.formaloo.boardauthentication.databinding.LayoutUiEmptyItemBinding

class EmptyViewHolder(itemView: View) : BaseVH(itemView) {

    val binding = LayoutUiEmptyItemBinding.bind(itemView)

    override fun initView() {
        initPlusViews(binding.seclay, null,null)

    }


}
