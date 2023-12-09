package com.example.vcare_app.present.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.adapter.NewsAdapter
import com.example.vcare_app.adapter.ViewPagerAdapter
import com.example.vcare_app.databinding.FragmentHomeBinding
import com.example.vcare_app.mainactivity.MainActivityViewModel
import com.example.vcare_app.model.News
import com.example.vcare_app.onclickinterface.OnCardItemClick
import com.example.vcare_app.present.sos.SOSFragment
import com.example.vcare_app.utilities.TabItem
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
                News("R.drawable.banner1", "Dược phẩm nào là tốt nhất dành cho giới trẻ", "Ngành dược học tại Việt Nam đang phát triển mạnh mẽ, với sự gia tăng của các cửa hàng thuốc và trung tâm y tế trên khắp đất nước. Các nhà thuốc không chỉ là nơi cung cấp thuốc chất lượng mà còn là địa điểm tư vấn chăm sóc sức khỏe. Việc chính phủ và ngành y tế đầu tư vào hạ tầng và đào tạo nhân sự dược học cũng đóng góp tích cực vào sự phát triển bền vững của ngành này. Việt Nam đang dần thúc đẩy nghiên cứu và sản xuất thuốc, tạo ra cơ hội lớn cho sự đổi mới và cải tiến trong lĩnh vực y tế."),
                News("R.drawable.banner2", "Vắc xin phòng chống bệnh hiếm tại Việt Nam", "\n" +
                        "Vắc xin đóng vai trò quan trọng trong hệ thống y tế của Việt Nam, đặc biệt là trong bối cảnh đối mặt với các thách thức về dịch bệnh. Chính phủ Việt Nam đã tích cực thúc đẩy chương trình tiêm chủng quốc gia, tạo điều kiện thuận lợi cho việc cung cấp vắc xin đến cộng đồng. Sự đầu tư vào nghiên cứu và phát triển vắc xin cũng đang ngày càng được tăng cường, giúp nâng cao khả năng phòng ngừa và kiểm soát bệnh tật trong cả nước. Hệ thống tiêm chủng đa dạng và hiệu quả đã đóng vai trò quan trọng trong việc bảo vệ sức khỏe cộng đồng và giảm áp lực cho hệ thống y tế trước những thách thức y tế toàn cầu."),
                News(
                    "R.drawable.banner3",
                    "Nhận thức về bệnh hiếm của tháng",
                    "Tháng Nhận Thức về Bệnh Hiếm là dịp quan trọng tại Việt Nam, nhấn mạnh tầm quan trọng của việc nâng cao hiểu biết về các bệnh lạ và thách thức mà những người bị ảnh hưởng phải đối mặt. Trong tháng này, cộng đồng y tế và xã hội hóa mình để tăng cường giáo dục và hỗ trợ cho những người và gia đình chịu ảnh hưởng bởi các bệnh hiếm. Các sự kiện và chiến dịch trong tháng nhận thức này không chỉ giúp nâng cao ý thức mà còn góp phần quan trọng vào việc cung cấp nguồn thông tin và hỗ trợ cho những người cần giúp đỡ."
                ),

            ), this
        )
        binding.newsRecyclerView.apply {
            adapter = newsRecyclerViewAdapter
        }

        binding.homeBookingBtn.setOnClickListener {
            activityViewModel.changeTab(TabItem.Booking.ordinal)
        }
        binding.homePersonalBtn.setOnClickListener {
            activityViewModel.changeTab(TabItem.Personal.ordinal)
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
    }
}