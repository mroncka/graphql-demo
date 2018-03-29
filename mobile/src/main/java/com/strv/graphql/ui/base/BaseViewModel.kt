package com.strv.graphql.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            2/21/2018
 * Description:     Base viewModel class to be inherited by all viewModels
 */
abstract class BaseViewModel<T: BaseView> : ViewModel(), Observable {
    private val observableCallbacks = PropertyChangeRegistry()
    internal var view: T? = null

    fun onBindView(view: T?) {
        this.view = view
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        observableCallbacks.add(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        observableCallbacks.remove(callback)
    }

    @Synchronized
    public fun notifyChange() {
        observableCallbacks.notifyCallbacks(this, 0, null)
    }

    public fun notifyPropertyChanged(fieldId: Int) {
        notifyPropertyChanged(fieldId)
    }
}
