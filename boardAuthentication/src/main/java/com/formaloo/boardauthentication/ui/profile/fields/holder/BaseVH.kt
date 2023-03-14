package com.formaloo.boardauthentication.ui.profile.fields.holder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.boardauthentication.data.model.Fields
import com.formaloo.boardauthentication.data.model.Form
import com.formaloo.boardauthentication.data.model.submitForm.RenderedData
import com.formaloo.boardauthentication.ui.profile.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import timber.log.Timber

open class BaseVH(itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {

    private val _errStatus = MutableLiveData<Boolean>().apply { value = null }
    val errStatus: LiveData<Boolean> = _errStatus

    internal var lifeCycleOwner: LifecycleOwner
    internal var fromEdit: Boolean = false
    internal var locationChanged: Boolean = false
    internal var position_: Int = -1
    internal lateinit var field: Fields
    internal lateinit var form: Form
    internal lateinit var viewmodel: ProfileViewModel
    internal var fieldRenderedData: RenderedData? = null
    internal var rowRenderedData: Map<String, RenderedData>? = null

    lateinit var highLightedView: View
    var errLay: View? = null
    var errorTxv: TextView? = null

    fun initPlusViews(
        _highLightedView: View,
        _errLay: RelativeLayout?,
        _errorTxv: TextView?
    ) {
        highLightedView = _highLightedView
        errLay = _errLay
        errorTxv = _errorTxv

    }


    init {
        setIsRecyclable(false)
        itemView.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(false)
            true
        }
        lifeCycleOwner = itemView.context as LifecycleOwner


    }

    internal fun hideErr(errLay: View, viewmodel: ProfileViewModel) {
        errLay.visibility = View.GONE
        viewmodel.setErrorToField(field.slug ?: "", "")

    }

    fun bindItems(
        field: Fields,
        position_: Int,
        form: Form,
        viewmodel: ProfileViewModel,
        rowRenderedData: Map<String, RenderedData>,
    ) {
        this.rowRenderedData = rowRenderedData
        this.viewmodel = viewmodel
        this.form = form
        this.position_ = position_
        this.field = field

        rowRenderedData.forEach {
            if (it.key.equals(field.slug)) {
                fieldRenderedData = it.value
            }

        }

        lifeCycleOwner.lifecycleScope.launch {
            viewmodel.profileUIState.collectLatest {
                Timber.e("RRprofileUIState $it")

                it.fieldsWithError.keys.forEach { errFSlug ->
                    Timber.e("errFSlug $errFSlug")
                    Timber.e("field.slug ${field.slug}")

                    if (errFSlug == field.slug || errFSlug == "phone") {
                        val error=it.fieldsWithError[errFSlug]
                        Timber.e("error $error")
                        errLay?.visibility = View.VISIBLE
                        errorTxv?.text = error
                    }
                }
            }
        }


        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }

        setIsRecyclable(false)

        initView()
    }

    open fun initView() {

    }

    fun fillHeaderData(titleTxv: TextView, descTxv: TextView) {
        val title = field.title
        titleTxv.text = if (field.required == true) {
            "$title *"
        } else {
            title
        }

        if (field.description?.isNotEmpty() == true) {
//            fieldDesc(descTxv, field)

        } else {
            descTxv.visibility = View.GONE
        }

    }


    fun displayErrLay(error: String) {



    }


}
