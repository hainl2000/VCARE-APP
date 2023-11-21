package com.example.vcare_app.present.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.mainactivity.MainActivityViewModel
import com.example.vcare_app.R
import com.example.vcare_app.adapter.NewsAdapter
import com.example.vcare_app.adapter.ViewPagerAdapter
import com.example.vcare_app.databinding.FragmentHomeBinding
import com.example.vcare_app.model.News
import com.example.vcare_app.onclickinterface.OnCardItemClick
import com.example.vcare_app.present.sos.SOSFragment
import java.util.Timer
import java.util.TimerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnCardItemClick {
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

    private lateinit var handler: Handler
    private lateinit var timer: Timer

    lateinit var binding: FragmentHomeBinding
    lateinit var activityViewModel: MainActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        binding.viewpager2.adapter = ViewPagerAdapter(
            listOf(
                R.drawable.banner1, R.drawable.banner2, R.drawable.banner3
            )
        )
        startAutoScroll()


        val newsRecyclerViewAdapter = NewsAdapter(
            listOf(
                News("R.drawable.banner1", "Banner1", "whofre are you"),
                News("R.drawable.banner1", "Banner1", "whoe are you\nfreiushfres"),
                News(
                    "R.drawable.banner1",
                    "Banner1",
                    "who grare you\nfrhuifheirsuhfresf\nfreuygfhuers"
                ),
                News("R.drawable.banner1", "Banner1", "who are you"),
                News("R.drawable.banner1", "Banner1", "who are you")
            ), this
        )
        binding.newsRecyclerView.apply {
            adapter = newsRecyclerViewAdapter
        }

        binding.homeBookingBtn.setOnClickListener {
            activityViewModel.changeTab(1)
        }

        binding.homeNotificationBtn.setOnClickListener {
            activityViewModel.changeTab(2)
        }
        binding.homePersonalBtn.setOnClickListener {
            activityViewModel.changeTab(3)
        }

        binding.sosBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container_view, SOSFragment())
                addToBackStack("sos")
                commit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
    }

    private fun startAutoScroll() {
        handler = Handler()
        timer = Timer()

        val scrollRunnable = Runnable {
            val currentItem = binding.viewpager2.currentItem
            val totalCount = binding.viewpager2.adapter?.itemCount ?: 0
            val nextItem = (currentItem + 1) % totalCount
            binding.viewpager2.setCurrentItem(nextItem, true)
        }

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post(scrollRunnable)
            }
        }, 0, 3000) // Scroll every 3 seconds (3000 milliseconds)
    }

    private fun stopAutoScroll() {
        timer.cancel()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCardItemClick(news: News) {
        TODO("Not yet implemented")
    }
}