package com.example.vcare_app.present.personal.editpersonal


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.request.UpdateUserRequest
import com.example.vcare_app.databinding.FragmentEditPersonalBinding
import com.example.vcare_app.utilities.CustomInformationDialog
import com.example.vcare_app.utilities.CustomSnackBar
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
    private var gender = false
    private lateinit var imgUrl: Uri
    private lateinit var file: File

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPersonalBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[EditPersonalFragmentViewModel::class.java]
        viewModel.getUserProfile()

        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.profile = it
        }

        binding.editAvatarBtn.setOnClickListener {
            openGallery()
        }

        binding.btnUpdateUser.setOnClickListener {
            gender = binding.radioNam.isChecked
            viewModel.updateUser(
                UpdateUserRequest(
                    binding.editFullName.text.toString(),
                    viewModel.imgUrl.value,
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
                if (it == LoadingStatus.Error) {
                    LoadingDialogManager.dismissLoadingDialog()

                }
            }
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                CustomSnackBar.showCustomSnackbar(binding.root, "${viewModel.errorMsg.value}")
            }
        }
        return binding.root
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            getLaunch.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            getLaunch.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.data != null) {
                    imgUrl = result.data?.data!!
                    binding.editPersonalAvatar.setImageURI(imgUrl)
                    Log.e("TAG", "${result.data?.data}")
                    val contentUri = result.data?.data!!
                    val filePath = Utilities.getFileFromContentUri(requireContext(), contentUri)
                    file = File(filePath ?: "")

                    Log.e("TAG", file.path)
                    viewModel.uploadImage(file)
                } else {
                    Glide.with(this).load(viewModel.userProfile.value?.avatar)
                        .error(R.drawable.logo_vcare)
                        .into(binding.editPersonalAvatar)
                }
            }
        }

    private val getLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                getContent.launch(intent)
            } else {
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        } else {
            CustomInformationDialog.showCustomInformationDialog(
                requireContext(),
                requireContext().resources.getString(R.string.gallery_permission_denied)
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                activity?.startActivity(intent)
            }
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                imgUrl = it
                binding.editPersonalAvatar.setImageURI(imgUrl)
                val contentUri = it
                val filePath = Utilities.getFileFromContentUri(requireContext(), contentUri)
                file = File(filePath ?: "")

                viewModel.uploadImage(file)
            } else {
                Glide.with(this).load(viewModel.userProfile.value?.avatar)
                    .error(R.drawable.logo_vcare)
                    .into(binding.editPersonalAvatar)
            }
        }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, mYear, mMonth, dayOfMonth ->
                val formattedDate = String.format(
                    "%04d-%02d-%02d",
                    mYear,
                    mMonth,
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