package com.example.libraryapp.ui.home.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var navigationController: NavController
    private val userViewModel by viewModels<UserViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = Navigation.findNavController(view)
        observeData()
        setOnListener()
    }

    private fun observeData() {
        userViewModel.currentUser.observe(this, Observer {
            binding.user = it
            if (!it.imageUri.isNullOrBlank()) {
                Glide.with(this).load(it.imageUri).centerCrop().into(binding.profileImage)
                Glide.with(this).load(it.imageUri).centerCrop().into(binding.profileBackground)
            }
        })
    }

    private fun setOnListener() = with(binding){
        imgBtnEdit.setOnClickListener {
            navigationController.navigate(R.id.action_homeFragment_to_editProfileFragment)
        }

        logout.setOnClickListener {
            userViewModel.logout()
            navigationController.navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

}