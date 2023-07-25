package com.example.buildspace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubscriptionEntity (
    @PrimaryKey val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val amount: Double,
    val expired: Boolean,
    val timestamp: Long
)
