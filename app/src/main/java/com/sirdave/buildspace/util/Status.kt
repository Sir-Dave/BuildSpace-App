package com.sirdave.buildspace.util

enum class Status {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}

enum class SubscriptionType{
    DAILY,
    WEEKLY,
    MONTHLY
}

inline fun <reified T : Enum<T>> getEnumName(name: String): T {
    check (isValidEnum<T>(name)){
        throw IllegalStateException("Invalid ${T::class.simpleName} name")
    }
    return enumValueOf(name.uppercase())
}

inline fun <reified T : Enum<T>> isValidEnum(name: String): Boolean {
    return enumValues<T>().any { enum -> enum.name.equals(name, ignoreCase = true) }
}