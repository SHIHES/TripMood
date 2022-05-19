package com.shihs.tripmood.ext

import android.icu.text.SimpleDateFormat
import java.util.*

/**
 * Created by Wayne Chen on 2020-01-15.
 */
fun Long.toDisplayDateFormat(): String {
    return SimpleDateFormat("yyyy.MM.dd", Locale.TAIWAN).format(this)
}

fun Long.toDisplayTimeFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(this)
}