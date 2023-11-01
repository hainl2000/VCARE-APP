package com.example.vcare_app.present.personal.editpersonal


import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.api.api_model.response.Profile
import com.example.vcare_app.databinding.FragmentEditPersonalBinding
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.SuccessDialog
import com.example.vcare_app.utilities.Utilities
import java.io.File
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPersonalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentEditPersonalBinding
    private lateinit var viewModel: EditPersonalFragmentViewModel
    var gender = false
    var dateOfBirth = ""
    lateinit var imgUrl: Uri
    lateinit var currentProfile: Profile
    lateinit var file: File
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPersonalBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[EditPersonalFragmentViewModel::class.java]

        currentProfile = arguments?.getSerializable("data") as Profile
        binding.profile = currentProfile
        Log.d("TAGG", currentProfile.avatar ?: "")

        Glide.with(this).load(currentProfile.avatar)
            .error(com.example.vcare_app.R.drawable.error_icon).into(binding.editPersonalAvatar)

        binding.editAvatarBtn.setOnClickListener {
            openGallery()
        }

        binding.btnUpdateUser.setOnClickListener {
            gender = binding.radioNam.isChecked
            viewModel.updateUser(
                UpdateUserRequest(
                    binding.editFullName.text.toString(),
                    viewModel.imgUrl.value
                        ?: Utilities.extractFileNameFromUrl(currentProfile.avatar!!),
                    gender,
                    binding.editDob.text.toString(),
                    binding.editIdentityNumber.text.toString(),
                    binding.editSin.text.toString(),
                    binding.editAddress.text.toString()
                )
            )
        }

        binding.editDob.setOnClickListener {
            showDatePickerDialog()
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                if (it == LoadingStatus.Success) {
                    LoadingDialogManager.dismissLoadingDialog()
                    if (viewModel.updateSuccess.value == true) {
                        SuccessDialog.showDialog(requireContext()) {
                            parentFragmentManager.popBackStack()
                        }
                    }
                }
                if (it == LoadingStatus.Error && !viewModel.errorMsg.value.isNullOrEmpty()) {
                    LoadingDialogManager.dismissLoadingDialog()
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getLaunch.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            getLaunch.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val getLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(requireContext(), "Please accept permission", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFileFromContentUri(context: Context, contentUri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }

        return null
    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                imgUrl = it
                binding.editPersonalAvatar.setImageURI(imgUrl)
                val contentUri = it
                val filePath = getFileFromContentUri(requireContext(), contentUri)
                file = File(filePath)

                Log.e("Error", file.path)
                viewModel.uploadImage(file)
            } else {
                Glide.with(this).load(currentProfile.avatar)
                    .error(com.example.vcare_app.R.drawable.error_icon)
                    .into(binding.editPersonalAvatar)
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getLaunch.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        getLaunch.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else {
                    Toast.makeText(requireContext(), "Deo duoc", Toast.LENGTH_LONG).show()
                }
                return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                val formattedDate = String.format(
                    "%04d-%02d-%02d",
                    year,
                    month,
                    dayOfMonth
                )// Format the date as needed
                binding.editDob.text = formattedDate
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditPersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditPersonalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}