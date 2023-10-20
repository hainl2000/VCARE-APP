package com.example.vcare_app.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.vcare_app.R
import com.example.vcare_app.adapter.NewsAdapter
import com.example.vcare_app.adapter.ViewPagerAdapter
import com.example.vcare_app.model.News
import com.example.vcare_app.onclickinterface.OnCardItemClick
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
        retainInstance = true
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Songlai", "Songlai")
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager2 = view.findViewById<ViewPager2>(R.id.viewpager2)
        viewPager2.adapter = ViewPagerAdapter(
            listOf(
                R.drawable.banner1, R.drawable.banner2, R.drawable.banner3
            )
        )
        startAutoScroll()

        val newsRecyclerView = view.findViewById<RecyclerView>(R.id.news_recycler_view)
        val newsRecyclerViewAdapter = NewsAdapter(
            listOf(
                News("R.drawable.banner1", "Banner1", "whofre are you"),
                News("R.drawable.banner1", "Banner1", "whoe are you\nfreiushfres"),
                News("R.drawable.banner1", "Banner1", "who grare you\nfrhuifheirsuhfresf\nfreuygfhuers"),
                News("R.drawable.banner1", "Banner1", "who are you"),
                News("R.drawable.banner1", "Banner1", "who are you")
            ), this
        )
        newsRecyclerView.apply {
            adapter = newsRecyclerViewAdapter
        }
        Log.i("Viewpager", "${viewPager2.currentItem}")
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
    }

    private fun startAutoScroll() {
        handler = Handler()
        timer = Timer()

        val scrollRunnable = Runnable {
            val currentItem = viewPager2.currentItem
            val totalCount = viewPager2.adapter?.itemCount ?: 0
            val nextItem = (currentItem + 1) % totalCount
            viewPager2.setCurrentItem(nextItem, true)
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