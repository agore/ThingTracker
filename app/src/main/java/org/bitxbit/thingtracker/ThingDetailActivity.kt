package org.bitxbit.thingtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.realm.Realm
import io.realm.Sort
import org.bitxbit.thingtracker.adapter.ThingDetailRealmAdapter
import org.bitxbit.thingtracker.model.Thing

class ThingDetailActivity : Activity() {

    private lateinit var adapter : ThingDetailRealmAdapter
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thing_detail_activity)
        val fabAddDetail = findViewById<FloatingActionButton>(R.id.fab_add_detail)
        val recycler = findViewById<RecyclerView>(R.id.recycler_thing_details)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(SpaceItemDecoration())
        realm = Realm.getDefaultInstance()

        val thingName = intent?.getStringExtra("thingName")
        if (thingName != null) {
            val results = realm.where(Thing::class.java).beginsWith("name", thingName)
                    .findAll().sort("date", Sort.DESCENDING)
            adapter = ThingDetailRealmAdapter(this, results)
            recycler.adapter = adapter

            fabAddDetail.setOnClickListener {
                val t = results.first()
                val intent = Intent(this, DataEntryActivity::class.java)
                intent.putExtra(DataEntryActivity.ITEM_NAME, t?.name)
                intent.putExtra(DataEntryActivity.ITEM_TYPE, t?.type)
                startActivity(intent)
            }
        }
    }
}