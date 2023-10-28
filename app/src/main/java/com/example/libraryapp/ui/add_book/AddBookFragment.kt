package com.example.libraryapp.ui.add_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.databinding.FragmentAddBookBinding
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookFragment : Fragment() {

    private lateinit var binding: FragmentAddBookBinding
    private lateinit var navigationController: NavController
    private val addBookViewModel by viewModels<AddBookViewModel> ()
    private lateinit var book: Book
    private lateinit var bookImageUri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = findNavController()
        observeData()
        setOnListener()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                bookImageUri = imageUri.toString()
                Glide.with(this).load(imageUri).centerCrop().into(binding.imageBook)
            }
        }
    }

    private fun observeData() {

        addBookViewModel.isUpdate.observe(this, Observer {
            if (it) {
                navigationController.popBackStack()
            } else {
                context?.showToast(getString(R.string.update_error))
            }
            ProgressDialogHelper.dismissProgressDialog()
        })

    }

    private fun setOnListener() = with(binding){
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        save.setOnClickListener {
            val bookID = editTextIdBook.text.toString().trim()
            val bookName = editTextName.text.toString().trim()
            val bookAuth = editTextAuthor.text.toString().trim()
            val bookNhaXB = editTextProducer.text.toString().trim()
            val bookNamXB = editTextYear.text.toString().trim()
            val bookType = editTextCategory.text.toString().trim()
            val bookMoTa = editTextDescribe.text.toString().trim()

            if (bookID.isBlank() || bookName.isBlank() || bookAuth.isBlank() || bookNhaXB.isBlank() || bookNamXB.isBlank() || bookType.isBlank() || bookMoTa.isBlank()) {
                context?.showToast(getString(R.string.book_blank))
            }
            else {
                book = Book(bookID = bookID, bookName = bookName, bookAuth = bookAuth, bookNhaXB = bookNhaXB, bookNamXB = bookNamXB.toInt(), bookType = bookType, bookMoTa = bookMoTa, bookImageUri = bookImageUri)

                ProgressDialogHelper.showProgressDialog(
                    requireContext(), getString(R.string.update_data)
                )
                addBookViewModel.updateBook(book)
            }
        }

        imageButtonBook.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
    }
}