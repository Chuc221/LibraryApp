package com.example.libraryapp.ui.borrow_book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.Student
import com.example.libraryapp.databinding.FragmentAddBookBinding
import com.example.libraryapp.databinding.FragmentPhieuMuonBinding
import com.example.libraryapp.ui.home.book.RecyclerBookAdapter
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhieuMuonFragment : Fragment() {

    private lateinit var binding: FragmentPhieuMuonBinding
    private lateinit var navigationController: NavController
    private val phieuMuonViewModel by viewModels<PhieuMuonViewModel> ()
    private lateinit var recyclerBookAdapter: RecyclerBookAdapter
    private val listBook = mutableListOf<Book>()

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
        setAdapter()
    }

    private fun observeData() {
        phieuMuonViewModel.isAddStudent.observe(this, Observer {
            if (it){
                ProgressDialogHelper.dismissProgressDialog()
            }
            else {
                context?.showToast(getString(R.string.update_error))
            }
            navigationController.popBackStack()
        })
    }

    private fun setOnListener() = with(binding){
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        save.setOnClickListener {
            val sID = editTextIdSV.text.toString().trim()
            val sName = editTextName.text.toString().trim()
            val sBirthday = editTextBirthday.text.toString().trim()
            val sClass = editTextLop.text.toString().trim()
            phieuMuonViewModel.addStudent(Student(sID,sName,sBirthday,sClass))

            ProgressDialogHelper.showProgressDialog(
                requireContext(), getString(R.string.update_data)
            )
        }

        search.setOnClickListener {
            val sID = editTextIdSV.text.toString().trim()
            if (sID.isNotBlank()){
                phieuMuonViewModel.getStudent(sID){
                    student = it
                }
            }
            else {
                context?.showToast(getString(R.string.studentID_blank))
            }
        }

        buttonAddBook.setOnClickListener {
            val bID = editTextIDBook.text.toString().trim()
            if (bID.isNotBlank()){
                phieuMuonViewModel.getBookByID(bID){
                    if (it != null) {
                        listBook.add(it)
                        recyclerBookAdapter.notifyDataSetChanged()
                    }
                    else {
                        context?.showToast(getString(R.string.bookID_error))
                    }
                }
            }
            else {
                context?.showToast(getString(R.string.bookID_blank))
            }
        }
    }

    private fun setAdapter() {
        recyclerBookAdapter = RecyclerBookAdapter()
        binding.recyclerSachMuon.adapter = recyclerBookAdapter
        binding.recyclerSachMuon.layoutManager = GridLayoutManager(context, 1)
        recyclerBookAdapter.submitList(listBook)
    }

}