package com.formaloo.boardauthentication.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.formaloo.boardauthentication.R
import com.formaloo.boardauthentication.data.model.Fields
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.submitForm.RenderedData
import com.formaloo.boardauthentication.ui.profile.fields.holder.BaseVH
import com.formaloo.boardauthentication.ui.profile.fields.holder.EmptyViewHolder
import com.formaloo.boardauthentication.ui.profile.fields.holder.NumberViewHolder
import com.formaloo.boardauthentication.ui.profile.fields.holder.SectionViewHolder
import com.formaloo.boardauthentication.ui.profile.fields.holder.TextViewHolder


class FieldsUiAdapter(
    private val form: Form,
    private val viewmodel: ProfileViewModel,
    private val rowRenderedData: Map<String, RenderedData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TYPE_EDT = 1
    private val TYPE_SECTION = 2
    private val TYPE_Number = 3

    internal var collection = arrayListOf<Fields>()

    fun setCollection(items: ArrayList<Fields>) {
        collection = items
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView: View
        when (viewType) {
            TYPE_EDT -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ui_edt_item, parent, false);
                return TextViewHolder(itemView)
            }

            TYPE_Number -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ui_number_item, parent, false);
                return NumberViewHolder(itemView)
            }

            TYPE_SECTION -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ui_section_item, parent, false);
                return SectionViewHolder(itemView)
            }

            else -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ui_section_item, parent, false);
                return EmptyViewHolder(itemView)

            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position_: Int) {

        val btnItem = collection[position_]

        (holder as BaseVH).bindItems(
            btnItem,
            position_,
            form,
            viewmodel,
            rowRenderedData,
        )

    }

    override fun getItemViewType(position: Int): Int {
        val fields = collection[position]
        val type = if (fields.sub_type != null) {
            fields.sub_type
        } else {
            fields.type
        }
        return when (type) {
            "number" -> {
                TYPE_Number
            }

            "section" -> {
                TYPE_SECTION

            }

            else -> {
                TYPE_EDT
            }
        }

    }

    override fun getItemCount(): Int {
        return collection.size

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

}


