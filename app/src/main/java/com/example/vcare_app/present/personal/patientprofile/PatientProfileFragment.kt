package com.example.vcare_app.present.personal.patientprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vcare_app.R
import com.example.vcare_app.adapter.ImageAndUrlAdapter
import com.example.vcare_app.databinding.FragmentPatientProfileBinding
import com.example.vcare_app.onclickinterface.OnImageOrUrlClick
import com.example.vcare_app.present.personal.patientprofile.editpatientprofile.EditPatientProfileFragment
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.FullScreenImageFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.Utilities


class PatientProfileFragment : Fragment(), OnImageOrUrlClick {

    companion object {
        fun newInstance() = PatientProfileFragment()
    }

    private lateinit var viewModel: PatientProfileViewModel

    private lateinit var binding: FragmentPatientProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PatientProfileViewModel::class.java)
        binding = FragmentPatientProfileBinding.inflate(inflater)

        viewModel.getPatientProfile()
        val adapter = ImageAndUrlAdapter(emptyList(), this)
        viewModel.listData.observe(viewLifecycleOwner) {
            adapter.updateData(it)
            if (it.isEmpty()) {
                binding.tvEmptyList.visibility = View.VISIBLE
            } else {
                binding.tvEmptyList.visibility = View.GONE
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error) {
                    CustomSnackBar.showCustomSnackbar(binding.root, viewModel.errorMsg.value ?: "")
                }
            }
        }

        binding.patientProfileRcv.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.patientProfileRcv.context,
            LinearLayoutManager.VERTICAL
        )
        binding.patientProfileRcv.addItemDecoration(dividerItemDecoration)
        binding.addPatientProfileDetail.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container_view, EditPatientProfileFragment())
                addToBackStack("edit_patient_fragment")
                commit()
            }
        }

        binding.backToHomeBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun onShowImage(img: String) {
        val fullScreenImageFragment = FullScreenImageFragment.newInstance(img)
        fullScreenImageFragment.show(parentFragmentManager, "fullscreenImg")
    }

    override fun onImageOrUrlClick(url: String) {
        if (Utilities.isImageUrl(url)) {
            onShowImage(url)
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }catch (err: Exception){
                CustomSnackBar.showCustomSnackbar(binding.root,"Can not open this url")
            }
        }
    }

}