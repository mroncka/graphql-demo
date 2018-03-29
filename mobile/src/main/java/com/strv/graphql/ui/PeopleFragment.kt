package com.strv.graphql.ui

import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.view.*

import com.strv.graphql.R
import com.strv.graphql.BR

import com.strv.graphql.databinding.FragmentListBinding
import com.strv.graphql.entity.Person
import com.strv.graphql.ui.base.BaseFragment
import com.strv.graphql.ui.base.BaseView
import com.strv.graphql.ui.base.BaseViewModel
import io.reactivex.disposables.Disposable

import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            04/03/18
 * Description:     A list fragment
 */
interface PeopleView : BaseView {
    val personItemBinding: ItemBinding<Person>
}


class PeopleFragment : BaseFragment<PeopleView, PeopleViewModel, FragmentListBinding>(), PeopleView {
    override val personItemBinding: ItemBinding<Person> = ItemBinding.of<Person>(BR.person, R.layout.item_person)
                //.bindExtra(BR.personClickedListener, this)

    //override fun onPersonClicked(personId: Int) {
    //    startActivity(PersonActivity.newInstance(activity, personId))
    //}

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
    val people = DiffObservableList<Person>(object : DiffObservableList.Callback<Person> {
        override fun areItemsTheSame(oldItem: Person?, newItem: Person?) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem.hasSameContentAs(newItem)
    })

    init {
        loadData()
    }

    var getAllPeopleRequest: Disposable? = null
    private fun loadData() {
        getAllPeopleRequest?.dispose()
        // TODO: Replace retrofit call with graphQL
//        getAllPeopleRequest = PeopleProvider.service.getAllPeople()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    it.body()?.let {
//                        people.set(it)
//                    }
//                }, {
//                    it.printStackTrace()
//                })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = PeopleViewModel() as T
    }
}
