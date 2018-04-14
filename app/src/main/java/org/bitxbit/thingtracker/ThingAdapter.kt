package org.bitxbit.thingtracker

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView

class ThingAdapter(private val num: Int) : RecyclerView.Adapter<ThingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(TextView(parent.context))


    override fun getItemCount(): Int = num

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = "Item ${position}"
    }

    class ViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)

}
