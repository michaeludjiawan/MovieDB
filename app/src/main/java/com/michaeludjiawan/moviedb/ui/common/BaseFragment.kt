package com.michaeludjiawan.moviedb.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected fun initDefaultToolbar(
        toolbarTitle: String = "",
        showBackButton: Boolean = true,
        homeAsUpIndicator: Int = 0
    ) {
        setHasOptionsMenu(true)

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            if (toolbarTitle.isBlank()) {
                setDisplayShowTitleEnabled(false)
            } else {
                setDisplayShowTitleEnabled(true)
                title = toolbarTitle
            }

            setHomeAsUpIndicator(homeAsUpIndicator)
            setDisplayHomeAsUpEnabled(showBackButton)
        }
    }

}