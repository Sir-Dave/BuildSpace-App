package com.example.buildspace.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface BuildSpaceDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertSubscription(subscriptionEntity: SubscriptionEntity)

    @Query("SELECT * FROM subscriptionentity LIMIT 1")
    suspend fun getCurrentSubscription(): SubscriptionEntity?

    @Query("DELETE FROM subscriptionentity")
    suspend fun clearSubscription()

    @Insert(onConflict = REPLACE)
    suspend fun insertSubscriptionPlans(plans: List<SubscriptionPlanEntity>)

    @Query("SELECT * FROM subscriptionplanentity")
    suspend fun getSubscriptionPlans(): List<SubscriptionPlanEntity>

    @Query("DELETE FROM subscriptionplanentity")
    suspend fun clearPlans()

    @Insert(onConflict = REPLACE)
    suspend fun insertSubscriptionHistory(history: List<SubscriptionHistoryEntity>)

    @Query("SELECT * FROM SubscriptionHistoryEntity")
    suspend fun getSubscriptionHistory(): List<SubscriptionHistoryEntity>

    @Query("DELETE FROM subscriptionhistoryentity")
    suspend fun clearHistory()
}