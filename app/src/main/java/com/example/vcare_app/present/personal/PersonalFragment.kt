package com.example.vcare_app.present.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vcare_app.R
import com.example.vcare_app.adapter.SettingsAdapter
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.databinding.FragmentPersonalBinding
import com.example.vcare_app.model.SettingsItem
import com.example.vcare_app.onclickinterface.OnSettingClick
import com.example.vcare_app.present.login.LoginActivity
import com.example.vcare_app.present.personal.editpersonal.EditPersonalFragment
import com.example.vcare_app.present.personal.healthstatus.HealthStatusFragment
import com.example.vcare_app.present.personal.history.HistoryFragment
import com.example.vcare_app.present.personal.patientprofile.PatientProfileFragment
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFragment : Fragment(), OnSettingClick {
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


    lateinit var binding: FragmentPersonalBinding
    private lateinit var viewModel: PersonalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalBinding.inflate(inflater)

        viewModel = ViewModelProvider(this)[PersonalViewModel::class.java]
        binding.refreshLayoutPersonal.setOnRefreshListener {
            viewModel.getUserProfile()
            binding.refreshLayoutPersonal.isRefreshing = false
        }
        viewModel.getUserProfile()
        val listSetting = listOf(
            SettingsItem(R.drawable.edit_icon, resources.getString(R.string.edit)),
            SettingsItem(
                R.drawable.health_status_icon,
                resources.getString(R.string.health_status_information)
            ),
            SettingsItem(R.drawable.history_icon, resources.getString(R.string.history)),
            SettingsItem(
                R.drawable.patient_profile_icon,
                resources.getString(R.string.patient_profile)
            ),
            SettingsItem(R.drawable.logout_icon, resources.getString(R.string.logout)),
        )
        binding.settingsRecyclerView.adapter = SettingsAdapter(listSetting, this)
        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.userProfile = it
            Glide.with(this).load(it.avatar)
                .error(R.drawable.logo_vcare)
                .into(binding.circleImageView)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
            }
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                CustomSnackBar.showCustomSnackbar(binding.root, it)
            }
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSettingClick(settings: SettingsItem) {
        when (settings.title) {
            resources.getString(R.string.edit) -> {
                parentFragmentManager.beginTransaction().apply {
                    setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                    val editFragment = EditPersonalFragment()
                    replace(R.id.fragment_container_view, editFragment)
                    addToBackStack("edit_personal")
                    commit()
                }
            }

            resources.getString(R.string.health_status_information) -> {
                parentFragmentManager.beginTransaction().apply {
                    setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                    replace(R.id.fragment_container_view, HealthStatusFragment())
                    addToBackStack("health_status")
                    commit()
                }
            }

            resources.getString(R.string.history) -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_view, HistoryFragment())
                    addToBackStack("history")
                    commit()
                }
            }

            resources.getString(R.string.patient_profile) -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_view, PatientProfileFragment())
                    addToBackStack("patient_profile")
                    commit()
                }
            }

            resources.getString(R.string.logout) -> {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SharePrefManager.deleteAllSavedData()
            }
        }
    }
}