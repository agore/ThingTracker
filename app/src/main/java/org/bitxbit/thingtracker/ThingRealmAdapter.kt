package org.bitxbit.thingtracker

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.model.Thing
import java.text.SimpleDateFormat
import java.util.*

class ThingRealmAdapter(act: MainActivity, data: OrderedRealmCollection<Thing>, autoUpdate: Boolean = true):
        RealmRecyclerViewAdapter<Thing, ThingViewHolder>(data, autoUpdate) {
    var activity : MainActivity = act

    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingViewHolder = ThingViewHolder(TextView(parent.context))

    override fun onBindViewHolder(holder: ThingViewHolder, position: Int) {
        val t: Thing? = getItem(position)
        holder.tv.text = "${formatDate(t?.date)} [${t?.type}] ${t?.name}"
        holder.itemView.setOnClickListener {
            val intent = Intent(activity.applicationContext, ThingDetailActivity::class.java)
            intent.putExtra("thingName", t?.name)
            activity.startActivity(intent)
        }
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

        fun formatDate(d: Date?) = SimpleDateFormat("M/d/yyyy").format(d)
    }

}

class ThingViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)