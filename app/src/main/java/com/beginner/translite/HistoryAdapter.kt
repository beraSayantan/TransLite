package com.beginner.translite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(var context: Context, var history: MutableList<TransQuery>)
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_cardview, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder,position: Int) {

        val tvInputText: TextView = holder.itemView.findViewById(R.id.tvInputText)
        val tvOutputText: TextView = holder.itemView.findViewById(R.id.tvOutputText)
        tvInputText.text = history[position].input
        tvOutputText.text = history[position].output

    }

    override fun getItemCount(): Int {
        return history.size
    }

}