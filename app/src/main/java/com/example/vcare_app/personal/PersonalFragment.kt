package com.example.vcare_app.personal

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
import com.example.vcare_app.repository.CurrentUser
import com.example.vcare_app.utilities.ItemSettings
import org.w3c.dom.Text

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
        SettingsItem(R.drawable.resume_icon, ItemSettings.Profile.name),
        SettingsItem(R.drawable.history_icon, ItemSettings.History.name),
        SettingsItem(R.drawable.password_change_icon, ItemSettings.ChangePassword.name),
        SettingsItem(R.drawable.delete_icon, ItemSettings.DeleteAccount.name),
        SettingsItem(R.drawable.logout_icon, ItemSettings.Logout.name)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_personal, container, false)
        val email = view.findViewById<TextView>(R.id.email)
        val phoneNumber = view.findViewById<TextView>(R.id.user_phone)
        val recyclerView = view.findViewById<RecyclerView>(R.id.settings_recyclerView)
        recyclerView.adapter = SettingsAdapter(listSetting, this)
        email.text = CurrentUser.data.email
        phoneNumber.text = CurrentUser.data.phone
        return view
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
            ItemSettings.Profile.name -> {}
            ItemSettings.History.name -> {}
            ItemSettings.ChangePassword.name -> {}
            ItemSettings.DeleteAccount.name -> {}
            ItemSettings.Logout.name -> {}
        }
    }
}