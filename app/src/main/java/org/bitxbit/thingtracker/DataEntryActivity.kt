package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import org.bitxbit.thingtracker.model.Dao
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class DataEntryActivity() : Activity() {

    companion object {
        val TAG: String = "ThingTracker"
        val ITEM_ID : String = "ITEM_ID"
        val ITEM_NAME : String = "ITEM_NAME"
        val ITEM_TYPE : String = "ITEM_TYPE"
//        val ITEM_VAL : String = "ITEM_VAL"
    }

    private lateinit var editKeyName : EditText
    private lateinit var editKeyValue : EditText
    private lateinit var radioType : RadioGroup
    private var itemId: Long = 0L
    private lateinit var dao: Dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_entry_activity)

        dao = Dao()

        var id = intent?.extras?.getLong(ITEM_ID)
        if (id != null) itemId = id!!

        var itemName : String? = ""
        var itemType : ThingType? = null
        var itemVal : String? = ""
        if (itemId == 0L) {
            val bundle = /*if (savedInstanceState == null) intent?.extras else savedInstanceState*/ intent?.extras
            itemName = bundle?.getString(ITEM_NAME)
            itemType = bundle?.getSerializable(ITEM_TYPE) as ThingType?
//            itemVal = bundle?.getString(ITEM_VAL)
        }

        editKeyName = findViewById(R.id.edit_key_name)
        editKeyValue = findViewById(R.id.edit_key_val)
        radioType = findViewById(R.id.radiogroup_type)

        val btnSave = findViewById<Button>(R.id.btn_save)

        if (itemId != 0L) {
            btnSave.setEnabled(false)
            fillFormWithThing(itemId, btnSave)
        } else if (itemName != null && itemType != null) {
            fillFormWithItemTypeAndName(itemName!!, itemVal!!, itemType!!)
        }


        btnSave.setOnClickListener({
            createNewThing(itemId, radioType.checkedRadioButtonId, editKeyName.text.toString(), editKeyValue.text.toString())
            Toast.makeText(it.context, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//        val name = editKeyName.text?.toString()
//        val value = editKeyValue.text?.toString()
//        outState?.putString(ITEM_NAME, if (name.isNullOrEmpty()) "" else name)
//        outState?.putString(ITEM_VAL, if (value.isNullOrEmpty()) "" else value)
//    }

    private fun createNewThing(itemId: Long, checkedBtnId: Int, name: String, value: String) {
        val thing: Thing =
                when (checkedBtnId) {
                    R.id.radio_bool ->
                        Thing(itemId, name, "", -1, value.toBoolean(), Date(), "BOOL")
                    R.id.radio_int ->
                        Thing(itemId, name, "", value.toInt(), false, Date(), "INT")
                    else ->
                        Thing(itemId, name, value, -1, false, Date(), "STRING")
                }

        Log.i(TAG, thing.toString())

        dao?.insertThing(thing)
    }

    private fun updateThing(savedThing: Thing, name: String, checkedBtnId: Int, value: String) {
        val type =
           when(checkedBtnId) {
               R.id.radio_bool -> ThingType.BOOL
               R.id.radio_int -> ThingType.INT
               else -> ThingType.STRING
           }

        dao?.updateThing(savedThing, name, type, value)
    }

    private fun fillFormWithThing(itemId: Long?, btnSave: Button) {
        if (itemId == null) return

        val finderCallback = fun(record: Thing?) {
            if (record != null) {
                runOnUiThread {
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
                    btnSave.setEnabled(true)
                }
            }
        }

        dao?.findThing(itemId, finderCallback);

    }

    private fun fillFormWithItemTypeAndName(itemName: String, itemVal: String, itemType: ThingType) {
        editKeyName.setText(itemName)
        editKeyValue.setText(itemVal)

        var checkedBtnId : Int =
                when (itemType) {
                    ThingType.BOOL ->  R.id.radio_bool
                    ThingType.INT ->  R.id.radio_int
                    else ->  R.id.radio_string
                }
        radioType.check(checkedBtnId)
    }
}