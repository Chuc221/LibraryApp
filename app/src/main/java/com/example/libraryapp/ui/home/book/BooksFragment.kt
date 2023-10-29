package com.example.libraryapp.ui.home.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentBooksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var navigationController: NavController
    private val bookViewModel by viewModels<BookViewModel> ()
    private lateinit var recyclerBookAdapter: RecyclerBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)

        observeData()
        setOnListener()
        setAdapter()
    }

    private fun observeData() {
        bookViewModel.allBook.observe(this, Observer {
            recyclerBookAdapter.submitList(it)
        })
    }

    private fun setOnListener() = with(binding){
        addBook.setOnClickListener {
            navigationController.navigate(R.id.action_homeFragment_to_addBookFragment)
        }
    }

    private fun setAdapter() {
        recyclerBookAdapter = RecyclerBookAdapter()
        binding.recyclerBook.adapter = recyclerBookAdapter
        binding.recyclerBook.layoutManager = GridLayoutManager(context, 1)

    }

}