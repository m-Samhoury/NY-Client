package com.moustafa.nyclient.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author moustafasamhoury
 * created on Sunday, 27 Oct, 2019
 */

fun Date.formatted(): String = SimpleDateFormat("dd MMM 'at' HH:mm", Locale.US).format(this)