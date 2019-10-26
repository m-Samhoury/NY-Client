package com.moustafa.nyclient.ui.articleslist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moustafa.nyclient.R
import com.moustafa.nyclient.base.BaseFragment
import com.moustafa.nyclient.model.NYArticle
import com.moustafa.nyclient.ui.misc.AsyncState
import com.moustafa.nyclient.utils.ItemDecorationCustomMargins
import kotlinx.android.synthetic.main.fragment_articles_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

class ArticlesListFragment : BaseFragment(R.layout.fragment_articles_list) {

    private val articlesListViewModel: ArticlesListViewModel by viewModel()
    private val articlesListAdapter = ArticlesListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesListViewModel.stateLiveData.observe(this, Observer {
            handleState(it)
        })
    }

    private fun handleState(state: ArticlesListState) =
        when (val articlesListState = state.articlesListAsyncState) {
            AsyncState.Init -> {
                showLoading(false)
            }
            AsyncState.Loading -> {
                showLoading(true)
            }
            is AsyncState.Loaded -> {
                showLoading(false)
                populateArticlesList(articlesListState.result)
            }
            is AsyncState.Failed -> {
                showLoading(false)
                showError(articlesListState.failed, articlesListState.action)
            }
        }

    private fun populateArticlesList(list: List<NYArticle>) {
        articlesListAdapter.submitList(list)
    }

    override fun setupViews(rootView: View) {
        recyclerViewArticlesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articlesListAdapter
            addItemDecoration(
                ItemDecorationCustomMargins(
                    top = 8, bottom = 8,
                    start = 16, end = 16
                )
            )
        }
    }

    private fun showLoading(shouldShow: Boolean) {
        if (shouldShow) {
            progressBarLoadingArticles.show()
        } else {
            progressBarLoadingArticles.hide()
        }
    }

    private fun showError(throwable: Throwable, action: (() -> Any)? = null) {
        val snackBar = Snackbar.make(
            constraintLayoutRoot,
            throwable.message ?: getString(R.string.generic_error_unknown),
            Snackbar.LENGTH_SHORT
        )
        if (action != null) {
            snackBar.setAction(R.string.action_retry) {
                action.invoke()
            }
            snackBar.duration = Snackbar.LENGTH_INDEFINITE
        }
        snackBar.show()
    }

}
