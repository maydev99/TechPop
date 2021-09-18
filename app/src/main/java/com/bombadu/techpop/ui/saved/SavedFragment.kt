package com.bombadu.techpop.ui.saved

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.techpop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment(), SavedAdapter2.ItemClickListener {

    private val viewModel: SavedViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedAdapter: SavedAdapter2
    private lateinit var dialog: TextView
    private var isLandscape = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        recyclerView = view.findViewById(R.id.saved_recycler_view)
        savedAdapter = SavedAdapter2(this)
        dialog = view.findViewById(R.id.no_articles_dialog)

        setupRecyclerView()
        observeNewsArticles()

    }


    private fun setupRecyclerView() = recyclerView.apply {
        adapter = savedAdapter
        hasFixedSize()
        layoutManager = if(isLandscape) {
            GridLayoutManager(this.context, 2)
        } else {
            GridLayoutManager(this.context, 1)
        }
       // layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object  :
        ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                savedAdapter.getItemAt(viewHolder.adapterPosition)
                    .let {
                        viewModel.deleteSavedItem(it) }
                Toast.makeText(requireContext(), "Article Deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(recyclerView)

    }

    private fun observeNewsArticles() {
        viewModel.savedArticles.observe(viewLifecycleOwner, { saved ->
            saved.let {
                if (it.isNullOrEmpty()) {
                    dialog.visibility = View.VISIBLE
                } else {
                    dialog.visibility = View.INVISIBLE
                }
            }
            savedAdapter.submitList(saved)
            recyclerView.scrollToPosition(0)


        })


    }

    override fun onItemClick(position: Int) {
        val item = savedAdapter.getItemAt(position)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(item.webUrl)
        startActivity(intent)

    }


}