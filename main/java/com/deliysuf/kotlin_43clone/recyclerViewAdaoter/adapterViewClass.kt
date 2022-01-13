package com.deliysuf.kotlin_43clone.recyclerViewAdaoter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deliysuf.kotlin_43clone.databinding.RecyclerRowBinding
import com.deliysuf.kotlin_43clone.model.Model
import com.squareup.picasso.Picasso

class adapterViewClass(private var recycletList:ArrayList<Model>): RecyclerView.Adapter<adapterViewClass.viewHolder>() {
    class viewHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
       val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
   holder.binding.recyclerCommentText.text=recycletList.get(position).Comment.toString()
   holder.binding.ryclerEmailText.text=recycletList.get(position).email.toString()
   Picasso.get().load(recycletList.get(position).image).into(holder.binding.recyclerImage)
    }

    override fun getItemCount(): Int {
       return recycletList.size
    }

}