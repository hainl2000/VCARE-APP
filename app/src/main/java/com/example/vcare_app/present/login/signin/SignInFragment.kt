package com.example.vcare_app.present.login.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.vcare_app.MainActivity
import com.example.vcare_app.data.sharepref.SharePrefManager
import com.example.vcare_app.databinding.FragmentSignInBinding
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

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

    private val viewModel: SignInFragmentViewModel by viewModels()
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater)

        if (SharePrefManager.getStatusSave()) {
            viewModel.login(SharePrefManager.getEmail(), SharePrefManager.getPassword())
        }

        binding.signInUserNameInput.setText(SharePrefManager.getEmail())
        binding.signInPasswordInput.setText(SharePrefManager.getPassword())
        binding.signInCheckData.isChecked = SharePrefManager.getStatusSave()
        binding.signInBtn.setOnClickListener {
            viewModel.login(
                binding.signInUserNameInput.text.toString(),
                binding.signInPasswordInput.text.toString()
            )
        }
        viewModel.errorMsg.observe(requireActivity()) {
            binding.errorTextSignIn.text = it
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireActivity())
            } else if (it == LoadingStatus.Success) {
                LoadingDialogManager.dismissLoadingDialog()

                if (binding.signInCheckData.isChecked) {
                    viewModel.saveUserData(
                        email = binding.signInUserNameInput.text.toString(),
                        password = binding.signInPasswordInput.text.toString()
                    )
                } else {
                    viewModel.saveUserData("", "", false)
                }
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }else if (it == LoadingStatus.Error){
                LoadingDialogManager.dismissLoadingDialog()
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