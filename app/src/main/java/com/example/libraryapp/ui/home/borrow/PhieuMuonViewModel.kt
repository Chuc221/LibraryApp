package com.example.libraryapp.ui.home.borrow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhieuMuonViewModel @Inject constructor(private val repository: Repository) : ViewModel(){
    private val _allPhieu = MutableLiveData<MutableList<PhieuMuon>> ()
    val allPhieu: LiveData<MutableList<PhieuMuon>> = _allPhieu

    init {
        viewModelScope.launch {
            repository.getAllPhieu {
                _allPhieu.postValue(it)
            }
        }
    }
}