package com.protecciontotal.rickandmorty.utils

fun extractIdFromUrl(url: String): Int? {
    val regex = Regex("""/(\d+)$""")
    val matchResult = regex.find(url)
    return matchResult?.groups?.get(1)?.value?.toIntOrNull()
}
