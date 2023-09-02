package com.sirdave.buildspace.data.mapper

import android.icu.text.DecimalFormat
import com.sirdave.buildspace.data.local.SubscriptionEntity
import com.sirdave.buildspace.data.local.SubscriptionHistoryEntity
import com.sirdave.buildspace.data.local.SubscriptionPlanEntity
import com.sirdave.buildspace.data.remote.dto.response.SubscriptionDto
import com.sirdave.buildspace.data.remote.dto.response.SubscriptionHistoryDto
import com.sirdave.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.sirdave.buildspace.domain.model.Subscription
import com.sirdave.buildspace.domain.model.SubscriptionHistory
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.util.Status
import com.sirdave.buildspace.util.getEnumName
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
        amount = formatAmount(amount, true),
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
        amount = formatAmount(amount, true),
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
        amount = formatAmount(formattedAmount, true),
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
        amount = formatAmount(formattedAmount, true),
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
        amount = formatAmount(amount),
        numberOfDays = numberOfDays,
        type = type
    )
}

fun SubscriptionPlanEntity.toSubscriptionPlan(): SubscriptionPlan {
    return SubscriptionPlan(
        name = name,
        amount = formatAmount(amount),
        numberOfDays = numberOfDays,
        type = type
    )
}

fun SubscriptionPlanDto.toSubscriptionPlanEntity(): SubscriptionPlanEntity{
    return SubscriptionPlanEntity(
        name = name,
        amount = amount,
        numberOfDays = numberOfDays,
        type = type
    )
}

fun formatAmount(amount: Double, toDecimal: Boolean = false): String {
    val formatter = if (toDecimal)
        DecimalFormat("#,###.00")

    else DecimalFormat("#,###")
    return formatter.format(amount)
}

