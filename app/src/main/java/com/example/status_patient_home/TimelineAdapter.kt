package com.example.status_patient_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimelineAdapter(private val careHistoryDataList : ArrayList<CareHistoryData>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TimelineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val currentItem = careHistoryDataList[position]
        holder.title.text = currentItem.heading
        holder.date.text = currentItem.dateText
        holder.description.text = currentItem.descripText
    }

    override fun getItemCount(): Int {
        return careHistoryDataList.size
    }

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title : TextView = itemView.findViewById(R.id.eventTitle)
        val date : TextView = itemView.findViewById(R.id.eventDate)
        val description : TextView = itemView.findViewById(R.id.eventDescription)

    }
}