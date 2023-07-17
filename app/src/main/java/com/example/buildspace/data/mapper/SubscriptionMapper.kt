package com.example.buildspace.data.mapper

import com.example.buildspace.data.remote.dto.response.SubscriptionDto
import com.example.buildspace.data.remote.dto.response.SubscriptionHistoryDto
import com.example.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.example.buildspace.domain.model.Subscription
import com.example.buildspace.domain.model.SubscriptionHistory
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.util.Status
import com.example.buildspace.util.getEnumName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun SubscriptionDto.toSubscription(): Subscription{
    val inputFormat = "yyyy-MM-dd HH:mm:ss"
    val outputFormat = "dd/MM/yyyy"
    val inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat, Locale.getDefault())

    val localStartDateTime = LocalDateTime.parse(startDate, inputFormatter)
    val localEndDateTime = LocalDateTime.parse(endDate, inputFormatter)
    val formattedStartDate = localStartDateTime.format(outputFormatter)
    val formattedEndDate = localEndDateTime.format(outputFormatter)

    return Subscription(
        id = id,
        startDate = formattedStartDate,
        endDate = formattedEndDate,
        type = type,
        amount = String.format("%.2f", amount),
        expired = expired
    )

}

fun SubscriptionHistoryDto.toSubscriptionHistory(): SubscriptionHistory {
    val inputFormat = "yyyy-MM-dd HH:mm:ss"
    val outputFormat = "dd/MM/yyyy"
    val inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat, Locale.getDefault())

    val localDateTime = LocalDateTime.parse(date, inputFormatter)
    val formattedDate = localDateTime.format(outputFormatter)

    val statusType = getEnumName<Status>(status)

    return SubscriptionHistory(
        id = id,
        amount = String.format("%.2f", amount),
        reference = reference,
        date = formattedDate,
        status = status,
        userEmail = userEmail,
        subscriptionType = subscriptionType,
        currency = currency,
        isSuccess = statusType != Status.CANCELLED
    )
}

fun SubscriptionPlanDto.toSubscriptionPlan(): SubscriptionPlan{
    return SubscriptionPlan(
        name = name,
        amount = String.format("%.2f", amount),
        numberOfDays = numberOfDays
    )

}