package com.sirdave.buildspace.util

import android.icu.text.DecimalFormat

fun formatAmount(amount: Double, toDecimal: Boolean = false): String {
    val formatter = if (toDecimal)
        DecimalFormat("#,###.00")

    else DecimalFormat("#,###")
    return formatter.format(amount)
}