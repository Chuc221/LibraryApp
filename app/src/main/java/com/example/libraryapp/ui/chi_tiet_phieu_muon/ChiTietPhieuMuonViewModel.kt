package com.example.libraryapp.ui.chi_tiet_phieu_muon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.data.model.Student
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChiTietPhieuMuonViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _isUpdatePhieu = MutableLiveData<Boolean>()
    val isUpdatePhieu: LiveData<Boolean> = _isUpdatePhieu

    fun getStudent(sID: String, returnStudent: (Student?) -> Unit) {
        viewModelScope.launch {
            repository.getStudent(sID) {
                returnStudent(it)
            }
        }
    }

    fun getPhieuById(sID: String, returnPhieu: (PhieuMuon?) -> Unit) {
        viewModelScope.launch {
            repository.getPhieuById(sID) {
                returnPhieu(it)
            }
        }
    }

    fun updatePhieu(phieuMuon: PhieuMuon){
        viewModelScope.launch {
            repository.updatePhieuMuon(phieuMuon){
                _isUpdatePhieu.postValue(it)
            }
        }
    }
}