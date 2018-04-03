package com.strv.graphql

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore

import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory

import okhttp3.OkHttpClient
import java.io.File

import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.strv.graphql.type.CustomType
import com.apollographql.apollo.CustomTypeAdapter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            04/03/18
 * Description:     Application class, allows setup of necessary services before the application starts and provides access to application context
 */
class GraphqlApplication : Application() {
    companion object {
        private const val BASE_URL = "https://api.graph.cool/simple/v1/cjdk5y6n31b4j0162pg5p1i6f"
        //private const val BASE_URL = "https://githunt-api.herokuapp.com/graphql"
        private const val SQL_CACHE_NAME = "db_name"

        private lateinit var instance: GraphqlApplication
        val context: Context get() = instance
        val apolloClient: ApolloClient get() = instance.apolloClient
    }

    init {
        instance = this
    }

    lateinit var apolloClient: ApolloClient

    // TODO: Add interceptor to authorize queries with access token
    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            //.addInterceptor { chain ->
            //    val original = chain.request()
            //    val builder = original.newBuilder().method(original.method(), original.body())
            //    builder.header("Authorization", UserManager.accessToken)
            //    chain.proceed(builder.build())
            //}
            .build()

    override fun onCreate() {
        super.onCreate()


        // TODO: Access generated classes
        //EntryDetailQuery

        // TODO: Add http cache
        // Directory where cached responses will be stored
        val file = File("/cache/")

        // Size in bytes of the cache
        val size = 1024L * 1024L

        // Create the http response cache store
        val cacheStore = DiskLruHttpCacheStore(file, size)

        // TODO: Add normalized cache
        // Create the ApolloSqlHelper. Please note that if null is passed in as the name, you will get an in-memory SqlLite database that
        // will not persist across restarts of the app.
        val apolloSqlHelper = ApolloSqlHelper.create(context, SQL_CACHE_NAME)

        //Create NormalizedCacheFactory
//        val cacheFactory = SqlNormalizedCacheFactory(apolloSqlHelper)
//        val normalizedCacheFactory = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION, SqlNormalizedCacheFactory(apolloSqlHelper))

        //Create the cache key resolver, this example works well when all types have globally unique ids.
//        val cacheKeyResolver = object : CacheKeyResolver() {
//            override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
//                return formatCacheKey(recordSet["id"] as String)
//            }
//
//            override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
//                return formatCacheKey(field.resolveArgument("id", variables) as String?)
//            }
//
//            private fun formatCacheKey(id: String?): CacheKey {
//                return if (id == null || id.isEmpty()) {
//                    CacheKey.NO_KEY
//                } else {
//                    CacheKey.from(id)
//                }
//            }
//        }

        val customTypeAdapter = object : CustomTypeAdapter<Date> {
            val ISO8601DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSS", Locale.getDefault())
            override fun decode(value: String): Date {
                try {
                    return ISO8601DateFormat.parse(value)
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }

            }

            override fun encode(value: Date): String {
                return ISO8601DateFormat.format(value)
            }
        }

        // TODO: 4. step: build apollo client
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .httpCache(ApolloHttpCache(cacheStore))
                .addCustomTypeAdapter(CustomType.DATETIME, customTypeAdapter)
                //.normalizedCache(cacheFactory, cacheKeyResolver)
                .build()


        // TODO: Prefetch data
        //apolloClient.prefetch()
    }
}