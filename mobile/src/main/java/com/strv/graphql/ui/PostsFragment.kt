package com.strv.graphql.ui

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.DialogInterface
import android.util.Log
import android.view.*
import android.widget.EditText
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.strv.graphql.*

import com.strv.graphql.databinding.FragmentPostsBinding
import com.strv.graphql.fragment.PostInfo
import com.strv.graphql.ui.base.BaseFragment
import com.strv.graphql.ui.base.BaseView
import com.strv.graphql.ui.base.BaseViewModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            04/03/18
 * Description:     A list fragment
 */
interface PostsView : BaseView {
    val postItemBinding: ItemBinding<PostInfo>
}

interface UpvoteClickedListener {
    fun upvote(post: PostInfo)
}

class PostsFragment : BaseFragment<PostsView, PostsViewModel, FragmentPostsBinding>(), PostsView, UpvoteClickedListener {
    override fun upvote(post: PostInfo) {
        showToast("Upvote $post")
    }

    override val postItemBinding: ItemBinding<PostInfo> = ItemBinding.of<PostInfo>(BR.post, R.layout.item_post)
            .bindExtra(BR.upvoteClickedListener, this)

    override fun setupViewModel(): PostsViewModel {
        return ViewModelProviders.of(this, PostsViewModel.Factory()).get(PostsViewModel::class.java)
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentPostsBinding = FragmentPostsBinding.inflate(inflater)

    companion object {
        fun newInstance(): PostsFragment {
            return PostsFragment()
        }
    }

    fun showCreatePostDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = layoutInflater
        val viewInflated = inflater.inflate(R.layout.dialog_create_post, null)

        val author = viewInflated.findViewById(R.id.author) as EditText
        val imageUrlInput = viewInflated.findViewById(R.id.imageUrl) as EditText
        val descriptionInput = viewInflated.findViewById(R.id.description) as EditText

        builder.setView(viewInflated)
                .setPositiveButton("Post") { dialog, id ->
                    val description = descriptionInput.text.toString()
                    val imageUrl = imageUrlInput.text.toString()
                    val author = author.text.toString()

                    // TODO: 8. step: add create post mutation
                    viewModel.createPost(author, description, imageUrl)
                }
                .setNegativeButton("Cancel") { dialog, id -> }

        builder.show()
    }


}

class PostsViewModel : BaseViewModel<PostsView>() {
    val posts = DiffObservableList<PostInfo>(object : DiffObservableList.Callback<PostInfo> {
        override fun areItemsTheSame(oldItem: PostInfo?, newItem: PostInfo?) = oldItem == newItem
        override fun areContentsTheSame(oldItem: PostInfo, newItem: PostInfo) = oldItem.hashCode() == newItem.hashCode()
    })

    init {
        loadData()
    }

    var getAllPostsRequest: Disposable? = null
    fun loadData() {
        // TODO: Use RxJava
        getAllPostsRequest?.dispose()
        getAllPostsRequest = Rx2Apollo.from(GraphqlApplication.apolloClient.query(AllPostsQuery.builder()
                .build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .watcher())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.data()?.let {
                        posts.update(it.allPosts().map { it.fragments().postInfo() })
                    }
                }
    }

    fun createPost(author: String, description: String, imageUrl: String) {
        GraphqlApplication.apolloClient.mutate(CreatePostMutation.builder()
                .author(author)
                .imageUrl(imageUrl)
                .text(description)
                .build()).enqueue(object : ApolloCall.Callback<CreatePostMutation.Data>() {
                    override fun onResponse(dataResponse: Response<CreatePostMutation.Data>) {
                        loadData()
                    }
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }
                })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = PostsViewModel() as T
    }
}
