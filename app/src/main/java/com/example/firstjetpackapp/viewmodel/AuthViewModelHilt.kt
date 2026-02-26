package com.example.firstjetpackapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstjetpackapp.model.Users
import com.example.firstjetpackapp.repository.AuthRepositoryHilt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Tell Hilt this is a ViewModel it needs to manage
@HiltViewModel
// 2. Tell Hilt to automatically inject the AuthRepository here
class AuthViewModelHilt @Inject constructor(
    private val repository: AuthRepositoryHilt
): ViewModel() {
//    private val _loginSuccess = mutableStateOf(false)
//    val loginSuccessHilt : State<Boolean> = _loginSuccess

//    private val _registrationSuccess = mutableStateOf(false)
//    val registrationSuccessHilt:State<Boolean> = _registrationSuccess
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccessHilt: LiveData<Boolean> = _loginSuccess

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccessHilt: LiveData<Boolean> = _registrationSuccess

//    fun registerUserHilt(username: String, password: String, phnnumber: String, email: String) {
//        viewModelScope.launch {
//            repository.registerUserHilt(Users(username = username, password = password, phnnumber = phnnumber, email = email))
//            _registrationSuccess.value = true
//        }
//    }

    fun registerUserHilt(username: String, password: String, phnnumber: String, email: String)
    {
        viewModelScope.launch {
            repository.registerUserHilt(
                Users(username=username, password = password, phnnumber = phnnumber, email = email)
            )
            _registrationSuccess.postValue(true)
        }
    }

//    fun loginUserHilt(username: String, password: String) {
//        viewModelScope.launch {
//            _loginSuccess.value = repository.loginUserHilt(username,password)
//        }
//    }

    fun loginUserHilt(username: String, password: String)
    {
        viewModelScope.launch {
            val result = repository.loginUserHilt(username, password)
            _loginSuccess.postValue(result)
        }
    }
}

// 3. AuthViewModelFactory is COMPLETELY DELETED!
// You don't need it anymore because Hilt generates this code for you under the hood.