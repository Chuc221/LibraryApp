package com.example.libraryapp.ui.borrow_book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.data.model.Student
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhieuMuonViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _isAddStudent = MutableLiveData<Boolean>()
    val isAddStudent: LiveData<Boolean> = _isAddStudent

    private val _isAddPhieuMuon = MutableLiveData<Boolean>()
    val isAddPhieuMuon: LiveData<Boolean> = _isAddPhieuMuon

    fun addPhieuMuon(phieuMuon: PhieuMuon) {
        viewModelScope.launch {
            repository.addPhieuMuon(phieuMuon) {
                _isAddPhieuMuon.postValue(it)
            }
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            repository.addStudent(student) {
                _isAddStudent.postValue(it)
            }
        }
    }

    fun getStudent(sID: String, returnStudent: (Student?) -> Unit) {
        viewModelScope.launch {
            repository.getStudent(sID) {
                returnStudent(it)
            }
        }
    }

    fun getBookByID(bID: String, returnBook: (Book?) -> Unit) {
        viewModelScope.launch {
            repository.getBookByID(bID){
                returnBook(it)
            }
        }
    }
}