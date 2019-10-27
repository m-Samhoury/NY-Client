package com.moustafa.nyclient.articleslisttest

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.moustafa.nyclient.model.NYArticle
import com.moustafa.nyclient.repository.Repository
import com.moustafa.nyclient.ui.articleslist.ArticlesListViewModel
import com.moustafa.nyclient.ui.misc.AsyncState
import com.moustafa.nyclient.utils.CoroutineRule
import com.moustafa.nyclient.utils.LiveDataTestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test

/**
 * Some tests to make sure the ArticlesViewModel is emitting the right states for the UI
 * to be then rendered
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class ArticlesListScreenTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    internal lateinit var articlesListViewModel: ArticlesListViewModel

    private val currentViewModelState
        get() = LiveDataTestUtil.getValue(articlesListViewModel.stateLiveData)

    @Test
    fun `get articles list triggers loading state`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchArticlesList(
                searchQuery: String,
                onError: (Exception) -> Unit
            ): List<NYArticle>? = emptyList()

        }

        coroutinesTestRule.pauseDispatcher()

        articlesListViewModel = ArticlesListViewModel(repository)

        assertThat(currentViewModelState.articlesListAsyncState is AsyncState.Loading).isTrue()
        coroutinesTestRule.resumeDispatcher()

    }

    @Test
    fun `get articles list and return success`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchArticlesList(
                searchQuery: String,
                onError: (Exception) -> Unit
            ): List<NYArticle>? = seedDummyArticles(5)


        }
        coroutinesTestRule.pauseDispatcher()

        articlesListViewModel = ArticlesListViewModel(repository)

        assertThat(currentViewModelState.articlesListAsyncState is AsyncState.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.articlesListAsyncState is AsyncState.Loaded).isTrue()
        assertThat((currentViewModelState.articlesListAsyncState as AsyncState.Loaded).result)
            .hasSize(5)
    }

    @Test
    fun `get articles list and return failure`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchArticlesList(
                searchQuery: String,
                onError: (Exception) -> Unit
            ): List<NYArticle>? {

                withContext<List<NYArticle>>(Dispatchers.Main) {
                    onError(NetworkErrorException("No internet"))
                    return@withContext emptyList()
                }
                return null
            }

        }
        coroutinesTestRule.pauseDispatcher()

        articlesListViewModel = ArticlesListViewModel(repository)

        assertThat(currentViewModelState.articlesListAsyncState is AsyncState.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.articlesListAsyncState is AsyncState.Failed).isTrue()
        assertThat((currentViewModelState.articlesListAsyncState as AsyncState.Failed).failed)
            .isInstanceOf(NetworkErrorException::class.java)
    }

    @Test
    fun `filter articles list and return result`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchArticlesList(
                searchQuery: String,
                onError: (Exception) -> Unit
            ): List<NYArticle>? = seedDummyArticles(5).filter {
                it.abstract?.contains(other = searchQuery, ignoreCase = false) == true
                        || it.snippet?.contains(other = searchQuery, ignoreCase = false) == true
            }
        }

        articlesListViewModel = ArticlesListViewModel(repository)

        articlesListViewModel.queriedFetchNYArticles("article #1")
        coroutinesTestRule.pauseDispatcher()
        delay(400)  //delay the same debounce timeOut

        assertThat((currentViewModelState.articlesListAsyncState as AsyncState.Loaded<List<NYArticle>>).result)
            .hasSize(1)
        assertThat((currentViewModelState.articlesListAsyncState as AsyncState.Loaded).result[0].snippet)
            .isEqualTo("article #1")

        coroutinesTestRule.resumeDispatcher()
    }

    @Test
    fun `filter articles list and return empty`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchArticlesList(
                searchQuery: String,
                onError: (Exception) -> Unit
            ): List<NYArticle>? = seedDummyArticles(5).filter {
                it.abstract?.contains(other = searchQuery, ignoreCase = false) == true
                        || it.snippet?.contains(other = searchQuery, ignoreCase = false) == true
            }
        }

        articlesListViewModel = ArticlesListViewModel(repository)

        articlesListViewModel.queriedFetchNYArticles("Lebanon")
        coroutinesTestRule.pauseDispatcher()
        delay(400)  //delay the same debounce timeOut

        assertThat((currentViewModelState.articlesListAsyncState as AsyncState.Loaded).result)
            .hasSize(0)

        coroutinesTestRule.resumeDispatcher()
    }
}

private fun seedDummyArticles(numberOfArticles: Int): List<NYArticle> {
    val articlesList = ArrayList<NYArticle>()
    repeat(numberOfArticles) { index: Int ->
        articlesList.add(NYArticle(snippet = "article #$index"))
    }
    return articlesList
}