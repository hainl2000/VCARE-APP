package com.example.vcare_app.present.booking.hospitalbooking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vcare_app.R
import com.example.vcare_app.adapter.HospitalAdapter
import com.example.vcare_app.api.api_model.response.Hospital
import com.example.vcare_app.onclickinterface.OnHospitalClick
import com.example.vcare_app.present.booking.BookingFragment
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.example.vcare_app.utilities.PaginationScrollListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HospitalBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HospitalBookingFragment : Fragment(), OnHospitalClick {
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

    lateinit var viewModel: HospitalBookingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hospital_booking, container, false)
        viewModel = ViewModelProvider(this)[HospitalBookingViewModel::class.java]
        val recyclerView = view.findViewById<RecyclerView>(R.id.hospital_recycler_view)
        val searchText = view.findViewById<EditText>(R.id.search_hospital_text)
        val loadMoreProgressBar = view.findViewById<ProgressBar>(R.id.load_more_progress)
        val adapter = HospitalAdapter(emptyList(), this)
        recyclerView.adapter = adapter



        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMoreHospital()
            }

            override fun isLastPage(): Boolean {
                return recyclerView.adapter?.itemCount!! >= viewModel.totalcount
            }

            override fun isLoading(): Boolean {
                return viewModel.loadMoreStatus.value ?: false
            }

        })

        viewModel.listHospital.observe(viewLifecycleOwner) {
            adapter.setData(it)
            Log.d("TAG", it.joinToString())
        }
        viewModel.loadMoreStatus.observe(viewLifecycleOwner) {
            if (it) {
                loadMoreProgressBar.visibility = View.VISIBLE
            } else {
                loadMoreProgressBar.visibility = View.GONE
            }
        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val newList = viewModel.searchHospital(s.toString())
                adapter.setData(newList)
            }
        })
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == LoadingStatus.Loading) {
                LoadingDialogManager.showDialog(requireContext())
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it == LoadingStatus.Error && !viewModel.errorMsg.value.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "${viewModel.errorMsg.value}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return view.rootView
    }

    override fun onStart() {
        super.onStart()
        viewModel.getHospitalList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HospitalBookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HospitalBookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onHospitalItemClick(hospital: Hospital) {
        parentFragmentManager.beginTransaction().apply {
            val bookingFragment = BookingFragment()
            val bundle = Bundle()
            bundle.putInt("hospital_id", hospital.id)
            Log.d("HospitalId: ", "${hospital.id}")
            bookingFragment.arguments = bundle
            replace(R.id.fragment_container_view, bookingFragment)
            addToBackStack("hospital_booking")
            setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            commit()
        }
    }
}