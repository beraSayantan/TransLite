package com.beginner.translite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.beginner.translite.databinding.FragmentAboutBinding
import com.beginner.translite.databinding.FragmentTransBinding

class AboutFragment : Fragment() {

    private lateinit var currContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val shashankCard = view?.findViewById<CardView>(R.id.cvShashank)
        var binding = DataBindingUtil.inflate<FragmentAboutBinding>(inflater
            ,R.layout.fragment_about,container,false)

        binding.cvShashank.setOnClickListener{
            Toast.makeText(currContext,"Share",Toast.LENGTH_LONG).show()
        }

        binding.civShashank.setOnClickListener {
            Toast.makeText(currContext,"Share",Toast.LENGTH_LONG).show()
        }

        (activity as AppCompatActivity).supportActionBar?.title = "About Us"
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onAttach(context: Context) {

        currContext=context
        super.onAttach(context)
    }

}