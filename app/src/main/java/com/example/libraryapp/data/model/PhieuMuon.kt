package com.example.libraryapp.data.model

class PhieuMuon (
    var idPhieu: String? = "",
    val idNV: String? = "",
    val idSV: String? = "",
    val nameSV: String? = "",
    val listBook: MutableList<Book>? = null,
    val ngayMuon: Long? = 0,
    var ngayTra: Long? = 0,
    var trangThai: String? = ""
)