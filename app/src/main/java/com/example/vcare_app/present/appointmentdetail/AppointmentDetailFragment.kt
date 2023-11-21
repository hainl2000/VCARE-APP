package com.example.vcare_app.present.appointmentdetail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.adapter.ConclusionAdapter
import com.example.vcare_app.databinding.FragmentAppointmentDetailBinding
import com.example.vcare_app.mainactivity.MainActivityViewModel
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.onclickinterface.OnMedicineResultClick
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.FullScreenImageFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.Utilities

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentDetailFragment : Fragment(), OnMedicineResultClick {
    // TODO: Rename and change types of parameters

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentArgument =
                it.getParcelable("appointment_id", AppointmentDetailArgument::class.java)!!
        }

    }

    private lateinit var binding: FragmentAppointmentDetailBinding

    private lateinit var viewModel: AppointmentDetailViewModel

    private lateinit var activityViewModel: MainActivityViewModel

    private lateinit var appointmentArgument: AppointmentDetailArgument


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AppointmentDetailViewModel::class.java]
        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        val conclusionAdapter = ConclusionAdapter(emptyList(), this)
        binding.conclusionImageRecyclerView.adapter = conclusionAdapter

        viewModel.getAppointmentDetail(appointmentArgument.appointmentId)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.appointmentDetailResponse.observe(viewLifecycleOwner) {
            binding.appointment = it
            conclusionAdapter.updateData(it.services)
        }

        binding.backToHomeBtn.setOnClickListener {
            activityViewModel.changeTab(0)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error) {
                    CustomSnackBar.showCustomSnackbar(binding.root, "${viewModel.errorMsg.value}")
                }
            }
        }

        binding.medicineImg.setOnClickListener {
            onShowImage(viewModel.appointmentDetailResponse.value?.medicine ?: "")
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppointmentDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppointmentDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun onShowImage(img: String) {
        val fullScreenImageFragment = FullScreenImageFragment.newInstance(img)
        fullScreenImageFragment.show(parentFragmentManager, "fullscreenImg")
    }

    override fun onMedicineResultClick(url: String) {
        if (Utilities.isExcelFile(url)) {
            // Create an Intent with ACTION_VIEW and set the MIME type for Excel files
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)


        } else {
            onShowImage(url)
        }
    }
}