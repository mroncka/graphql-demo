package com.strv.graphql.ui.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.strv.graphql.R

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            2/21/2018
 * Description:     Base activity class to be inherited by all MVVM activities
 */
abstract class BaseActivity : AppCompatActivity() {
    fun replaceFragment(fragment: Fragment, container: Int = R.id.container, addToBackStack: Boolean = false, fragmentManager: FragmentManager = supportFragmentManager) {
        val transaction = fragmentManager.beginTransaction()
                .replace(container, fragment, fragment.javaClass.simpleName)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }

        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }

        super.onBackPressed()
    }
}
