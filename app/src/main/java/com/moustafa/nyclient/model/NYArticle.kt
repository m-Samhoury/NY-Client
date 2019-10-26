package com.moustafa.nyclient.model

import com.moustafa.nyclient.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */
@JsonClass(generateAdapter = true)
data class NYArticle(
    @field:Json(name = "web_url")
    val webUrl: String? = null,
    @field:Json(name = "snippet")
    val snippet: String? = null,
    @field:Json(name = "lead_paragraph")
    val leadParagraph: String? = null,
    @field:Json(name = "abstract")
    val abstract: String? = null,
    @field:Json(name = "source")
    val source: String? = null,
    @field:Json(name = "pub_date")
    val publishDate: Date? = null,
    @field:Json(name = "type_of_material")
    val materialType: String? = null,
    @field:Json(name = "word_count")
    val wordCount: Long? = null,
    @field:Json(name = "multimedia")
    val multimedia: List<Multimedia>? = null
) {
    val thumbnailUrl
        get() = multimedia?.firstOrNull() {
            val typeIsImage = TYPE_IMAGE.equals(other = it.type, ignoreCase = true)
            val subtypeIsThumbnail = SUBTYPE_THUMBNAIL.equals(other = it.subtype, ignoreCase = true)
            typeIsImage && subtypeIsThumbnail
        }?.url?.let { BuildConfig.BASE_IMAGE_URL + it }

    val largeUrl
        get() = multimedia?.firstOrNull() {
            val typeIsImage = TYPE_IMAGE.equals(other = it.type, ignoreCase = true)
            val subtypeIsXLarge = SUBTYPE_X_LARGE.equals(other = it.subtype, ignoreCase = true)
            typeIsImage && subtypeIsXLarge
        }?.url?.let { BuildConfig.BASE_IMAGE_URL + it }

    companion object {
        const val TYPE_IMAGE = "image"
        const val SUBTYPE_THUMBNAIL = "thumbnail"
        const val SUBTYPE_X_LARGE = "xlarge"
    }
}

@JsonClass(generateAdapter = true)
data class Multimedia(
    @field:Json(name = "type")
    val type: String? = null,
    @field:Json(name = "subtype")
    val subtype: String? = null,
    @field:Json(name = "url")
    val url: String? = null,
    @field:Json(name = "height")
    val height: Int? = null,
    @field:Json(name = "width")
    val width: Int? = null
)
