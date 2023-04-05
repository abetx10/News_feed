package com.example.newsfeed.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newsfeed.R
import com.example.newsfeed.data.NewsSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceFilter : Fragment() {
    private val viewModel: NewsFeedViewModel by activityViewModels()
    private lateinit var btnHabr: Button
    private lateinit var btnTechCruncher: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_source_filter, container, false)

        btnHabr = view.findViewById(R.id.btnHabrFilter)
        btnTechCruncher = view.findViewById(R.id.btnTechFilter)

        btnHabr.setOnClickListener {
            Log.d("SourceFilter", "btnHabr clicked")
            navigateToMainFragment(NewsSource.HABR)
        }

        btnTechCruncher.setOnClickListener {
            Log.d("SourceFilter", "btnTechCruncher clicked")
            navigateToMainFragment(NewsSource.TECHCRUNCHER)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToMainFragment(source: NewsSource) {
        viewModel.setSelectedSource(source)
        viewModel.combineNewsItems()
        parentFragmentManager.popBackStack()
    }
}