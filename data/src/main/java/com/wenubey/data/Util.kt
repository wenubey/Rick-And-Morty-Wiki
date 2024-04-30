package com.wenubey.data

fun String.getIdFromUrl(): Int {
   return if (this.isNotBlank()) {
        this.substringAfterLast("/").toInt()
    } else {
        -1
   }

}

fun List<String>.getIdFromUrls(): List<Int> {
    return this.map { it.getIdFromUrl() }
}