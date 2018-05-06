package org.bitxbit.thingtracker

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.realm.Realm
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class MainActivity : Activity() {

    private lateinit var realm: Realm
    private lateinit var adapter : ThingRealmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val thingRecycler = findViewById(R.id.recycler_things) as RecyclerView
        thingRecycler.layoutManager = LinearLayoutManager(this)
//        adapter = ThingAdapter(createThings(100))

        realm = Realm.getDefaultInstance()

//        ThingRealmAdapter.createFakes(10000, realm)
        adapter = ThingRealmAdapter(realm.where(Thing::class.java).findAll())

        thingRecycler.adapter = adapter
        thingRecycler.addItemDecoration(SpaceItemDecoration())


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, DataEntryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createThings(howMany: Int) : List<Thing> {
        val strings = listOf("A", "B", "C", "D")
        val rand = Random()
        val things = MutableList<Thing>(howMany) {
            Thing()
        }

        return things
    }


    class SpaceItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            outRect.set(0, 0, 0, 24)
        }
    }
}