package com.example.vcare_app.present.booking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.adapter.DepartmentAdapter
import com.example.vcare_app.api.api_model.response.Department
import com.example.vcare_app.data.repository.AppointmentFlow
import com.example.vcare_app.onclickinterface.OnDepartmentItemClick
import com.example.vcare_app.present.booking.appointment.AppointmentFragment
import com.example.vcare_app.utilities.CustomSnackBar
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment(), OnDepartmentItemClick {
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

    lateinit var viewModel: BookingFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)
        val searchingText = view.findViewById<EditText>(R.id.search_booking_text)

        viewModel = ViewModelProvider(this)[BookingFragmentViewModel::class.java]
        val doctorRecyclerView = view.findViewById<RecyclerView>(R.id.department_recyclerView)
        val adapter = DepartmentAdapter(emptyList(), this)
        doctorRecyclerView.adapter = adapter

        val hospitalId = arguments?.getInt("hospital_id") ?: 0
        viewModel.getDepartmentList(hospitalId)

        searchingText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val newList = viewModel.searchBooking(s.toString())
                    adapter.setData(newList)
                }

            }
        )

        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireActivity())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                viewModel.clearErrorMsg()
            }
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && viewModel.status.value == LoadingStatus.Error) {
                CustomSnackBar.showCustomSnackbar(view, it)
            }
        }
        viewModel.listDepartment.observe(viewLifecycleOwner) {
            adapter.setData(it)
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
         * @return A new instance of fragment BookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDepartmentClick(department: Department) {
        AppointmentFlow.apartment_id = department.id
        AppointmentFlow.hospital_id = department.hospitalID
        AppointmentFlow.department_name = department.name
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            replace(R.id.fragment_container_view, AppointmentFragment())
            addToBackStack("appointment_fragment")
            commit()
        }
    }


}