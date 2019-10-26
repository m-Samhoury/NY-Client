package com.moustafa.nyclient.repository

import com.moustafa.nyclient.model.NYArticle
import com.moustafa.nyclient.repository.network.NYArticlesService

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

class RepositoryImpl(val service: NYArticlesService) : Repository {

    override suspend fun fetchArticlesList(searchQuery: String): List<NYArticle>? =
        safeApiCall({
            service.fetchArticlesList(searchQuery, "")
        }, {

        })
}
