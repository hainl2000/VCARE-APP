package com.example.vcare_app.present.booking.appointment

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vcare_app.MainActivityViewModel
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.request.AppointmentRequest
import com.example.vcare_app.data.repository.AppointmentFlow
import com.example.vcare_app.databinding.FragmentAppointmentBinding
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.present.appointmentdetail.AppointmentDetailFragment
import com.example.vcare_app.utilities.CustomInformationDialog
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.NoticeWorker
import com.example.vcare_app.utilities.SuccessDialog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AppointmentViewModel::class.java]
        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        binding.lifecycleOwner = this
        binding.appointmentFlow = AppointmentFlow

        viewModel.getCurrentProfile()

        var timepick = ""
        var datepick = ""

        binding.timePickerBtn.setOnClickListener {
            TimePickerDialog(
                requireContext(), // Your context, typically an Activity or Fragment
                { _, hourOfDay, minute ->
                    if ((hourOfDay >= AppointmentFlow.startAtAm && hourOfDay < AppointmentFlow.endAtAm) || (hourOfDay >= AppointmentFlow.startAtPm && hourOfDay < AppointmentFlow.endAtPm)) {
                        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                        binding.timePickerBtn.text = selectedTime
                        timepick = selectedTime
                        viewModel.setErrorMsg("")
                    } else {
                        viewModel.setErrorMsg("Chọn thời gian đúng như đã yêu cầu")
                    }

                },
                7, // Initial hour (optional)
                0, // Initial minute (optional)
                true // true for 24-hour format, false for 12-hour format (optional)
            ).show()
        }

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
                        viewModel.setErrorMsg("Chọn ngày trong tương lai.")
                    }
                    // Month starts from 0
                    // Do something with the selectedDate
                },
                currentYear, // Initial year
                currentMonth, // Initial month
                currentDay, // Initial day
            ).show()
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty() && binding.datePickerBtn.text.isNotEmpty() && binding.timePickerBtn.text.isNotEmpty()) {
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
                        "$datepick $timepick",
                        viewModel.profile.value?.identityNumber ?: "",
                        viewModel.profile.value?.socialInsuranceNumber ?: "",
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
                    launchNotice.launch(Manifest.permission.POST_NOTIFICATIONS)
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

    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH)
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)


    private val launchNotice =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val inputData = Data.Builder()
                    .putString(NoticeWorker.NOTIFICATION_TITLE, "Khám bệnh!")
                    .putString(
                        NoticeWorker.NOTIFICATION_MESSAGE,
                        "Lịch khám ngày mai: ${viewModel.scheduleTime}"
                    )
                    .putInt(
                        NoticeWorker.NOTIFICATION_APPOINTMENT_ID,
                        viewModel.appointmentDetail.id
                    )
                    .build()
                val duration = calculateTimeDifferenceInMinutes(
                    viewModel.scheduleTime
                )
                if (duration.toInt() == -1) {
                    CustomSnackBar.showCustomSnackbar(
                        requireView(),
                        "Lỗi không nhận diện được ngày tháng."
                    )
                } else {
                    val worker = OneTimeWorkRequestBuilder<NoticeWorker>().setInputData(inputData)
                        .setInitialDelay(duration, TimeUnit.MINUTES)
                        .build()
                    WorkManager.getInstance(requireContext()).enqueue(
                        worker
                    )
                }

            } else {
                CustomInformationDialog.showCustomInformationDialog(
                    requireContext(),
                    requireContext().resources.getString(R.string.notification_permission_denied)
                ) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    activity?.startActivity(intent)
                }
            }
        }

    //
    private fun calculateTimeDifferenceInMinutes(targetDateTimeString: String): Long {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val targetDate = dateFormat.parse(targetDateTimeString)

            if (targetDate != null) {
                val currentDate = java.util.Calendar.getInstance().time
                val targetCalendar = java.util.Calendar.getInstance().apply { time = targetDate }

                val timeDifferenceInMillis = targetCalendar.timeInMillis - currentDate.time
                return timeDifferenceInMillis / (1000 * 60)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }

        return -1 // Return -1 if there is an error in parsing or calculation
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