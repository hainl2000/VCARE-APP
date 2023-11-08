package com.example.vcare_app.utilities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.vcare_app.R
import com.github.chrisbanes.photoview.PhotoView

class FullScreenImageFragment : DialogFragment() {
    private var imageUrl: String? = null

    companion object {
        fun newInstance(imageUrl: String): FullScreenImageFragment {
            val fragment = FullScreenImageFragment()
            val args = Bundle()
            args.putString("image_url", imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUrl = arguments?.getString("image_url")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            Dialog(requireContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)

        dialog.window?.apply {

            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_full_screen, container, false)
        val photoView = view.findViewById<PhotoView>(R.id.photoview)

        // Load the image using Glide or your preferred image loading library
        val requestOptions = RequestOptions().centerInside()
        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .error(R.drawable.logo_vcare)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(photoView)
        return view
    }
}