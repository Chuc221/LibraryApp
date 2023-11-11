package com.example.libraryapp.ui.home.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.databinding.FragmentBooksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var navigationController: NavController
    private val bookViewModel by viewModels<BookViewModel> ()
    private lateinit var recyclerBookAdapter: RecyclerBookAdapter
    private val listBook = mutableListOf<Book>()
    private var listBookSearch = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
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
        bookViewModel.allBook.observe(this, Observer {books ->
            listBook.clear()
            listBook.addAll(books.sortedBy { it.bookID })
            recyclerBookAdapter.notifyDataSetChanged()
        })
    }

    private fun setOnListener() = with(binding){
        addBook.setOnClickListener {
            navigationController.navigate(R.id.action_homeFragment_to_addBookFragment)
        }

        searchViewBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    listBookSearch = listBook.filter {
                        it.bookName!!.lowercase().contains(newText.lowercase()) || it.bookID!!.lowercase().contains(newText.lowercase())
                    } as MutableList<Book>
                    if (listBookSearch.size > 0) {
                        layoutSearchOff.visibility = View.GONE
                        recyclerBookAdapter.submitList(listBookSearch)
                    } else {
                        layoutSearchOff.visibility = View.VISIBLE
                    }
                } else {
                    layoutSearchOff.visibility = View.GONE
                    recyclerBookAdapter.submitList(listBook)
                }
                return true
            }

        })

        searchViewBook.setIconifiedByDefault(false)

    }

    private fun setAdapter() {
        recyclerBookAdapter = RecyclerBookAdapter(this)
        binding.recyclerBook.adapter = recyclerBookAdapter
        binding.recyclerBook.layoutManager = GridLayoutManager(context, 1)
        recyclerBookAdapter.submitList(listBook)
    }

    override fun onItemClick(book: Book) {
        val bundle = Bundle()
        bundle.putString("idBook", book.bookID)
        navigationController.navigate(R.id.action_homeFragment_to_addBookFragment, bundle)
    }

    override fun onItemLongClick(book: Book) {

    }

}