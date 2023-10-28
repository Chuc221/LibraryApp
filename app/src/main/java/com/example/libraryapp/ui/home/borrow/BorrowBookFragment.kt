package com.example.libraryapp.ui.home.borrow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentBooksBinding
import com.example.libraryapp.databinding.FragmentBorrowBookBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BorrowBookFragment : Fragment() {

    private lateinit var binding: FragmentBorrowBookBinding
    private lateinit var navigationController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_borrow_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)

        observeData()
        setOnListener()
    }

    private fun observeData() {

    }

    private fun setOnListener() = with(binding){
        createPhieu.setOnClickListener {
            navigationController.navigate(R.id.action_homeFragment_to_phieuMuonFragment)
        }
    }
}