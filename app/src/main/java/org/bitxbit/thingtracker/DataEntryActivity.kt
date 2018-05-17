package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.*
import io.realm.Realm
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class DataEntryActivity : Activity() {

    companion object {
        val TAG: String = "ThingTracker"
        val ITEM_ID : String = "ITEM_ID"
        val ITEM_NAME : String = "ITEM_NAME"
        val ITEM_TYPE : String = "ITEM_TYPE"
    }

    private lateinit var realm : Realm
    private lateinit var editKeyName : EditText
    private lateinit var editKeyValue : EditText
    private lateinit var radioType : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_entry_activity)

        val itemId : Long? = intent?.extras?.getLong(ITEM_ID)
        var itemName : String? = null
        var itemType : ThingType? = null
        if (itemId == 0L) {
             itemName = intent?.extras?.getString(ITEM_NAME)
             itemType = intent?.extras?.getSerializable(ITEM_TYPE) as ThingType?
        }

        realm = Realm.getDefaultInstance()
        editKeyName = findViewById<EditText>(R.id.edit_key_name)
        editKeyValue = findViewById<EditText>(R.id.edit_key_val)
        radioType = findViewById<RadioGroup>(R.id.radiogroup_type)

        val managedThing: Thing? = if (itemId != 0L) fillFormWithThing(itemId) else null
        if (itemId == 0L && (itemName != null && itemType != null)) fillFormWithItemTypeAndName(itemName!!, itemType!!)

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener({
            val name = editKeyName.text.toString()
            val value = editKeyValue.text.toString()
            val checkedBtnId = radioType.checkedRadioButtonId

            if (managedThing != null) {
                updateThing(managedThing, name, checkedBtnId, value)
            } else {
                createNewThing(checkedBtnId, name, value)
            }

            Toast.makeText(it.context, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun createNewThing(checkedBtnId: Int, name: String, value: String) {
        val thing: Thing =
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
            if (thing.id == 0L) {
                val id: Number? = it.where(Thing::class.java).max("id")
                val nextId = if (id == null) 1 else (id.toLong() + 1)
                thing.id = nextId
            }
            it.copyToRealm(thing)
        }
    }

    private fun updateThing(savedThing: Thing, name: String, checkedBtnId: Int, value: String) {
        realm.executeTransaction {
            savedThing.name = name
            when (checkedBtnId) {
                R.id.radio_bool -> savedThing.boolVal = value.toBoolean()
                R.id.radio_int -> savedThing.intVal = value.toInt()
                R.id.radio_string -> savedThing.strVal = value
            }
        }
    }

    private fun fillFormWithThing(itemId: Long?) : Thing? {
        var record: Thing? = null

        realm.executeTransaction {
            record = it.where(Thing::class.java).equalTo("id", itemId).findFirst()
            editKeyName.setText(record?.name)
            var checkedBtnId : Int
            when (record?.type) {
                ThingType.BOOL -> {
                    editKeyValue.setText(record?.boolVal.toString())
                    checkedBtnId = R.id.radio_bool
                }
                ThingType.INT -> {
                    editKeyValue.setText(record?.intVal.toString())
                    checkedBtnId = R.id.radio_int
                }
                else -> {
                    editKeyValue.setText(record?.strVal)
                    checkedBtnId = R.id.radio_string
                }
            }
            radioType.check(checkedBtnId)
        }


        return record
    }

    private fun fillFormWithItemTypeAndName(itemName: String, itemType: ThingType) {
        editKeyName.setText(itemName)
        editKeyValue.setText("")

        var checkedBtnId : Int =
                when (itemType) {
                    ThingType.BOOL ->  R.id.radio_bool
                    ThingType.INT ->  R.id.radio_int
                    else ->  R.id.radio_string
                }
        radioType.check(checkedBtnId)
    }
}