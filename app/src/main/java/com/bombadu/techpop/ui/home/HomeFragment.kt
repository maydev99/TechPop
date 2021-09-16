package com.bombadu.techpop.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.techpop.R
import com.bombadu.techpop.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private var isLandscape = false
    private var lastPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        homeAdapter = HomeAdapter()
        recyclerView = view.findViewById(R.id.home_recycler_view)

        setupRecyclerView()
        observeNewsArticles()
        setHasOptionsMenu(true)

    }

    private fun setupRecyclerView() = recyclerView.apply {
        adapter = homeAdapter
        hasFixedSize()
        layoutManager = if(isLandscape) {
            GridLayoutManager(this.context, 2)
        } else {
            GridLayoutManager(this.context, 1)
        }
    }

    private fun observeNewsArticles() {
        viewModel.newsArticles.observe(viewLifecycleOwner, { news ->
            lastPosition = news.size - 1
            if(news.isNullOrEmpty()) {
                viewModel.getNewsFromFirebase()
            }
            homeAdapter.submitList(news)
            recyclerView.scrollToPosition(0)

        })


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.about -> {
                Utils.showAboutDialog(requireContext(),"v2.0","9.15.2021")
            }

            R.id.scroll_to_top -> {
                recyclerView.smoothScrollToPosition(0)
            }

            R.id.scroll_to_bottom -> {
                recyclerView.smoothScrollToPosition(lastPosition)
            }

        }

        return super.onOptionsItemSelected(item)
    }

}