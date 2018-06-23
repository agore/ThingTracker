package org.bitxbit.thingtracker

import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType

interface DataEntryView {
    fun thingSaved()
    fun disableSaveButton()
    fun fillFormWithThingAsync(record: Thing)
    fun fillFormWithItemTypeAndName(itemName: String, itemType: ThingType)
}