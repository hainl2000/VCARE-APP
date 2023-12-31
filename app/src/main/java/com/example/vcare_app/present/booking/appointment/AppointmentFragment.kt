package com.example.vcare_app.present.booking.appointment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.data.repository.AppointmentFlow
import com.example.vcare_app.databinding.FragmentAppointmentBinding
import com.example.vcare_app.mainactivity.MainActivityViewModel
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.present.appointmentdetail.AppointmentDetailFragment
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.SuccessDialog
import com.example.vcare_app.utilities.Utilities
import java.time.LocalTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentFragment : Fragment() {
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

    lateinit var binding: FragmentAppointmentBinding
    lateinit var viewModel: AppointmentViewModel
    private lateinit var activityViewModel: MainActivityViewModel
    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH)
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AppointmentViewModel::class.java]
        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        binding.lifecycleOwner = this
        binding.appointmentFlow = AppointmentFlow
        binding.checkboxAddTime.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.pickPeriodLayout.visibility = View.VISIBLE
            } else {
                binding.pickPeriodLayout.visibility = View.GONE
            }
        }
        viewModel.getCurrentProfile()
        var datepick = ""
        var timePick = Utilities.INIT_PERIOD_TIME
        binding.datePickerBtn.setOnClickListener {
            DatePickerDialog(
                requireActivity(), // Context
                { _, year, month, dayOfMonth ->
                    if (currentYear < year || (currentYear == year && month > currentMonth) || (currentYear == year && month == currentMonth && currentDay < dayOfMonth)) {
                        val selectedDate =
                            String.format("%02d-%02d-%02d", year, month + 1, dayOfMonth)
                        binding.datePickerBtn.text = selectedDate
                        datepick = selectedDate
                        viewModel.setErrorMsg("")

                    } else {
                        val currentTime = LocalTime.now()
                        viewModel.setErrorMsg("Hãy chọn ngày trong tương lai.")
                        // Định nghĩa giờ cố định (ví dụ: 8 giờ)
                        val specificTime = LocalTime.of(7, 0)
                        if (currentYear == year && month == currentMonth && currentDay == dayOfMonth && currentTime.isAfter(
                                specificTime
                            )
                        ) {
                            viewModel.setErrorMsg("Đã quá giờ ưu tiên đặt lịch trong ngày hôm nay, xin chọn ngày khác")
                        }


                    }
                },
                currentYear, // Initial year
                currentMonth, // Initial month
                currentDay, // Initial day
            ).show()
        }
        val items = Utilities.listPeriod.keys.toList()
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_spinner_custom, items)
        binding.pickPeriodSpinner.adapter = adapter
        binding.pickPeriodSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    timePick = Utilities.listPeriod[items[position]] ?: Utilities.INIT_PERIOD_TIME
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    timePick = Utilities.INIT_PERIOD_TIME
                }
            }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty() && binding.datePickerBtn.text.isNotEmpty()) {
                binding.errorTv.text = ""
                binding.btnDatKham.visibility = View.VISIBLE
            } else {
                binding.errorTv.text = it
                binding.btnDatKham.visibility = View.GONE
            }
        }
        binding.btnDatKham.setOnClickListener {
            if (viewModel.errorMsg.value?.isNotEmpty() == true) {
                CustomSnackBar.showCustomSnackbar(binding.root, "Hoàn thành yêu cầu trước")
            } else {
                viewModel.createAppointment(
                    AppointmentRequest(
                        AppointmentFlow.apartment_id,
                        AppointmentFlow.hospital_id,
                        binding.symptomInput.text.toString(),
                        datepick,
                        viewModel.profile.value?.identityNumber ?: "",
                        viewModel.profile.value?.socialInsuranceNumber ?: "",
                        if (binding.checkboxAddTime.isChecked) timePick else null,
                        viewModel.profile.value?.address ?: ""
                    )
                )
            }
        }

        viewModel.status.observe(
            viewLifecycleOwner
        ) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Success && viewModel.createSuccess.value == true && viewModel.scheduleTime.isNotEmpty()) {
                    AppointmentFlow.isFromAppointment = true
                    SuccessDialog.showDialog(requireContext()) {
                        parentFragmentManager.beginTransaction().apply {
                            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                            val fragment = AppointmentDetailFragment()
                            val argument = AppointmentDetailArgument(
                                viewModel.appointmentDetail.id
                            )
                            val bundle = Bundle().apply {
                                putParcelable("appointment_id", argument)
                            }
                            fragment.arguments = bundle
                            replace(R.id.fragment_container_view, fragment)
                            commit()
                        }
                    }

                }
            }
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
         * @return A new instance of fragment AppointmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppointmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}