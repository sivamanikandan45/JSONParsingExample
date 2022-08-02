package com.example.jsonparsingexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val list:List<University>): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val universityName:TextView
        val universityWeb:TextView
        init{
            universityName=view.findViewById(R.id.university_name)
            universityWeb=view.findViewById(R.id.web_page)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.universityWeb.text=list[position].web
        holder.universityName.text=list[position].name
    }

    override fun getItemCount(): Int {
        return list.size
    }
}