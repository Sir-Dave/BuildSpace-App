package com.example.buildspace.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.buildspace.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

}