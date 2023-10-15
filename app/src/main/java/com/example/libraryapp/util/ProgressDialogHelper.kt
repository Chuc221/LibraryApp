package com.example.libraryapp.util

import android.app.ProgressDialog
import android.content.Context

object ProgressDialogHelper {
    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context, message: String) {
        dismissProgressDialog()
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialog = null
        }
    }
}