package com.michaeludjiawan.moviedb.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.ui.movie.FilterType
import kotlinx.android.synthetic.main.filter_type_bottom_dialog.*

class FilterTypeBottomDialog : BottomSheetDialogFragment() {

    private val valueSelectedMutable: MutableLiveData<FilterType> = MutableLiveData()
    val valueSelected: LiveData<FilterType> = valueSelectedMutable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_type_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClickListener(tv_filter_type_btm_dialog_popular, FilterType.POPULAR)
        initOnClickListener(tv_filter_type_btm_dialog_upcoming, FilterType.UPCOMING)
        initOnClickListener(tv_filter_type_btm_dialog_top_rated, FilterType.TOP_RATED)
        initOnClickListener(tv_filter_type_btm_dialog_now_playing, FilterType.NOW_PLAYING)
    }

    private fun initOnClickListener(textView: TextView, filterType: FilterType) {
        textView.setOnClickListener {
            valueSelectedMutable.value = filterType
            dismiss()
        }
    }

}