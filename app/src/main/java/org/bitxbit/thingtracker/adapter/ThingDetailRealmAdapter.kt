package org.bitxbit.thingtracker.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.DataEntryActivity
import org.bitxbit.thingtracker.R
import org.bitxbit.thingtracker.ThingDetailActivity
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.text.SimpleDateFormat


class ThingDetailRealmAdapter(val activity: ThingDetailActivity, data: OrderedRealmCollection<Thing>):
        RealmRecyclerViewAdapter<Thing, ThingDetailViewHolder>(data, true) {

    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingDetailViewHolder =
            ThingDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.thing_detail_list_item, parent, false))

    override fun onBindViewHolder(holder: ThingDetailViewHolder, position: Int) {
        val t = getItem(position)
        holder.bind(t!!, activity)
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

class ThingDetailViewHolder(val cell: View) : RecyclerView.ViewHolder(cell) {
    val fmt = SimpleDateFormat("M/d/Y hh:mm:ss")
    fun bind(t: Thing, act: Activity) {
        itemView.findViewById<TextView>(R.id.txt_date).text = fmt.format(t.date)
        itemView.findViewById<TextView>(R.id.txt_val).text =
            when (t.type) {
                ThingType.INT -> t.intVal.toString()
                ThingType.BOOL -> t.boolVal.toString()
                else -> t.strVal
            }

        itemView.findViewById<ImageButton>(R.id.btn_save).setOnClickListener {
            val intent = Intent(act,  DataEntryActivity::class.java)
            intent.putExtra(DataEntryActivity.ITEM_ID, t.id)
            act.startActivity(intent)
        }
    }
}