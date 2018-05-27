package org.bitxbit.thingtracker.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

enum class ThingType {
    STRING,
    INT,
    BOOL,
    NONE
}

open class Thing(
        @PrimaryKey var id: Long = 0,
        var name: String = "",
        var strVal: String = "",
        var intVal: Int = -1,
        var boolVal: Boolean = false,
        var date: Date? = null,
        var typeStr: String = "NONE"

) : RealmObject() {

    var type : ThingType
        get() = ThingType.valueOf(typeStr)
        set(value) {
            typeStr = value.name
        }


    override fun toString(): String {
        val value =
            when (type) {
                ThingType.STRING -> strVal
                ThingType.INT -> intVal
                ThingType.BOOL -> boolVal
                else -> "none"
            }
        return "${name} [${typeStr}] ${value}"
    }
}

