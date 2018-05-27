package org.bitxbit.thingtracker.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.MainActivity
import org.bitxbit.thingtracker.R
import org.bitxbit.thingtracker.ThingDetailActivity
import org.bitxbit.thingtracker.model.Thing
import java.text.SimpleDateFormat
import java.util.*

class ThingRealmAdapter(act: MainActivity, data: OrderedRealmCollection<Thing>, autoUpdate: Boolean = true):
        RealmRecyclerViewAdapter<Thing, ThingViewHolder>(data, autoUpdate) {
    var activity : MainActivity = act

    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingViewHolder =
            ThingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.things_list, parent, false))

    override fun onBindViewHolder(holder: ThingViewHolder, position: Int) {
        val t: Thing? = getItem(position)
        holder.bind(t!!)
        holder.itemView.setOnClickListener {
            val intent = Intent(activity.applicationContext, ThingDetailActivity::class.java)
            intent.putExtra("thingName", t?.name)
            activity.startActivity(intent)
        }
    }

    override fun getItemId(position: Int): Long = getItem(position)!!.id

//    fun removeAt(adapterPosition: Int) {
//        Realm.getDefaultInstance().executeTransaction {
//            data?.deleteFromRealm(adapterPosition)
//        }
//
//        notifyItemRemoved(adapterPosition)
//    }

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

class ThingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvType : TextView = itemView.findViewById<TextView>(R.id.txt_type);
    private val tvName : TextView = itemView.findViewById<TextView>(R.id.txt_name);

    fun bind(t: Thing) {
        tvType.text = t.typeStr
        tvName.text = t.name
    }
}