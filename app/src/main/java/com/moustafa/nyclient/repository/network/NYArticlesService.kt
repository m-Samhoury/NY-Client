package com.moustafa.nyclient.repository.network

import com.moustafa.nyclient.model.NYArticle
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

interface NYArticlesService {

    @GET("articlesearch.json?q={query}&fq={filter}")
    suspend fun fetchArticlesList(
        @Path("query") query: String,
        @Path("filter") filter: String
    ): Response<List<NYArticle>>
}
