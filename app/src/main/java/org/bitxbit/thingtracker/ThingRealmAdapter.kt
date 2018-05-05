package org.bitxbit.thingtracker

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.model.Thing

class ThingRealmAdapter(data: OrderedRealmCollection<Thing>): RealmRecyclerViewAdapter<Thing, ThingViewHolder>(data, true) {
    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingViewHolder = ThingViewHolder(TextView(parent.context))

    override fun onBindViewHolder(holder: ThingViewHolder, position: Int) {
        val t = getItem(position)
        holder.tv.text = t.toString()
    }

    override fun getItemId(position: Int): Long = getItem(position)!!.id

    companion object {
        fun createFakes(howMany: Int, realm: Realm) {
            for (i in 0..howMany) {
                realm.executeTransaction {
                    it.createObject<Thing>(i)
                }

            }
        }
    }

}

class ThingViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)