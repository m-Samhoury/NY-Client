package com.moustafa.nyclient.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author moustafasamhoury
 * created on Saturday, 26 Oct, 2019
 */

class DateAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
    @ToJson
    @Synchronized
    fun dateToJson(d: Date): String {
        return dateFormat.format(d)
    }


    @FromJson
    @Synchronized
    @Throws(ParseException::class)
    fun jsonToDate(s: String): Date {
        return dateFormat.parse(s)
    }
}
