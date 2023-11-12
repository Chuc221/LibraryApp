package com.example.libraryapp.ui.borrow_book

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.data.model.Student
import com.example.libraryapp.databinding.FragmentPhieuMuonBinding
import com.example.libraryapp.ui.home.book.OnItemClickListener
import com.example.libraryapp.ui.home.book.RecyclerBookAdapter
import com.example.libraryapp.util.Constants.STATUS_BORROW
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils.showNotification
import com.example.libraryapp.util.Utils.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class PhieuMuonFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentPhieuMuonBinding
    private lateinit var navigationController: NavController
    private val phieuMuonViewModel by viewModels<PhieuMuonViewModel> ()
    private lateinit var recyclerBookAdapter: RecyclerBookAdapter
    private val listBook = mutableListOf<Book>()
    private val idNV = FirebaseAuth.getInstance().uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phieu_muon, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = findNavController()
        observeData()
        setOnListener()
        setAdapter()
    }

    private fun observeData() {
        phieuMuonViewModel.isAddPhieuMuon.observe(this, Observer {
            if (it){
                ProgressDialogHelper.dismissProgressDialog()
            }
            else {
                context?.showToast(getString(R.string.update_error))
            }
            navigationController.popBackStack()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnListener() = with(binding){
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        save.setOnClickListener {
            val sID = editTextIdSV.text.toString().trim()
            val sName = editTextName.text.toString().trim()
            val sBirthday = editTextBirthday.text.toString().trim()
            val sClass = editTextLop.text.toString().trim()
            val dateCreate = editTextDateCreate.text.toString().trim()
            val dateReturn = editTextDateReturn.text.toString().trim()

            if(sID.isBlank() || sName.isBlank() || sBirthday.isBlank() || sClass.isBlank()){
                context?.showNotification(getString(R.string.tt_sv))
            }
            else if(dateReturn.isBlank()){
                context?.showNotification(getString(R.string.not_date_return))
            }
            else if(!isDateValid(dateReturn) || !isDateValid(dateCreate)){
                context?.showNotification(getString(R.string.not_date_vald))
            }
            else if (parseDateToTimeInMillis(dateReturn) == null || parseDateToTimeInMillis(dateCreate) == null){
                context?.showNotification(getString(R.string.date_error))
            }
            else if (parseDateToTimeInMillis(dateReturn)!! <= parseDateToTimeInMillis(dateCreate)!!){
                context?.showNotification(getString(R.string.date_return_error))
            }
            else if(listBook.size <= 0){
                context?.showNotification(getString(R.string.not_book))
            }
            else{
                val student = Student(sID,sName,sBirthday,sClass)
                val phieuMuon = PhieuMuon(idNV = idNV, idSV = sID, nameSV = sName, listBook = listBook, ngayMuon = System.currentTimeMillis(), ngayTra = parseDateToTimeInMillis(dateReturn), trangThai = STATUS_BORROW)
                phieuMuonViewModel.addStudent(student)
                phieuMuonViewModel.addPhieuMuon(phieuMuon)
                ProgressDialogHelper.showProgressDialog(
                    requireContext(), getString(R.string.update_data)
                )
            }
        }

        editTextIdSV.addTextChangedListener {
            val sID = editTextIdSV.text.toString().trim()
            if (sID.isNotBlank()){
                phieuMuonViewModel.getStudent(sID){ sinhvien ->
                    if (sinhvien != null){
                        student = sinhvien
                    }
                    else{
                        editTextName.text = null
                        editTextBirthday.text = null
                        editTextLop.text = null
                    }
                }
            }
        }

        search.setOnClickListener {
            val sID = editTextIdSV.text.toString().trim()
            if (sID.isNotBlank()){
                phieuMuonViewModel.getStudent(sID){ sinhvien ->
                    if (sinhvien != null){
                        student = sinhvien
                    }
                    else{
                        editTextName.text = null
                        editTextBirthday.text = null
                        editTextLop.text = null
                        context?.showToast(getString(R.string.studentID_error))
                    }
                }
            }
            else {
                context?.showToast(getString(R.string.studentID_blank))
            }
        }

        buttonAddBook.setOnClickListener {
            val bID = editTextIDBook.text.toString().trim()
            if (bID.isNotBlank()){
                var isCon = false
                listBook.forEach { book ->
                    if (book.bookID == bID){
                        context?.showToast(getString(R.string.book) + " " + book.bookName + " " + getString(R.string.da_co_sach))
                        isCon = true
                    }
                }

                if (!isCon){
                    phieuMuonViewModel.getBookByID(bID){ book ->
                        if (book != null) {
                            listBook.add(book)
                            soSachMuon.text = " " + listBook.size.toString() + " " + getString(R.string.cuon)
                            recyclerBookAdapter.submitList(listBook)
                            editTextIDBook.text = null
                        }
                        else {
                            context?.showToast(getString(R.string.bookID_error))
                        }
                    }
                }

            }
            else {
                context?.showToast(getString(R.string.bookID_blank))
            }
        }

        editTextDateCreate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(System.currentTimeMillis()))

        editTextDateReturn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = simpleDateFormat.format(calendar.timeInMillis)
                    editTextDateReturn.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun setAdapter() {
        recyclerBookAdapter = RecyclerBookAdapter(this)
        binding.recyclerSachMuon.adapter = recyclerBookAdapter
        binding.recyclerSachMuon.layoutManager = GridLayoutManager(context, 1)
        recyclerBookAdapter.submitList(listBook)
    }

    override fun onItemClick(book: Book) {

    }

    override fun onItemLongClick(book: Book) {
        val builder = AlertDialog.Builder(context!!)
        builder.setIcon(R.drawable.ic_notifications)
        builder.setTitle(getText(R.string.notification))
        builder.setMessage(getString(R.string.xoa) + " " + book.bookName + " " + getString(R.string.xoa1))

        builder.setPositiveButton(getString(R.string.dong_y)) { dialog, _ ->
            listBook.remove(book)
            binding.soSachMuon.text = " " + listBook.size.toString() + " " + getString(R.string.cuon)
            recyclerBookAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        builder.setNegativeButton(getText(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun isDateValid(dateString: String): Boolean {
        val pattern = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        return pattern.matches(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateToTimeInMillis(dateString: String): Long? {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = format.parse(dateString)
            date?.time
        } catch (e: Exception) {
            null
        }
    }
}