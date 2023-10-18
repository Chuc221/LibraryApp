package com.example.libraryapp.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.User
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    init {
        viewModelScope.launch {
            repository.getCurrentUser {
                _currentUser.postValue(it)
            }
        }
    }

    fun updateProfileCurrentUser(user: User) {
        viewModelScope.launch {
            repository.updateProfileCurrentUser(user) {
                _isUpdate.postValue(it)
            }
        }
    }
}