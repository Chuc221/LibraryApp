package com.example.libraryapp.ui.home.borrow.dang_muon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentDangMuonBinding
import com.example.libraryapp.ui.home.book.RecyclerBookAdapter
import com.example.libraryapp.ui.home.borrow.PhieuMuonAdapter
import com.example.libraryapp.ui.home.borrow.PhieuMuonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DangMuonFragment : Fragment() {

    private lateinit var binding: FragmentDangMuonBinding
    private lateinit var navigationController: NavController
    private val bookViewModel by viewModels<PhieuMuonViewModel>()
    private lateinit var recyclerPhieuAdapter: PhieuMuonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dang_muon, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setAdapter()
    }

    private fun observeData() {
        bookViewModel.allPhieu.observe(this, Observer {
            recyclerPhieuAdapter.submitList(it)
        })
    }

    private fun setAdapter() {
        recyclerPhieuAdapter = PhieuMuonAdapter()
        binding.recyclerPhieu.adapter = recyclerPhieuAdapter
        binding.recyclerPhieu.layoutManager = GridLayoutManager(context, 1)

    }

}