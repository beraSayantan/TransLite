package com.beginner.translite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.beginner.translite.databinding.FragmentHistoryBinding
import com.beginner.translite.databinding.FragmentTransBinding

class TransFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentTransBinding>(inflater
            ,R.layout.fragment_trans,container,false)
        return binding.root
    }


}