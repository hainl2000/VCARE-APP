package com.example.vcare_app.present.personal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.adapter.SettingsAdapter
import com.example.vcare_app.model.SettingsItem
import com.example.vcare_app.onclickinterface.OnSettingClick
import com.example.vcare_app.data.repository.CurrentUser
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.databinding.FragmentPersonalBinding
import com.example.vcare_app.present.login.LoginActivity
import com.example.vcare_app.utilities.ItemSettings

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

    private val listSetting = listOf(
        SettingsItem(R.drawable.edit_icon, ItemSettings.Edit.name),
        SettingsItem(R.drawable.history_icon, ItemSettings.History.name),
        SettingsItem(R.drawable.password_change_icon, ItemSettings.ChangePassword.name),
        SettingsItem(R.drawable.delete_icon, ItemSettings.DeleteAccount.name),
        SettingsItem(R.drawable.logout_icon, ItemSettings.Logout.name)
    )

    lateinit var binding: FragmentPersonalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalBinding.inflate(inflater)

        binding.userProfile = CurrentUser.data
        binding.settingsRecyclerView.adapter = SettingsAdapter(listSetting, this)
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
            ItemSettings.Edit.name -> {}
            ItemSettings.History.name -> {}
            ItemSettings.ChangePassword.name -> {}
            ItemSettings.DeleteAccount.name -> {}
            ItemSettings.Logout.name -> {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SharePrefManager.deleteAllSavedData()
            }
        }
    }
}