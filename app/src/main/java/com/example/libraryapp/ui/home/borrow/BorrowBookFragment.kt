package com.example.libraryapp.ui.home.borrow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.databinding.FragmentBooksBinding
import com.example.libraryapp.databinding.FragmentBorrowBookBinding
import com.example.libraryapp.ui.home.borrow.da_tra.DaTraFragment
import com.example.libraryapp.ui.home.borrow.dang_muon.DangMuonFragment
import com.example.libraryapp.util.Constants
import com.example.libraryapp.util.Constants.STATUS_BORROW
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BorrowBookFragment : Fragment(), OnItemPhieuClickListener {

    private lateinit var binding: FragmentBorrowBookBinding
    private lateinit var navigationController: NavController
    private lateinit var phieuMuonAdapter: PhieuMuonAdapter
    private val listPhieu = mutableListOf<PhieuMuon>()
    private var listPhieuSearch = mutableListOf<PhieuMuon>()
    private val phieuMuonViewModel by viewModels<PhieuMuonViewModel>()


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

        tapLayout()
        observeData()
        setOnListener()
        setAdapter()
    }

    private fun setAdapter() {
        phieuMuonAdapter = PhieuMuonAdapter(this)
        binding.recyclerSearchPhieu.adapter = phieuMuonAdapter
        binding.recyclerSearchPhieu.layoutManager = GridLayoutManager(activity, 1)
    }

    private fun tapLayout() {
        binding.viewPager.adapter = PhieuMuonPagerAdapter(
            listOf(
                Pair(DangMuonFragment(), getString(R.string.dang_muon)),
                Pair(DaTraFragment(), getString(R.string.da_tra))
            ), childFragmentManager
        )
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun observeData() {
        phieuMuonViewModel.allPhieu.observe(this, Observer { phieus ->
            listPhieu.clear()
            phieus.forEach { phieuMuon ->
                if (phieuMuon.trangThai.equals(STATUS_BORROW)){
                    listPhieu.add(phieuMuon)
                }
            }
        })
    }

    private fun setOnListener() = with(binding){
        createPhieu.setOnClickListener {
            navigationController.navigate(R.id.action_homeFragment_to_phieuMuonFragment)
        }

        searchViewPhieu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    layoutSearchPhieu.visibility = View.VISIBLE
                    listPhieuSearch = listPhieu.filter {
                        it.nameSV!!.lowercase().contains(newText.lowercase()) || it.idSV!!.lowercase().contains(newText.lowercase())
                                || SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it.ngayMuon).contains(newText.lowercase())
                    } as MutableList<PhieuMuon>
                    if (listPhieuSearch.size > 0) {
                        layoutSearchOff.visibility = View.GONE
                        listPhieuSearch.sortedBy {it.ngayMuon}
                        phieuMuonAdapter.submitList(listPhieuSearch)
                    } else {
                        layoutSearchOff.visibility = View.VISIBLE
                    }
                } else {
                    layoutSearchPhieu.visibility = View.GONE
                }
                return true
            }

        })

        searchViewPhieu.setIconifiedByDefault(false)

        searchViewPhieu.setOnCloseListener {
            layoutSearchPhieu.visibility = View.GONE
            false
        }
    }

    override fun onItemClick(phieuMuon: PhieuMuon) {
        val bundle = Bundle()
        bundle.putString("idPhieu", phieuMuon.idPhieu)
        navigationController.navigate(R.id.action_homeFragment_to_chiTietPhieuMuonFragment, bundle)
    }
}