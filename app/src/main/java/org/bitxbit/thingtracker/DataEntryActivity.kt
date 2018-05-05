package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*
import java.util.logging.Logger

class DataEntryActivity : Activity() {

    companion object {
        val TAG: String = "ThingTracker"
    }

    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_entry_activity)

        realm = Realm.getDefaultInstance()

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener({
            val name = findViewById<EditText>(R.id.edit_key_name).text.toString()
            val value = findViewById<EditText>(R.id.edit_key_val).text.toString()
            val checkedBtnId = findViewById<RadioGroup>(R.id.radiogroup_type).checkedRadioButtonId
            val thing =
                when (checkedBtnId) {
                    R.id.radio_bool ->
                        Thing(0, name, "", -1, value.toBoolean(), Date(), "BOOL")
                    R.id.radio_int ->
                        Thing(0, name, "", value.toInt(), false, Date(), "INT")
                    else ->
                        Thing(0, name, value, -1, false, Date(), "STRING")
                }

            Log.i(TAG, thing.toString())

            realm.executeTransaction {
                val id: Number? = it.where(Thing::class.java).max("id")
                val nextId = if (id == null) 0 else (id.toLong() + 1)
                thing.id = nextId
                it.copyToRealm(thing)
            }

            Toast.makeText(it.context, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}