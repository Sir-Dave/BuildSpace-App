package com.sirdave.buildspace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SubscriptionHistoryEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val reference: String,
    val date: String,
    val status: String,
    val userEmail: String,
    val subscriptionType: String,
    val currency: String
)