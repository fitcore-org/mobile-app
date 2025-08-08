package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitcore.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserStateViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun setUser(user: User) {
        _user.value = user
    }
}
