package org.bitxbit.thingtracker

import org.bitxbit.thingtracker.model.Dao
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class DataEntryPresenter(val view: DataEntryView) {
    private var dao: Dao = Dao()

    fun setDao(daoOverride: Dao) {
        dao = daoOverride
    }

    fun saveThing(itemId: Long, type: ThingType, name: String, value: String, itemCreationDate: Date?) {
        val thing: Thing = newThing(type, itemId, name, value, itemCreationDate)
        dao?.insertThing(thing, callback = {
            view.thingSaved();
        })
    }

    private fun newThing(type: ThingType, itemId: Long, name: String, value: String, itemCreationDate: Date?): Thing {
        val date = if (itemCreationDate != null) itemCreationDate else Date()
        val thing: Thing =
            when (type) {
                ThingType.BOOL ->
                    Thing(itemId, name, "", -1, value.toBoolean(), date, "BOOL")
                ThingType.INT ->
                    Thing(itemId, name, "", value.toInt(), false, date, "INT")
                else ->
                    Thing(itemId, name, value, -1, false, date, "STRING")
            }
        return thing
    }

    fun setInitialItemDetails(itemId: Long, itemName: String?, itemType: ThingType?) {
        if (itemId != 0L) {
            view.disableSaveButton()
            dao.findThing(itemId, {
                if (it != null) view.fillFormWithThingAsync(it)
            })
        } else if (itemName != null && itemType != null) {
            view.fillFormWithItemTypeAndName(itemName, itemType)
        }
    }
}