package com.example.libraryapp.ui.home.borrow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryapp.R
import com.example.libraryapp.data.model.PhieuMuon
import com.example.libraryapp.databinding.ItemPhieuBinding
import com.example.libraryapp.util.Constants.STATUS_BORROW
import java.text.SimpleDateFormat
import java.util.*

class PhieuMuonAdapter(private val onItemPhieuClickListener: OnItemPhieuClickListener):
    ListAdapter<PhieuMuon, PhieuMuonAdapter.ViewHolder>(PhieuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhieuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemPhieuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(phieuMuon: PhieuMuon) {
            binding.phieu = phieuMuon
            binding.tvSoSach.text = phieuMuon.listBook?.size.toString()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            if (phieuMuon.trangThai.equals(STATUS_BORROW)){
                binding.txtNgay.text = itemView.context.getText(R.string.ngay_muon)
                val formattedDate = simpleDateFormat.format(phieuMuon.ngayMuon)
                binding.txtNgayMuonTra.text = formattedDate
            }
            else {
                binding.txtNgay.text = itemView.context.getText(R.string.ngay_tra)
                val formattedDate = simpleDateFormat.format(phieuMuon.ngayTra)
                binding.txtNgayMuonTra.text = formattedDate
            }
            itemView.setOnClickListener {
                onItemPhieuClickListener.onItemClick(phieuMuon)
            }
            binding.executePendingBindings()
        }
    }

    private class PhieuDiffCallback : DiffUtil.ItemCallback<PhieuMuon>() {
        override fun areItemsTheSame(oldItem: PhieuMuon, newItem: PhieuMuon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PhieuMuon, newItem: PhieuMuon): Boolean {
            return oldItem.idPhieu == newItem.idPhieu
        }
    }
}

interface OnItemPhieuClickListener {
    fun onItemClick(phieuMuon: PhieuMuon)

}