package com.example.libraryapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navigationController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)
        setListener()
        observeData()
    }

    private fun observeData() {

    }

    private fun setListener() = with(binding) {
        moveToRegisterFragment.setOnClickListener {
            navigationController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        buttonLogin.setOnClickListener {
            navigationController.navigate(R.id.action_loginFragment_to_homeFragment)
        }

        editTextMail.addTextChangedListener {
            updateStateButtonLogin()
        }
        editTextPassword.addTextChangedListener {
            updateStateButtonLogin()
        }
    }

    private fun updateStateButtonLogin() = with(binding) {
        val isEnable = !editTextMail.text.isNullOrBlank() && !editTextPassword.text.isNullOrBlank()
        if (isEnable) {
            buttonLogin.isEnabled = true
            buttonLogin.setBackgroundResource(R.drawable.button_enable)
        } else {
            buttonLogin.isEnabled = false
            buttonLogin.setBackgroundResource(R.drawable.btn_login)
        }
    }

}