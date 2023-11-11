package com.example.libraryapp.ui.add_book

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
class AddBookViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book> = _book

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateBook(book) {
                _isUpdate.postValue(it)
            }
        }
    }

    fun getBookByID(bID: String) {
        viewModelScope.launch {
            repository.getBookByID(bID){
                _book.postValue(it)
            }
        }
    }
}