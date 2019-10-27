package com.moustafa.nyclient.repository.network

import com.moustafa.nyclient.BuildConfig
import com.moustafa.nyclient.model.NYArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

interface NYArticlesService {

    @GET("search/v2/articlesearch.json")
    suspend fun fetchArticlesList(
        @Query("page") page: Int,
        @Query("q") query: String = "",
        @Query("fq") filter: String? = null,
        @Query("sort") sort: String? = "newest",
        @Query("api-key") apiKey: String = BuildConfig.NY_API_KEY
    ): Response<NYArticlesResponse>
}
