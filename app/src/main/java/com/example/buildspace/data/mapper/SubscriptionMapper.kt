package com.example.buildspace.data.mapper

import com.example.buildspace.data.local.SubscriptionEntity
import com.example.buildspace.data.local.SubscriptionHistoryEntity
import com.example.buildspace.data.local.SubscriptionPlanEntity
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

fun SubscriptionDto.toSubscriptionEntity(timestamp: Long): SubscriptionEntity{
    return SubscriptionEntity(
        id = id,
        startDate = startDate,
        endDate = endDate,
        type = type,
        amount = amount,
        expired = expired,
        timestamp = timestamp
    )
}

fun SubscriptionEntity.toSubscription(): Subscription{
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

    val formattedAmount = amount/100.0

    return SubscriptionHistory(
        id = id,
        amount = String.format("%.2f", formattedAmount),
        reference = reference,
        date = formattedDate,
        status = status,
        userEmail = userEmail,
        subscriptionType = subscriptionType,
        currency = currency,
        isSuccess = statusType != Status.CANCELLED
    )
}

fun SubscriptionHistoryEntity.toSubscriptionHistory(): SubscriptionHistory {
    val inputFormat = "yyyy-MM-dd HH:mm:ss"
    val outputFormat = "dd/MM/yyyy"
    val inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat, Locale.getDefault())

    val localDateTime = LocalDateTime.parse(date, inputFormatter)
    val formattedDate = localDateTime.format(outputFormatter)

    val statusType = getEnumName<Status>(status)

    val formattedAmount = amount/100.0

    return SubscriptionHistory(
        id = id,
        amount = String.format("%.2f", formattedAmount),
        reference = reference,
        date = formattedDate,
        status = status,
        userEmail = userEmail,
        subscriptionType = subscriptionType,
        currency = currency,
        isSuccess = statusType != Status.CANCELLED
    )
}

fun SubscriptionHistoryDto.toSubscriptionHistoryEntity(): SubscriptionHistoryEntity {
    return SubscriptionHistoryEntity(
        id = id,
        amount = amount,
        reference = reference,
        date = date,
        status = status,
        userEmail = userEmail,
        subscriptionType = subscriptionType,
        currency = currency,
    )
}

fun SubscriptionPlanDto.toSubscriptionPlan(): SubscriptionPlan{
    return SubscriptionPlan(
        name = name,
        amount = amount.toInt().toString(),
        numberOfDays = numberOfDays
    )
}

fun SubscriptionPlanEntity.toSubscriptionPlan(): SubscriptionPlan {
    return SubscriptionPlan(
        name = name,
        amount = amount.toInt().toString(),
        numberOfDays = numberOfDays
    )
}

fun SubscriptionPlanDto.toSubscriptionPlanEntity(): SubscriptionPlanEntity{
    return SubscriptionPlanEntity(
        name = name,
        amount = amount,
        numberOfDays = numberOfDays
    )
}


