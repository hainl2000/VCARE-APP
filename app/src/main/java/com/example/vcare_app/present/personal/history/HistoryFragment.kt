package com.example.vcare_app.present.personal.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.api.api_model.response.HistoryAppointment
import com.example.vcare_app.onclickinterface.OnAppointmentClick
import com.example.vcare_app.present.appointmentdetail.AppointmentDetailFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(), OnAppointmentClick {
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

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        val recyclerView = view.findViewById<RecyclerView>(R.id.appointment_history_recycler_view)
        val searchAppointment = view.findViewById<EditText>(R.id.search_appointment_text)
        val adapter = HistoryAppointmentAdapter(emptyList(), this)
        viewModel.getHistory()
        searchAppointment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val newList = viewModel.searchAppointment(s.toString())
                adapter.updateData(newList)
            }

        })

        recyclerView.adapter = adapter
        viewModel.listAppointment.observe(viewLifecycleOwner) {
            adapter.updateData(it.data)
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error) {
                    Toast.makeText(
                        requireContext(),
                        "${viewModel.errorMsg.value}",
                        Toast.LENGTH_LONG
                    ).show()
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
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAppointmentClick(historyAppointment: HistoryAppointment) {
        parentFragmentManager.beginTransaction().apply {
            val fragment = AppointmentDetailFragment()
            val bundle = Bundle().apply {
                putInt("appointment_id", historyAppointment.id)
            }
            fragment.arguments = bundle
            replace(R.id.fragment_container_view, fragment)
            addToBackStack("appointment_detail")
            commit()
        }
    }
}