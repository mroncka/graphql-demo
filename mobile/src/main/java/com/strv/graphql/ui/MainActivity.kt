package com.strv.graphql.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import com.strv.graphql.R
import com.strv.graphql.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val postsFragment = PostsFragment()
    val peopleFragment = PeopleFragment()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(postsFragment, R.id.container)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(peopleFragment, R.id.container)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_add -> {
                postsFragment.showCreatePostDialog()
                true
            }
            R.id.menu_refresh -> {
                postsFragment.viewModel.loadData()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.navigation_home
        }
    }
}
