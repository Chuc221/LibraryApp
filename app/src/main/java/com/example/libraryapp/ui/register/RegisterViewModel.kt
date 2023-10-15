package com.example.libraryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean> = _isRegister

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(name, email, password) {
                _isRegister.postValue(it)
            }
        }
    }
}