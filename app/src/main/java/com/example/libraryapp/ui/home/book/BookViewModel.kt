package com.example.libraryapp.ui.home.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: Repository) : ViewModel(){
    private val _allBook = MutableLiveData<MutableList<Book>> ()
    val allBook: LiveData<MutableList<Book>> = _allBook

    init {
        viewModelScope.launch {
            repository.getBook {
                _allBook.postValue(it)
            }
        }
    }
}