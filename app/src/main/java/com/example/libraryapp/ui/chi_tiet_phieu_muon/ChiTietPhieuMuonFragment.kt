package com.example.libraryapp.ui.chi_tiet_phieu_muon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.databinding.FragmentChiTietPhieuMuonBinding
import com.example.libraryapp.ui.home.book.OnItemClickListener
import com.example.libraryapp.ui.home.book.RecyclerBookAdapter
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils.showToast
import androidx.lifecycle.Observer
import com.example.libraryapp.util.Constants.STATUS_BORROW
import com.example.libraryapp.util.Constants.STATUS_COMPLETE
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChiTietPhieuMuonFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentChiTietPhieuMuonBinding
    private lateinit var navigationController: NavController
    private val chiTietPhieuMuonViewModel by viewModels<ChiTietPhieuMuonViewModel> ()
    private lateinit var recyclerBookAdapter: RecyclerBookAdapter
    private lateinit var phieuMuon: PhieuMuon


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chi_tiet_phieu_muon, container, false)
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
        val bundle = arguments
        val idPhieu = bundle?.getString("idPhieu")
        if (idPhieu != null) {
            chiTietPhieuMuonViewModel.getPhieuById(idPhieu){ phieu ->
                if (phieu != null) {
                    phieuMuon = phieu
                    phieu.idSV?.let {
                        chiTietPhieuMuonViewModel.getStudent(it){  student ->
                            binding.student = student
                        }
                    }


                    if (phieuMuon.trangThai.equals(STATUS_BORROW)){
                        binding.tra.visibility = View.VISIBLE
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        binding.editTextDateReturn.text = simpleDateFormat.format(phieu.ngayTra)
                        binding.editTextTrangThai.text = getText(R.string.dang_muon)
                    } else {
                        binding.tvNgayTra.text = getText(R.string.ngay_tra)
                        binding.tra.visibility = View.GONE
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        binding.editTextDateReturn.text = simpleDateFormat.format(phieu.ngayTra)
                        binding.editTextTrangThai.text = getText(R.string.da_tra)
                    }

                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    binding.editTextDateCreate.text = simpleDateFormat.format(phieu.ngayMuon)
                    binding.soSachMuon.text = phieu.listBook?.size.toString() + " "+ getString(R.string.cuon)
                    recyclerBookAdapter.submitList(phieu.listBook)
                }
            }
        }

        chiTietPhieuMuonViewModel.isUpdatePhieu.observe(this, Observer {
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

        tra.setOnClickListener {
            phieuMuon.ngayTra = System.currentTimeMillis()
            phieuMuon.trangThai = STATUS_COMPLETE

            val builder = AlertDialog.Builder(context!!)
            builder.setIcon(R.drawable.ic_notifications)
            builder.setTitle(getText(R.string.notification))
            builder.setMessage(getString(R.string.xn_tra_phieu))

            builder.setPositiveButton(getString(R.string.dong_y)) { dialog, _ ->
                chiTietPhieuMuonViewModel.updatePhieu(phieuMuon)

                ProgressDialogHelper.showProgressDialog(
                    requireContext(), getString(R.string.update_data)
                )
            }

            builder.setNegativeButton(getText(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setAdapter() {
        recyclerBookAdapter = RecyclerBookAdapter(this)
        binding.recyclerSachMuon.adapter = recyclerBookAdapter
        binding.recyclerSachMuon.layoutManager = GridLayoutManager(context, 1)
    }

    override fun onItemClick(book: Book) {

    }

    override fun onItemLongClick(book: Book) {

    }

}