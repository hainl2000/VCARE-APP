package com.example.vcare_app.present.personal.patientprofile.editpatientprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import com.example.vcare_app.databinding.FragmentEditPatientProfileBinding
import com.example.vcare_app.present.personal.patientprofile.PatientProfileFragment
import com.example.vcare_app.utilities.CustomInformationDialog
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.SuccessDialog
import com.example.vcare_app.utilities.Utilities
import java.io.File


class EditPatientProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditPatientProfileFragment()
    }

    private lateinit var viewModel: EditPatientProfileViewModel
    private lateinit var binding: FragmentEditPatientProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPatientProfileBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(EditPatientProfileViewModel::class.java)

        binding.radiobuttonGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {

                R.id.img_radio_button -> {
                    viewModel.setDataUrl("")
                    binding.urlInputLayout.visibility = View.GONE
                    binding.uploadImg.visibility = View.VISIBLE
                }

                R.id.url_radio_button -> {
                    viewModel.setDataUrl("")
                    binding.uploadImg.visibility = View.GONE
                    binding.urlInputLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.myUrl.observe(viewLifecycleOwner) {
            Log.d("myUrl", it)
            binding.postPatientBtn.isEnabled = it.isNotEmpty()
        }

        binding.postPatientBtn.setOnClickListener {
            viewModel.postPatientProfile()
        }

        binding.uploadImg.setOnClickListener {
            openGallery()
        }

        binding.backToHomeBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.urlInputView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `start` and `start + before`
                // are about to be replaced with new text with a length of `count`.
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that somewhere within `start` and `start + before`
                // the characters have been replaced with new text with a length of `count`.
            }

            override fun afterTextChanged(editable: Editable?) {
                // This method is called to notify you that somewhere within the `editable` text,
                // the text has been changed.
                val text = editable.toString()
                // Do something with the changed text
                Log.d("myUrl", text)
                viewModel.setDataUrl(text)
            }
        })
        viewModel.successUpload.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error) {
                    CustomSnackBar.showCustomSnackbar(binding.root, viewModel.errorMsg.value ?: "")
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Success) {
                    SuccessDialog.showDialog(requireContext()) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, PatientProfileFragment())
                            .commit()
                    }
                }
                if (it == LoadingStatus.Error) {
                    CustomSnackBar.showCustomSnackbar(binding.root, viewModel.errorMsg.value ?: "")
                }
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

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                val filePath = Utilities.getFileFromContentUri(requireContext(), it)
                val file = File(filePath ?: "")
                Glide.with(this).load(it)
                    .error(R.drawable.logo_vcare)
                    .into(binding.uploadImg)
                viewModel.uploadImage(file)
            } else {
                Glide.with(this).load(R.drawable.insert_photo_icon)
                    .error(R.drawable.logo_vcare)
                    .into(binding.uploadImg)
            }
        }
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.data != null) {

                    Log.e("TAG", "${result.data?.data}")
                    val contentUri = result.data?.data!!
                    val filePath = Utilities.getFileFromContentUri(requireContext(), contentUri)
                    val file = File(filePath ?: "")
                    Log.e("TAG", file.path)
                    viewModel.uploadImage(file)
                } else {
                    Glide.with(this).load(R.drawable.insert_photo_icon)
                        .error(R.drawable.logo_vcare)
                        .into(binding.uploadImg)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}