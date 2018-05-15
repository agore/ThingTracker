package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.realm.Realm
import org.bitxbit.thingtracker.model.Thing

class ThingDetailActivity : Activity() {

    private lateinit var adapter : ThingDetailRealmAdapter
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thing_detail_activity)

        val recycler = findViewById<RecyclerView>(R.id.recycler_thing_details)
        recycler.layoutManager = LinearLayoutManager(this)
        realm = Realm.getDefaultInstance()

        val thingName = intent?.getStringExtra("thingName")
        if (thingName != null) {
            adapter = ThingDetailRealmAdapter(realm.where(Thing::class.java).beginsWith("name", thingName).findAll())
            recycler.adapter = adapter
        }

    }
}