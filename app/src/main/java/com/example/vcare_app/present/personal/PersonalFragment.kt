package com.example.vcare_app.present.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.vcare_app.present.personal.history.HistoryFragment
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

        viewModel.getUserProfile()
        val listSetting = listOf(
            SettingsItem(R.drawable.edit_icon, resources.getString(R.string.edit)),
            SettingsItem(R.drawable.history_icon, resources.getString(R.string.history)),
            SettingsItem(
                R.drawable.password_change_icon,
                resources.getString(R.string.change_password)
            ),
            SettingsItem(R.drawable.logout_icon, resources.getString(R.string.logout))
        )
        binding.settingsRecyclerView.adapter = SettingsAdapter(listSetting, this)
        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.userProfile = it
            Glide.with(this).load(it.avatar)
                .error(R.drawable.error_icon)
                .into(binding.circleImageView)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error && !viewModel.errorMsg.value.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "${viewModel.errorMsg}", Toast.LENGTH_LONG)
                        .show()
                }
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
                    val editFragment = EditPersonalFragment()
                    val bundle = Bundle().apply {
                        putSerializable("data", viewModel.userProfile.value)
                    }
                    editFragment.arguments = bundle
                    replace(R.id.fragment_container_view, editFragment)
                    addToBackStack("edit_personal")
                    commit()
                }
            }

            resources.getString(R.string.history) -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_view,HistoryFragment())
                    addToBackStack("history")
                    commit()
                }
            }
            resources.getString(R.string.change_password) -> {}
            resources.getString(R.string.logout) -> {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SharePrefManager.deleteAllSavedData()
            }
        }
    }
}