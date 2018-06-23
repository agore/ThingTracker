package org.bitxbit.thingtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.realm.Realm
import io.realm.Sort
import org.bitxbit.thingtracker.adapter.ThingRealmAdapter
import org.bitxbit.thingtracker.model.Thing

class MainActivity : Activity() {

    private lateinit var realm: Realm
    private lateinit var adapter : ThingRealmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val thingRecycler = findViewById(R.id.recycler_things) as RecyclerView
        thingRecycler.layoutManager = LinearLayoutManager(this)


        realm = Realm.getDefaultInstance()


        adapter = ThingRealmAdapter(this, realm.where(Thing::class.java).distinct("name").findAll().sort("date", Sort.DESCENDING))

        thingRecycler.adapter = adapter
        thingRecycler.addItemDecoration(SpaceItemDecoration())

//        val swipeHandler = object : SwipeToDeleteCallback(this.applicationContext) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
//                val adapter = thingRecycler.adapter as ThingRealmAdapter
//                adapter.removeAt(viewHolder!!.adapterPosition)
//            }
//        }
//
//        val itemTouchHelper = ItemTouchHelper(swipeHandler)
//        itemTouchHelper.attachToRecyclerView(thingRecycler)


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, DataEntryActivity::class.java)
            startActivity(intent)
        }
    }

}