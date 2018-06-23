package org.bitxbit.thingtracker

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import org.bitxbit.thingtracker.model.Dao
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class DataEntryActivity() : Activity(), DataEntryView {

    companion object {
        val TAG: String = "ThingTracker"
        val ITEM_ID : String = "ITEM_ID"
        val ITEM_NAME : String = "ITEM_NAME"
        val ITEM_TYPE : String = "ITEM_TYPE"
        val ITEM_CREATE_DATE: String = "ITEM_CREATE_DATE"
//        val ITEM_VAL : String = "ITEM_VAL"
        val RADIOBTN_TO_THINGTYPE = HashMap<Int, ThingType>(3)
        init {
            RADIOBTN_TO_THINGTYPE.put(R.id.radio_string, ThingType.STRING)
            RADIOBTN_TO_THINGTYPE.put(R.id.radio_int, ThingType.INT)
            RADIOBTN_TO_THINGTYPE.put(R.id.radio_bool, ThingType.BOOL)
        }
    }

    private lateinit var editKeyName : EditText
    private lateinit var editKeyValue : EditText
    private lateinit var radioType : RadioGroup
    private lateinit var btnSave: Button
    private var itemId: Long = 0L
    private var itemCreationDate: Date? = null
    private lateinit var dao: Dao
    private lateinit var presenter: DataEntryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_entry_activity)

        presenter = DataEntryPresenter(this)

        dao = Dao()

        var id = intent?.extras?.getLong(ITEM_ID)
        if (id != null) {
            itemId = id!!
            itemCreationDate = intent?.extras?.getSerializable(ITEM_CREATE_DATE) as Date?
        }

        var itemName : String? = ""
        var itemType : ThingType? = null
        if (itemId == 0L) {
            val bundle = intent?.extras
            itemName = bundle?.getString(ITEM_NAME)
            itemType = bundle?.getSerializable(ITEM_TYPE) as ThingType?
        }

        editKeyName = findViewById(R.id.edit_key_name)
        editKeyValue = findViewById(R.id.edit_key_val)
        radioType = findViewById(R.id.radiogroup_type)
        btnSave = findViewById(R.id.btn_save)

        presenter.setInitialItemDetails(itemId, itemName, itemType)

        btnSave.setOnClickListener({
            presenter.saveThing(itemId,
                    RADIOBTN_TO_THINGTYPE[radioType.checkedRadioButtonId]!!,
                    editKeyName.text.toString(), editKeyValue.text.toString(), itemCreationDate)
        })
    }

    override fun fillFormWithThingAsync(record: Thing) {
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

    override fun thingSaved() {
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun fillFormWithItemTypeAndName(itemName: String, itemType: ThingType) {
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

    override fun disableSaveButton() {
        btnSave.setEnabled(false)
    }
}