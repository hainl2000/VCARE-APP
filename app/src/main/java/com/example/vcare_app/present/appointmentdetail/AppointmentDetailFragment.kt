package com.example.vcare_app.present.appointmentdetail

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vcare_app.R
import com.example.vcare_app.adapter.ConclusionAdapter
import com.example.vcare_app.data.repository.AppointmentFlow
import com.example.vcare_app.databinding.FragmentAppointmentDetailBinding
import com.example.vcare_app.mainactivity.MainActivityViewModel
import com.example.vcare_app.model.AppointmentDetailArgument
import com.example.vcare_app.onclickinterface.OnImageOrUrlClick
import com.example.vcare_app.utilities.CustomInformationDialog
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.FullScreenImageFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.NoticeWorker
import com.example.vcare_app.utilities.Utilities
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentDetailFragment : Fragment(), OnImageOrUrlClick {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentArgument =
                it.getParcelable<AppointmentDetailArgument>("appointment_id") as AppointmentDetailArgument
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
            if (viewModel.appointmentDetailResponse.value?.reExamination != null) {
                val inputData = Data.Builder()
                    .putString(NoticeWorker.NOTIFICATION_TITLE, "Khám bệnh!")
                    .putString(
                        NoticeWorker.NOTIFICATION_MESSAGE,
                        "Lịch tái khám ${viewModel.appointmentDetailResponse.value?.reExamination}"
                    )
                    .putInt(
                        NoticeWorker.NOTIFICATION_APPOINTMENT_ID,
                        viewModel.appointmentDetailResponse.value?.id ?: 0
                    )
                    .build()
                val duration = if (Build.VERSION.SDK_INT >= 30) {
                    calculateTimeDifferenceInMinutesForHigher30(
                        "${viewModel.appointmentDetailResponse.value?.reExamination} 00:00"
                    )
                } else {
                    calculateTimeDifferenceInMinutes(
                        "${viewModel.appointmentDetailResponse.value?.reExamination} 00:00"

                    )
                }

                val workerReExam =
                    OneTimeWorkRequestBuilder<NoticeWorker>().setInputData(inputData)
                        .setInitialDelay(duration - 1440, TimeUnit.MINUTES)
                        .build()
                WorkManager.getInstance(requireContext()).enqueueUniqueWork(
                    "chucnang",
                    ExistingWorkPolicy.REPLACE,
                    workerReExam
                )

                Log.d("TAGG", "bi len thot r")

            }
            if (AppointmentFlow.isFromAppointment) {
                launchNotice.launch(Manifest.permission.POST_NOTIFICATIONS)
                AppointmentFlow.isFromAppointment = false
            }
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

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                CustomSnackBar.showCustomSnackbar(requireView(), it)
            }
        }

        binding.medicineImg.setOnClickListener {
            onShowImage(viewModel.appointmentDetailResponse.value?.medicine ?: "")
        }

        return binding.root
    }

    private val launchNotice =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val inputData = Data.Builder()
                    .putString(NoticeWorker.NOTIFICATION_TITLE, "Khám bệnh!")
                    .putString(
                        NoticeWorker.NOTIFICATION_MESSAGE,
                        "Lịch khám ${viewModel.appointmentDetailResponse.value?.suggestTime} ${viewModel.appointmentDetailResponse.value?.timeInString}"
                    )
                    .putInt(
                        NoticeWorker.NOTIFICATION_APPOINTMENT_ID,
                        viewModel.appointmentDetailResponse.value?.id ?: 0
                    )
                    .build()
                val duration =
                    if (Build.VERSION.SDK_INT >= 30) {
                        calculateTimeDifferenceInMinutesForHigher30(
                            "${viewModel.appointmentDetailResponse.value?.timeInString} ${viewModel.appointmentDetailResponse.value?.suggestTime}"
                        )
                    } else {
                        calculateTimeDifferenceInMinutes(
                            "${viewModel.appointmentDetailResponse.value?.timeInString} ${viewModel.appointmentDetailResponse.value?.suggestTime}"

                        )
                    }
                Log.d("Duration", ": $duration")
                if (duration.toInt() == -1) {
                    CustomSnackBar.showCustomSnackbar(
                        requireView(),
                        "Lỗi không nhận diện được ngày tháng."
                    )
                } else {

                    Log.d("TAGG", "ro rang la phai show notifi")
                    val worker1DayBefore =
                        OneTimeWorkRequestBuilder<NoticeWorker>().setInputData(inputData)
                            .setInitialDelay(duration - 1440, TimeUnit.MINUTES)
                            .build()
                    WorkManager.getInstance(requireContext()).enqueue(
                        worker1DayBefore
                    )
                    val worker1HourBefore =
                        OneTimeWorkRequestBuilder<NoticeWorker>().setInputData(inputData)
                            .setInitialDelay(duration - 60, TimeUnit.MINUTES)
                            .build()
                    WorkManager.getInstance(requireContext()).enqueue(
                        worker1HourBefore
                    )
                }

            } else {
                Log.d("TAGG", "Tai sao deo show")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateTimeDifferenceInMinutesForHigher30(targetDateTimeString: String): Long {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val targetDate = dateFormat.parse(targetDateTimeString)

            if (targetDate != null) {

                val currentDate = LocalDateTime.now()
                val targetDateTime =
                    targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                val duration = Duration.between(currentDate, targetDateTime)
                return duration.toMinutes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }

        return -1
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

    override fun onImageOrUrlClick(url: String) {
        if (Utilities.isBrowserUrl(url)) {
            // Create an Intent with ACTION_VIEW and set the MIME type for Excel files
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        if (Utilities.isExcel(url)) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else {
            onShowImage(url)
        }
    }
}