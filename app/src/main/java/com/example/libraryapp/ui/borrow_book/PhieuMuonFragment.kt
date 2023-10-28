package com.example.libraryapp.ui.borrow_book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentAddBookBinding
import com.example.libraryapp.databinding.FragmentPhieuMuonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhieuMuonFragment : Fragment() {

    private lateinit var binding: FragmentPhieuMuonBinding
    private lateinit var navigationController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phieu_muon, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = findNavController()
        observeData()
        setOnListener()
    }

    private fun observeData() {

    }

    private fun setOnListener() = with(binding){
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        save.setOnClickListener {
            navigationController.popBackStack()
        }
    }

}