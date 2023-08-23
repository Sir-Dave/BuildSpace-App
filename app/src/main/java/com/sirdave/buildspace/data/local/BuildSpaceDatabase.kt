package com.sirdave.buildspace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SubscriptionEntity::class,
        SubscriptionPlanEntity::class,
        SubscriptionHistoryEntity::class,
    ],
    version = 1
)
abstract class BuildSpaceDatabase: RoomDatabase() {
    abstract val dao : BuildSpaceDao
}