package com.example.vcare_app.present.personal.edithealthstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.api.api_model.HealthStatus
import com.example.vcare_app.databinding.FragmentEditHealthStatusBinding
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.SuccessDialog

class EditHealthStatusFragment : Fragment() {

    companion object {
        fun newInstance() = EditHealthStatusFragment()
    }

    private lateinit var viewModel: EditHealthStatusViewModel
    private lateinit var binding: FragmentEditHealthStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditHealthStatusBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        viewModel = ViewModelProvider(this)[EditHealthStatusViewModel::class.java]
        viewModel.getHealthStatus()

        viewModel._healthStatus.observe(viewLifecycleOwner) {
            binding.currentHealthStatus = it
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (viewModel._updateStatus.value == true) {
                    SuccessDialog.showDialog(requireContext()) {
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.confirmButton.setOnClickListener {
            val height = Integer.parseInt(binding.editHeight.text.toString())
            val weight = Integer.parseInt(binding.editWeight.text.toString())
            val bloodType = binding.editBloodType.text.toString()
            val bloodPressure = binding.editBloodPressure.text.toString()
            val healthStatus = HealthStatus(height, weight, bloodType, bloodPressure)
            viewModel.updateHealthStatus(healthStatus)
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                CustomSnackBar.showCustomSnackbar(requireView(), it)
            }
        }

        return binding.root
    }

}