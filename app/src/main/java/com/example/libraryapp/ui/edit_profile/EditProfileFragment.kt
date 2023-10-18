package com.example.libraryapp.ui.edit_profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.data.model.User
import com.example.libraryapp.databinding.FragmentEditProfileBinding
import com.example.libraryapp.util.ProgressDialogHelper
import com.example.libraryapp.util.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var navigationController: NavController
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = findNavController()
        observeData()
        onClickListener()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                currentUser.imageUri = imageUri.toString()
                Glide.with(this).load(imageUri).centerCrop().into(binding.imageProfile)
            }
        }
    }

    private fun observeData() {
        editProfileViewModel.isUpdate.observe(this, Observer {
            if (it) {
                navigationController.popBackStack()
            } else {
                context?.showToast(getString(R.string.update_error))
            }
            ProgressDialogHelper.dismissProgressDialog()
        })

        editProfileViewModel.currentUser.observe(this, Observer {
            binding.user = it
            currentUser = it
            if (!it.imageUri.isNullOrBlank()) {
                Glide.with(this).load(it.imageUri).centerCrop().into(binding.imageProfile)
            }
        })
    }

    private fun onClickListener() = with(binding) {
        back.setOnClickListener {
            navigationController.popBackStack()
        }

        editProfile.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val birthday = editTextBirthday.text.toString().trim()
            if (name.isBlank()) {
                context?.showToast(getString(R.string.name_blank))
            } else if (phone.isBlank()) {
                context?.showToast(getString(R.string.phone_blank))
            } else if (birthday.isBlank()) {
                context?.showToast(getString(R.string.birthday_blank))
            } else {
                currentUser.name = name
                currentUser.phone = phone
                currentUser.birthday = birthday
                ProgressDialogHelper.showProgressDialog(
                    requireContext(), getString(R.string.update_data)
                )
                editProfileViewModel.updateProfileCurrentUser(currentUser)
            }
        }

        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        editTextBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = simpleDateFormat.format(calendar.time)
                    editTextBirthday.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

}