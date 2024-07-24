package com.example.graph

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TopLinksFragment : Fragment() {

    private lateinit var viewModel: LinksViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_top_links2, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewTopLinks)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = AppInfoAdapter(requireContext(),arrayListOf())
        recyclerView.adapter = adapter
        viewModel = ViewModelProvider(requireActivity()).get(LinksViewModel::class.java)

        viewModel.topLinks.observe(viewLifecycleOwner) { topLinks ->
            val dates = topLinks.map { it.date1 }
            dates.forEach { date ->
                Log.d("TopLinksFragment", "Date: $date")
            }

            adapter.updateData(topLinks)
        }
        return view
    }


}