package org.bitxbit.thingtracker

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import org.bitxbit.thingtracker.model.Thing

class ThingAdapter(private val things: List<Thing>) : RecyclerView.Adapter<ThingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(TextView(parent.context))


    override fun getItemCount(): Int = things.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = "Item ${things[position].strVal}"
    }

    class ViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)

}
