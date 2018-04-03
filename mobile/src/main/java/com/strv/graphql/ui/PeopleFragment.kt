package com.strv.graphql.ui

import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.view.*
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.strv.graphql.AllPeopleQuery

import com.strv.graphql.R
import com.strv.graphql.BR
import com.strv.graphql.GraphqlApplication

import com.strv.graphql.databinding.FragmentListBinding
import com.strv.graphql.entity.Person
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
interface PeopleView : BaseView {
    val personItemBinding: ItemBinding<AllPeopleQuery.AllPerson>
}

interface PersonClickedListener {
    fun onPersonClicked(person: AllPeopleQuery.AllPerson)
}

class PeopleFragment : BaseFragment<PeopleView, PeopleViewModel, FragmentListBinding>(), PeopleView, PersonClickedListener {
    override fun onPersonClicked(person: AllPeopleQuery.AllPerson) {
        showToast("Person clicked $person")
    }

    override val personItemBinding: ItemBinding<AllPeopleQuery.AllPerson> = ItemBinding.of<AllPeopleQuery.AllPerson>(BR.person, R.layout.item_person)
        .bindExtra(BR.personClickedListener, this)

    override fun setupViewModel(): PeopleViewModel {
        return ViewModelProviders.of(this, PeopleViewModel.Factory()).get(PeopleViewModel::class.java)
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentListBinding = FragmentListBinding.inflate(inflater)

    companion object {
        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }
}


class PeopleViewModel : BaseViewModel<PeopleView>() {
//    val people = DiffObservableList<Person>(object : DiffObservableList.Callback<Person> {
//        override fun areItemsTheSame(oldItem: Person?, newItem: Person?) = oldItem == newItem
//        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem.hasSameContentAs(newItem)
//    })

    val people = DiffObservableList<AllPeopleQuery.AllPerson>(object : DiffObservableList.Callback<AllPeopleQuery.AllPerson> {
        override fun areItemsTheSame(oldItem: AllPeopleQuery.AllPerson?, newItem: AllPeopleQuery.AllPerson?) = oldItem == newItem
        override fun areContentsTheSame(oldItem: AllPeopleQuery.AllPerson, newItem: AllPeopleQuery.AllPerson) = oldItem.hashCode() == newItem.hashCode()
    })

    init {
        loadData()
    }

    var getAllPeopleRequest: Disposable? = null
    private fun loadData() {
        getAllPeopleRequest?.dispose()
        // TODO: 5. step: Replace retrofit call with graphQL
        //getAllPeopleRequest = PersonProvider.service.getAllPeople()
        //        .subscribeOn(Schedulers.newThread())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe {
        //            it.body()?.let {
        //                people.set(it)
        //            }
        //        }


//        GraphqlApplication.apolloClient.query(AllPeopleQuery.builder()
//                .build())
//                .enqueue(object : ApolloCall.Callback<AllPeopleQuery.Data>() {
//                    override fun onResponse(response: Response<AllPeopleQuery.Data>) {
//                        response.data()?.let {
//                            people.update(it.allPersons())
//                        }
//                    }
//                    override fun onFailure(e: ApolloException) {
//                        e.printStackTrace()
//                    }
//                })

        getAllPeopleRequest = Rx2Apollo.from(GraphqlApplication.apolloClient.query(AllPeopleQuery.builder()
                .build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .watcher())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.data()?.let {
                        people.update(it.allPersons())
                    }
                }
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = PeopleViewModel() as T
    }
}
