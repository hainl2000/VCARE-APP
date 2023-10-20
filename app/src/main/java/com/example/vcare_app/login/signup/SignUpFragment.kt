package com.example.vcare_app.login.signup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.vcare_app.R
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.SuccessDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
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

    val viewModel: SignUpFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val textError = view.findViewById<TextView>(R.id.error_text_sign_up)
        val btnSignUp = view.findViewById<Button>(R.id.sign_up_confirm_btn)
        val editTextPassword = view.findViewById<EditText>(R.id.sign_up_password_input)
        val editTextConfirmPassword =
            view.findViewById<EditText>(R.id.sign_up_confirm_password_input)
        val editTextSocialInsuranceNumber =
            view.findViewById<EditText>(R.id.sign_up_social_insurance_number_input)
        val editTextIdentityNumber = view.findViewById<EditText>(R.id.sign_up_identity_number_input)
        val email = view.findViewById<EditText>(R.id.sign_up_user_name_input)
        val phoneNumber = view.findViewById<EditText>(R.id.sign_up_phone_input)

        btnSignUp.setOnClickListener {
            if (editTextConfirmPassword.text.toString() == editTextPassword.text.toString()) {
                viewModel.signUp(
                    email.text.toString(),
                    phoneNumber.text.toString(),
                    editTextSocialInsuranceNumber.text.toString(),
                    editTextIdentityNumber.text.toString(),
                    editTextPassword.text.toString()
                )
            }else{
                textError.text = "Lỗi: 2 mật khẩu không trùng nhau."
            }
        }
        viewModel.error.observe(viewLifecycleOwner){
            textError.text = it
        }
        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireActivity())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Success) {
                    SuccessDialog.showDialog(requireContext())
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(Runnable {
                        SuccessDialog.dissmissSuccessDialog()
                    }, 2000)
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
         * @return A new instance of fragment SIgnUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}