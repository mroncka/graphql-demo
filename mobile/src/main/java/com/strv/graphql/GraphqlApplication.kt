package com.strv.graphql

import android.app.Application
import android.content.Context

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            04/03/18
 * Description:     Application class, allows setup of necessary services before the application starts and provides access to application context
 */
class GraphqlApplication : Application() {
    companion object {
        private lateinit var instance: GraphqlApplication
        val context: Context get() = instance
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

    }
}