package com.example.buildspace.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.buildspace.di.dataStore
import com.example.buildspace.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_KEY = stringPreferencesKey("user")
        private val IS_REMEMBER_USER = booleanPreferencesKey("is_remember_user")
        private val IS_FIRST_LOGIN = booleanPreferencesKey("is_first_login")
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }


    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    fun getUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[USER_KEY]
            val user = Gson().fromJson(json, User::class.java)
            user
        }
    }

    suspend fun saveUser(user: User) {
        val json = Gson().toJson(user)
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = json
        }
    }

    suspend fun deleteUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }

    fun getUserLoginState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_REMEMBER_USER] ?: false
        }
    }

    suspend fun saveUserLoginState(){
        context.dataStore.edit { preferences ->
            preferences[IS_REMEMBER_USER] = true
        }
    }

    suspend fun clearUserLoginState(){
        context.dataStore.edit { preferences ->
            preferences.remove(IS_REMEMBER_USER)
        }
    }

    fun isFirstLogin(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_FIRST_LOGIN] ?: true
        }
    }

    suspend fun saveFirstLogin(){
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LOGIN] = false
        }
    }
}