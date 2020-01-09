package ir.seven.jalas.utilities

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

fun Date.toSimpleDateFormat(): String {
    return SimpleDateFormat(DATE_FORMAT).format(this)
}