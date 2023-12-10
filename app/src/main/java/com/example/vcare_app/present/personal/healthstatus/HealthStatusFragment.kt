package com.example.vcare_app.present.personal.healthstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.databinding.FragmentHealthStatusBinding
import com.example.vcare_app.present.personal.edithealthstatus.EditHealthStatusFragment
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

class HealthStatusFragment : Fragment() {

    companion object {
        fun newInstance() = HealthStatusFragment()
    }

    private lateinit var viewModel: HealthStatusViewModel

    private lateinit var binding: FragmentHealthStatusBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HealthStatusViewModel::class.java]
        viewModel.getHealthStatus()
        binding = FragmentHealthStatusBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHealthStatus()
        }
        viewModel._healthStatus.observe(viewLifecycleOwner) {
            binding.healthStatus = it
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
            }
        }
        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.editBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                replace(R.id.fragment_container_view, EditHealthStatusFragment())
                addToBackStack("edit_health_status")
                commit()
            }
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                CustomSnackBar.showCustomSnackbar(requireView(), it)
            }
        }
        return binding.root
    }
}