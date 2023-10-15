package com.example.libraryapp.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentRegisterBinding
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils
import com.example.libraryapp.util.Utils.showNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navigationController: NavController
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)

        setListener()
        observeData()
    }

    private fun observeData() {
        registerViewModel.isRegister.observe(this, Observer {
            if (it) {
                navigationController.navigate(R.id.action_registerFragment_to_homeFragment)
            } else {
                context?.showNotification(getString(R.string.regis_failed))
            }
            ProgressDialogHelper.dismissProgressDialog()
        })
    }

    private fun setListener() = with(binding){
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        buttonRegister.setOnClickListener {
            val name = edtName.text.toString().trim()
            val mail = edtMail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            if (Utils.isEmailValid(mail)) {
                if (Utils.isPasswordValid(pass)) {
                    ProgressDialogHelper.showProgressDialog(
                        requireContext(),
                        getString(R.string.load_data)
                    )
                    registerViewModel.register(name, mail, pass)
                } else {
                    context?.showNotification(getString(R.string.error_pass))
                }
            } else {
                context?.showNotification(getString(R.string.error_email))
            }
        }

        moveToLoginFragment.setOnClickListener {
            navigationController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        edtName.addTextChangedListener {
            updateStateButtonRegister()
        }
        edtMail.addTextChangedListener {
            updateStateButtonRegister()
        }
        edtPassword.addTextChangedListener {
            updateStateButtonRegister()
        }
        checkBox.setOnClickListener {
            updateStateButtonRegister()
        }
    }

    private fun updateStateButtonRegister() = with(binding) {
        val isEnable =
            !edtName.text.isNullOrBlank() && !edtMail.text.isNullOrBlank() && !edtPassword.text.isNullOrBlank() && checkBox.isChecked
        if (isEnable) {
            buttonRegister.isEnabled = true
            buttonRegister.setBackgroundResource(R.drawable.button_enable)
        } else {
            buttonRegister.isEnabled = false
            buttonRegister.setBackgroundResource(R.drawable.btn_login)
        }
    }

}