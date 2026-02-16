package com.example.firstjetpackapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firstjetpackapp.model.Users
import com.example.firstjetpackapp.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {

    private val _loginSuccess = mutableStateOf(false)

    val loginSuccess : State<Boolean> = _loginSuccess

    private val _registrationSuccess = mutableStateOf(false)

    val registrationSuccess:State<Boolean> = _registrationSuccess

    fun registerUser(username: String, password: String, phnnumber: String, email: String) {
        viewModelScope.launch {
            repository.registerUser(Users(username = username, password = password, phnnumber = phnnumber, email = email))
            _registrationSuccess.value = true
        }
    }

    fun loginUser(username: String, password: String)
    {
        viewModelScope.launch {
            _loginSuccess.value = repository.loginUser(username,password)
        }
    }
}

class AuthViewModelFactory(private val repository: AuthRepository): ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>):T{
        return AuthViewModel(repository) as T
    }
}