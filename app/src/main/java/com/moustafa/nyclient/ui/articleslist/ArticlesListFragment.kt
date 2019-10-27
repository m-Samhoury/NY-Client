package com.moustafa.nyclient.ui.articleslist

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moustafa.nyclient.R
import com.moustafa.nyclient.base.BaseFragment
import com.moustafa.nyclient.model.NYArticle
import com.moustafa.nyclient.ui.misc.AsyncState
import com.moustafa.nyclient.utils.ItemDecorationCustomMargins
import com.moustafa.nyclient.utils.RecyclerViewInfinitScrollListener
import com.moustafa.nyclient.utils.setExpansionAnimation
import kotlinx.android.synthetic.main.fragment_articles_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

class ArticlesListFragment : BaseFragment(R.layout.fragment_articles_list) {

    private val articlesListViewModel: ArticlesListViewModel by viewModel()
    private val articlesListAdapter = ArticlesListAdapter()
    private val infiniteScrollListener by lazy {
        RecyclerViewInfinitScrollListener(
            lifecycleScope,
            threshold = 3,
            rateLimitIgnoreWithin = 2_000L,
            onEndReached = { articlesListViewModel.nextPageNYArticles() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesListViewModel.stateLiveData.observe(this, Observer {
            handleState(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        setupSearchView(searchItem)
    }

    private fun setupSearchView(searchItem: MenuItem?) {
        val searchView = (searchItem?.actionView as? SearchView)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                articlesListViewModel.queriedFetchNYArticles(searchQuery = query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                articlesListViewModel.queriedFetchNYArticles(newText ?: "")
                return true
            }
        })

        val searchBar = searchItem?.actionView
        if (searchBar != null) {
            searchItem.setExpansionAnimation(
                revealAnimation = {
                    val cx: Double = searchBar.measuredWidth.toDouble()
                    val cy: Double = searchBar.measuredHeight / 2.0

                    val finalRadius = Math.hypot(cx, cy).toFloat()

                    ViewAnimationUtils.createCircularReveal(
                        searchBar,
                        cx.toInt(),
                        cy.toInt(),
                        0f,
                        finalRadius
                    )
                },
                collapseAnimation = {
                    val cx: Double = searchBar.measuredWidth.toDouble()
                    val cy: Double = searchBar.measuredHeight / 2.0

                    val finalRadius = Math.hypot(cx, cy).toFloat()

                    ViewAnimationUtils.createCircularReveal(
                        searchBar,
                        cx.toInt(),
                        cy.toInt(),
                        finalRadius,
                        0f
                    )
                },
                searchBarBackground = R.drawable.rectangular_background_rounded_corners,
                searchCloseButtonColor = Color.BLACK
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                item.expandActionView()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleState(state: ArticlesListState) =
        when (val articlesListState = state.articlesListAsyncState) {
            AsyncState.Init -> {
                showLoading(false)
                infiniteScrollListener.setLoadInProgress(false)
            }
            AsyncState.Loading -> {
                infiniteScrollListener.setLoadInProgress(true)
                showLoading(true)
            }
            is AsyncState.Loaded -> {
                infiniteScrollListener.setLoadInProgress(false)
                showLoading(false)
                populateArticlesList(articlesListState.result)
            }
            is AsyncState.Failed -> {
                infiniteScrollListener.setLoadInProgress(false)
                showLoading(false)
                showError(articlesListState.failed) {
                    articlesListViewModel.nextPageNYArticles()
                }
            }
        }

    private fun populateArticlesList(list: List<NYArticle>) {
        articlesListAdapter.submitList(list)
    }

    override fun setupViews(rootView: View) {
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerViewArticlesList.apply {
            layoutManager = linearLayoutManager
            adapter = articlesListAdapter
            addItemDecoration(
                ItemDecorationCustomMargins(
                    top = 8, bottom = 8,
                    start = 16, end = 16
                )
            )
        }
        recyclerViewArticlesList.addOnScrollListener(infiniteScrollListener)
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
