package com.guru.covid19tracker.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.guru.covid19tracker.R
import kotlinx.android.synthetic.main.fragment_image_dailog.*


private const val URL = "url"

class ImageDailogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(URL)
        }
        setStyle(STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_dailog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close.setOnClickListener { dialog?.dismiss() }
        Glide.with(image.context)
            .load(url)
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.light_grey)
            .into(image)
    }

    companion object {

        fun newInstance(url: String) =
            ImageDailogFragment().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                }
            }
    }
}
