package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val thingRecycler = findViewById(R.id.recycler_things) as RecyclerView
        thingRecycler.layoutManager = LinearLayoutManager(this)
        thingRecycler.adapter = ThingAdapter(100)

    }

}