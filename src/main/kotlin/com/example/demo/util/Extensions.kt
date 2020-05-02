package com.example.demo.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

/**
 * 生成 Slug
 * Slug 即是文章的 SEO 名称（eg. /blog/a-story 中的 a-story）
 */
fun generateSlug(dateTime: LocalDateTime, title: String): String {
    val date = dateTime.toLocalDate().toString()
    val titleSlug = Regex("[^A-Za-z0-9 ]").replace(title, "")
            .toLowerCase()
            .split(" ")
            .joinToString("-")
            .replace("-+".toRegex(), "-")
            .removeSuffix("-")
    // 这里偷了个懒，直接用 UUID 的 hashCode 作为尾缀
    val fullSlug = date + "-" + titleSlug + "-" + Integer.toHexString(UUID.randomUUID().hashCode())
    return fullSlug.replace("-+".toRegex(), "-")
}

// 以下是 Spring 官方 Kotlin 快速上手教程中对应的部分功能，对中文地区意义不大

/**
 *  将文章标题（英文）转化为 SEO 友好的字符串
 *  比如： example.com/blog/a-simple-story 后缀的 a-simple-story 即是一个 slug
 */
fun String.toSlug() = toLowerCase()
        .replace("\n", " ")
        .replace("[^a-z\\d\\s]".toRegex(), " ")
        .split(" ")
        .joinToString("-")
        .replace("-+".toRegex(), "-")
        .removeSuffix("-")

/**
 * 将日期文本格式化到英文表述格式
 */
fun LocalDateTime.formatToEnglishDate() = this.format(englishDateFormatter)

private val daysLookup = (1..31).associate { it.toLong() to getOrdinal(it) }

private val englishDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .appendLiteral(" ")
        .appendText(ChronoField.DAY_OF_MONTH, daysLookup)
        .appendLiteral(" ")
        .appendPattern("yyyy")
        .toFormatter(Locale.ENGLISH)

private fun getOrdinal(n: Int) = when {
    n in 11..13 -> "${n}th"
    n % 10 == 1 -> "${n}st"
    n % 10 == 2 -> "${n}nd"
    n % 10 == 3 -> "${n}rd"
    else -> "${n}th"
}
