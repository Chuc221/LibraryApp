package com.example.libraryapp.util

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.libraryapp.R

object Utils {

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.showNotification(mess: String) {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.ic_notifications)
        builder.setTitle(getText(R.string.notification))
        builder.setMessage(mess)

        builder.setPositiveButton(getText(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.cancel() })
        val dialog = builder.create()
        dialog.show()
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

    fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return emailPattern.matches(email)
    }
}