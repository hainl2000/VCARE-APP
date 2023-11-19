package com.example.vcare_app.present.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.databinding.FragmentSignUpBinding
import com.example.vcare_app.present.login.LoginActivityViewModel
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

    private val viewModel: SignUpFragmentViewModel by viewModels()
    private lateinit var activityViewModel: LoginActivityViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater)
        activityViewModel = ViewModelProvider(requireActivity())[LoginActivityViewModel::class.java]


        binding.signUpBtn.setOnClickListener {
            if (binding.signUpConfirmPasswordInput.text.toString() == binding.signUpPasswordInput.text.toString()) {
                viewModel.signUp(
                    binding.signUpUserNameInput.text.toString(),
                    binding.signUpPhoneInput.text.toString(),
                    binding.signUpSocialInsuranceNumberInput.text.toString(),
                    binding.signUpIdentityNumberInput.text.toString(),
                    binding.signUpPasswordInput.text.toString()
                )
            } else {
                binding.errorTextSignUp.text = "Lỗi: 2 mật khẩu không trùng nhau."
            }
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            binding.errorTextSignUp.text = it
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireActivity())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Success) {
                    SuccessDialog.showDialog(requireContext()) {
                        activityViewModel.setCurrentIndex(0)
                    }
                }
//                val handler = Handler(Looper.getMainLooper())
//                handler.postDelayed(Runnable {
//                    SuccessDialog.dissmissSuccessDialog()
//                    activityViewModel.setCurrentIndex(0)
//                }, 2000)


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
         * @return A new instance of fragment SIgnUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = SignUpFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}