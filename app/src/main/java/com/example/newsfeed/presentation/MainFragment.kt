package com.example.newsfeed.presentation

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsfeed.R
import com.example.newsfeed.domain.model.UnifiedNewsItem
import com.example.newsfeed.presentation.adapter.ListItemsAdapter
import com.example.newsfeed.presentation.decoration.SpaceItemDecoration
import com.example.newsfeed.utils.isInternetAvailable
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: NewsFeedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: ListItemsAdapter
    private lateinit var favoriteNewsAdapter: ListItemsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        if (!isInternetAvailable(requireContext())) {
            showErrorDialog(getString(R.string.no_internet))
        }

        recyclerView = view.findViewById(R.id.recyclerView)

        val onItemClick: (UnifiedNewsItem) -> Unit = { unifiedNewsItem ->
            openLinkInCustomTab(unifiedNewsItem.link)
        }

        val onFavoriteClick: (UnifiedNewsItem, Boolean) -> Unit = { unifiedNewsItem, isFavorite ->
            viewModel.toggleFavorite(unifiedNewsItem, isFavorite)
        }

        newsAdapter = ListItemsAdapter(listOf(), onItemClick, onFavoriteClick)
        favoriteNewsAdapter = ListItemsAdapter(listOf(), onItemClick, onFavoriteClick)

        recyclerView.adapter = newsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(SpaceItemDecoration(15))

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item_1 -> {
                    recyclerView.adapter = newsAdapter
                    viewModel.setSelectedSource(null)
                }
                R.id.navigation_item_2 -> {
                    parentFragmentManager.commit {
                        replace(R.id.container, SourceFilter())
                        addToBackStack(null)
                    }
                }
                R.id.navigation_item_3 -> {
                    recyclerView.adapter = favoriteNewsAdapter
                }
            }
            true
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshNews()
        }

        viewModel.newsItems.observe(viewLifecycleOwner) { newsItems ->
            Log.d("MainFragment", "Observed changes in newsItems: $newsItems")
            newsAdapter.submitList(newsItems)
        }

        viewModel.favoriteNewsItems.observe(viewLifecycleOwner) { favoriteNewsItems ->
            Log.d("MainFragment", "Observed changes in favoriteNewsItems: $favoriteNewsItems")
            favoriteNewsAdapter.submitList(favoriteNewsItems)
        }

        viewModel.selectedSourceValue.observe(viewLifecycleOwner) { source ->
            Log.d("SourceFilter", "Selected source: $source")
        }

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                val menuItem = bottomNavigationView.menu.findItem(R.id.navigation_item_1)
                menuItem.isChecked = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshNews() {
        if (isInternetAvailable(requireContext())) {
            viewModel.refreshNews()
            swipeRefreshLayout.isRefreshing = false
        } else {
            showErrorDialog(getString(R.string.no_internet))
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun openLinkInCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        context?.let {
            customTabsIntent.launchUrl(it, Uri.parse(url))
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}