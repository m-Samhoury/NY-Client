package com.moustafa.nyclient.ui.articleslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moustafa.nyclient.model.NYArticle
import com.moustafa.nyclient.repository.Repository
import com.moustafa.nyclient.ui.misc.AsyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

data class ArticlesListState(
    val articlesListAsyncState: AsyncState<List<NYArticle>> = AsyncState.Init
)

class ArticlesListViewModel(
    private val repository: Repository,
    private val state: ArticlesListState = ArticlesListState()
) : ViewModel() {
    private var searchFor = ""
    private var currentPage = 1
    private var currentList: List<NYArticle> = ArrayList<NYArticle>()

    private val _stateLiveData = MutableLiveData<ArticlesListState>()
    val stateLiveData: LiveData<ArticlesListState> = _stateLiveData

    init {
        fetchNYArticles(currentPage)
    }

    private fun fetchNYArticles(page: Int, searchQuery: String = "") {
        if (state.articlesListAsyncState is AsyncState.Loading) return

        _stateLiveData.value = state.copy(articlesListAsyncState = AsyncState.Loading)

        viewModelScope.launch(Dispatchers.Main) {
            val response = repository.fetchArticlesList(page, searchQuery) {
                _stateLiveData.value =
                    state.copy(articlesListAsyncState = AsyncState.Failed(it))
            }
            if (response != null) {
                val shouldAppend = page > 1
                currentPage = page + 1

                currentList = if (shouldAppend) {
                    currentList.plus(response)
                } else {
                    response
                }
                _stateLiveData.value =
                    state.copy(articlesListAsyncState = AsyncState.Loaded(currentList))
            }
        }
    }

    fun queriedFetchNYArticles(searchQuery: String) {
        val searchText = searchQuery.trim()
        if (searchText == searchFor) {
            return
        }

        searchFor = searchText
        viewModelScope.launch(Dispatchers.Main) {
            delay(400)  //debounce timeOut
            if (searchQuery != searchFor) {
                return@launch
            }

            fetchNYArticles(1, searchQuery)
        }
    }

    fun nextPageNYArticles(nextPage: Int) {
        if (state.articlesListAsyncState !is AsyncState.Loading) {
            fetchNYArticles(nextPage, searchFor)
        }
    }
}
