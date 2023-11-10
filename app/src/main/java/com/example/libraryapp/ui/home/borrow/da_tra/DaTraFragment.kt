package com.example.libraryapp.ui.home.borrow.da_tra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.databinding.FragmentDaTraBinding
import com.example.libraryapp.databinding.FragmentDangMuonBinding
import com.example.libraryapp.ui.home.borrow.OnItemPhieuClickListener
import com.example.libraryapp.ui.home.borrow.PhieuMuonAdapter
import com.example.libraryapp.ui.home.borrow.PhieuMuonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaTraFragment : Fragment(), OnItemPhieuClickListener {

    private lateinit var binding: FragmentDaTraBinding
    private lateinit var navigationController: NavController
    private val phieuMuonViewModel by viewModels<PhieuMuonViewModel>()
    private lateinit var recyclerPhieuAdapter: PhieuMuonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_da_tra, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)
        observeData()
        setAdapter()
    }

    private fun observeData() {
        phieuMuonViewModel.allPhieu.observe(this, Observer { listPhieu ->
            val list = mutableListOf<PhieuMuon>()
            listPhieu.forEach { phieuMuon ->
                if (phieuMuon.trangThai.equals(getString(R.string.da_tra))){
                    list.add(phieuMuon)
                }
            }
            list.sortByDescending { it.ngayTra }
            recyclerPhieuAdapter.submitList(list)
        })
    }

    private fun setAdapter() {
        recyclerPhieuAdapter = PhieuMuonAdapter(this)
        binding.recyclerPhieu.adapter = recyclerPhieuAdapter
        binding.recyclerPhieu.layoutManager = GridLayoutManager(context, 1)

    }

    override fun onItemClick(phieuMuon: PhieuMuon) {
        val bundle = Bundle()
        bundle.putString("idPhieu", phieuMuon.idPhieu)
        navigationController.navigate(R.id.action_homeFragment_to_chiTietPhieuMuonFragment, bundle)
    }
}