package com.moustafa.nyclient.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Saturday, 26 Oct, 2019
 */
@JsonClass(generateAdapter = true)
data class NYArticlesResponse(
    @field:Json(name = "status")
    var status: String? = null,
    @field:Json(name = "copyright")
    var copyright: String? = null,
    @field:Json(name = "response")
    var response: NYResponse? = null
) {
    val success
        get() = "ok" == status?.toLowerCase()
}

@JsonClass(generateAdapter = true)
data class NYResponse(
    @field:Json(name = "docs")
    var docs: List<NYArticle>? = null,
    @field:Json(name = "meta")
    var meta: NYMetaResponse? = null
)

@JsonClass(generateAdapter = true)
data class NYMetaResponse(
    @field:Json(name = "hits")
    var hits: Long? = null,
    @field:Json(name = "offset")
    var offset: Int? = null,
    @field:Json(name = "time")
    var time: Int? = null
)
