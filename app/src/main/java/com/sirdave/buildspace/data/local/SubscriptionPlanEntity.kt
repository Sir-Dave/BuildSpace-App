package com.sirdave.buildspace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SubscriptionPlanEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val amount: Double,
    val numberOfDays: Int
)