package com.example.model

/**
 * Cambodian Primary School Mathematics (MoEYS / VVOB 2021)
 * វិធីសាស្ត្របង្រៀនគណិតវិទ្យា ផ្នែកចំនួន ថ្នាក់ទី៣
 */
data class Chapter(
    val id: Int,
    val titleKhmer: String,
    val descriptionKhmer: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val chapterId: Int,
    val titleKhmer: String,
    val subtitleKhmer: String,
    val keyTermsKhmer: List<String>,
    val descriptionKhmer: String
)

object KhmerNumberUtil {
    private val khmerDigits = arrayOf('០', '១', '២', '៣', '៤', '៥', '៦', '៧', '៨', '៩')

    fun toKhmer(number: Int): String {
        return number.toString().map { ch ->
            if (ch in '0'..'9') khmerDigits[ch - '0'] else ch
        }.joinToString("")
    }

    fun toKhmer(text: String): String {
        return text.map { ch ->
            if (ch in '0'..'9') khmerDigits[ch - '0'] else ch
        }.joinToString("")
    }

    fun fromKhmer(khmerText: String): Int? {
        val converted = khmerText.map { ch ->
            val idx = khmerDigits.indexOf(ch)
            if (idx != -1) ('0' + idx) else ch
        }.joinToString("").filter { it.isDigit() }
        return converted.toIntOrNull()
    }

    // Convert integer up to 10,000 into spoken Khmer words (ការអានតាមខ្ទង់)
    fun toKhmerWords(n: Int): String {
        if (n == 0) return "សូន្យ"
        if (n == 10000) return "មួយម៉ឺន (ដប់ពាន់)"

        val thousands = n / 1000
        val remainderThousand = n % 1000
        val hundreds = remainderThousand / 100
        val remainderHundred = remainderThousand % 100
        val tens = remainderHundred / 10
        val ones = remainderHundred % 10

        val parts = mutableListOf<String>()

        if (thousands > 0) {
            parts.add(digitName(thousands) + "ពាន់")
        }
        if (hundreds > 0) {
            parts.add(digitName(hundreds) + "រយ")
        } else if (thousands > 0 && (tens > 0 || ones > 0)) {
            parts.add("សូន្យរយ")
        }

        if (tens > 0) {
            parts.add(tensName(tens))
        }
        if (ones > 0) {
            if (tens > 0 && ones == 1) {
                parts.add("មួយ")
            } else {
                parts.add(digitName(ones))
            }
        }

        return parts.joinToString("")
    }

    private fun digitName(d: Int): String = when (d) {
        1 -> "មួយ"
        2 -> "ពីរ"
        3 -> "បី"
        4 -> "បួន"
        5 -> "ប្រាំ"
        6 -> "ប្រាំមួយ"
        7 -> "ប្រាំពីរ"
        8 -> "ប្រាំបី"
        9 -> "ប្រាំបួន"
        else -> ""
    }

    private fun tensName(t: Int): String = when (t) {
        1 -> "ដប់"
        2 -> "ម្ភៃ"
        3 -> "សាមសិប"
        4 -> "សែសិប"
        5 -> "ហាសិប"
        6 -> "ហុកសិប"
        7 -> "ចិតសិប"
        8 -> "ប៉ែតសិប"
        9 -> "កៅសិប"
        else -> ""
    }
}
