package com.sirdave.buildspace.presentation.credit_card

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun formatCardNumber(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i % 4 == 3 && i != 15) {
                append(" ")
            }
        }
        toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 1
            if (offset <= 11) return offset + 2
            if (offset <= 16) return offset + 3
            return 19
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 1
            if (offset <= 14) return offset - 2
            if (offset <= 19) return offset - 3
            return 16
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}

fun formatExpirationDate(input: String): String {
    var formattedText = input
    formattedText = formattedText.replace(Regex("\\D"), "")

    // Add the "/" separator after the 2nd character
    if (formattedText.length > 2) {
        formattedText = formattedText.insert(2, "/")
    }

    return formattedText
}

fun getMonthAndYear(date: String): Pair<String, String>{
    val fields = date.split("/")
    return Pair(fields[0], fields[1]) //(month, year)
}

private fun String.insert(index: Int, other: String): String {
    return StringBuilder(this).insert(index, other).toString()
}