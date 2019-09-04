package com.github.travelplannerapp.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.travelplannerapp.R
import kotlinx.android.synthetic.main.fullscreen_scan.*


class FullScreenDialog(private val fileName: String) : DialogFragment() {

    companion object {
        const val TAG = "FULLSCREEN DIALOG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fullscreen_scan, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(view)
                .load(fileName)
                .apply(requestOptions)
                .into(imageViewFullscreenScan)

        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        imageViewCloseFullscreen.setOnClickListener { dismiss() }
    }

}
