package com.beginner.translite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginner.translite.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var rvHistory:RecyclerView
    private lateinit var currContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentHistoryBinding>(inflater
            ,R.layout.fragment_history,container,false)

        (activity as AppCompatActivity).supportActionBar?.title = "Recent"

        binding.rvHistory.adapter = HistoryAdapter(currContext, Database.history)

        binding.rvHistory.layoutManager = LinearLayoutManager(currContext)

        return binding.root
    }

    override fun onAttach(context: Context) {
        currContext = context
        super.onAttach(context)
    }
}