package com.strv.graphql.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.databinding.ViewDataBinding
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment

import com.strv.graphql.BR

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            2/21/2018
 * Description:     Base fragment class to be inherited by all MVVM fragments
 */
abstract class BaseFragment<T : BaseView, A : BaseViewModel<T>, B : ViewDataBinding> : Fragment() {
    lateinit var viewModel: A
    lateinit var binding: B

    val baseActivity: BaseActivity?
        get() = activity as BaseActivity?

    abstract fun setupViewModel(): A
    abstract fun inflateBindingLayout(inflater: LayoutInflater): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = setupViewModel()
        viewModel.onBindView(this as T)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = inflateBindingLayout(inflater)
        binding.setVariable(BR.view, this)
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    fun showSnackbar(string: String, length: Int = Snackbar.LENGTH_LONG) {
        view?.let { Snackbar.make(it, string, length).show() }
    }

    open fun finish() {
        baseActivity?.finish()
    }
}
