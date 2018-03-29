package com.strv.graphql.ui.base

import android.support.annotation.StringRes
import android.widget.Toast

import com.strv.graphql.GraphqlApplication.Companion.context

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            2/21/2018
 * Description:     Base view class to be inherited by all views
 */
interface BaseView {
    fun showToast(@StringRes stringRes: Int, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, stringRes, length).show()
    }

    fun showToast(string: String, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, string, length).show()
    }

    fun finish()
}
