package com.strv.graphql.utility

import android.databinding.BindingAdapter
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
//import com.strv.graphql.R

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            2/27/18
 * Description:     TODO: add description
 */
object ImageUtility {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        //Glide.with(GraphqlApplication.context)
        Glide.with(view.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .into(view)
    }

    private val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//            .error(R.drawable.ic_account_circle_gray_24dp)
}
