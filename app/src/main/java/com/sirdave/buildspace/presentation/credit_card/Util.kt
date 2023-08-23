package com.sirdave.buildspace.presentation.credit_card

fun formatCardNumber(input: String): String {
    var formattedText = input
    formattedText = formattedText.replace(Regex("\\D"), "")

    // Calculate the number of groups needed (grouping in multiples of 4)
    val remainingDigits = formattedText.length % 4
    val groups = formattedText.length / 4

    // Build the formatted string with grouped digits
    val formattedStringBuilder = StringBuilder()
    for (i in 0 until groups) {
        val start = i * 4
        val end = (i + 1) * 4
        formattedStringBuilder.append(formattedText.substring(start, end))
        formattedStringBuilder.append(" ")
    }

    // Append the remaining digits
    if (remainingDigits > 0) {
        formattedStringBuilder.append(formattedText.substring(groups * 4))
    } else {
        // Remove the trailing space if no remaining digits
        formattedStringBuilder.deleteCharAt(formattedStringBuilder.length - 1)
    }

    return formattedStringBuilder.toString()
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

fun formatCvv(input: String): String {
    var formattedText = input
    formattedText = formattedText.replace(Regex("\\D"), "")
    return formattedText.take(3)
}

fun formatPin(input: String): String {
    var formattedText = input
    formattedText = formattedText.replace(Regex("\\D"), "")
    return formattedText.take(4)
}

fun getMonthAndYear(date: String): Pair<String, String>{
    val fields = date.split("/")
    return Pair(fields[0], fields[1]) //(month, year)
}

fun stripFormatting(input: String): String {
    return input.replace(Regex("\\D"), "")
}

private fun String.insert(index: Int, other: String): String {
    return StringBuilder(this).insert(index, other).toString()
}