package org.bitxbit.thingtracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.model.Thing
import java.text.DateFormat
import java.text.SimpleDateFormat

class ThingDetailRealmAdapter(data: OrderedRealmCollection<Thing>):
        RealmRecyclerViewAdapter<Thing, ThingDetailViewHolder>(data, false) {
    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingDetailViewHolder =
            ThingDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.thing_detail_list_item, parent, false))

    override fun onBindViewHolder(holder: ThingDetailViewHolder, position: Int) {
        val t = getItem(position)
        val fmt : SimpleDateFormat = SimpleDateFormat("M/d/Y hh:mm:ss")
        holder.tvDate.text = fmt.format(t?.date)
        holder.tvVal.text = t?.strVal
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
    var tvDate : TextView = cell.findViewById<TextView>(R.id.txt_date)
    var tvVal : TextView = cell.findViewById<TextView>(R.id.txt_val)
}