package com.example.vcare_app.login.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.vcare_app.MainActivity
import com.example.vcare_app.R
import com.example.vcare_app.login.LoginActivityViewModel
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
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

    val viewModel: SignInFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val loginBtn = view.findViewById<Button>(R.id.sign_in_confirm_btn)
        val errorView = view.findViewById<TextView>(R.id.error_text_sign_in)
        val userName = view.findViewById<EditText>(R.id.sign_in_user_name_input)
        val password = view.findViewById<EditText>(R.id.sign_in_password_input)
        loginBtn.setOnClickListener {
            viewModel.login(userName.text.toString(), password.text.toString())
        }
        viewModel.error.observe(requireActivity()) {
            errorView.text = it
        }
        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireActivity())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Success) {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                }
                if (it == LoadingStatus.Error) {
                    Log.e("ERROR","${viewModel.error.value}")
//                    Toast.makeText(this, "${viewModel.error.value}", Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}