package org.bitxbit.thingtracker.model

import io.realm.Realm
import org.bitxbit.thingtracker.R

class Dao {
    fun insertThing(thing: Thing, callback: () -> Unit) {
        val realm = Realm.getDefaultInstance()
        try {
            realm.executeTransactionAsync(Realm.Transaction() {
                if (thing.id == 0L) {
                    val id: Number? = it.where(Thing::class.java).max("id")
                    val nextId = if (id == null) 1 else (id.toLong() + 1)
                    thing.id = nextId
                }
                it.copyToRealmOrUpdate(thing)
            }, Realm.Transaction.OnSuccess() {
                callback()
            })
        } finally {
            realm.close()
        }
    }


    fun findThing(itemId: Long?, finderCallback: ((Thing?) -> Unit)) {
        val realm = Realm.getDefaultInstance()
        var record: Thing? = null
        try {
            realm.executeTransactionAsync(Realm.Transaction() {
                val temp = it.where(Thing::class.java).equalTo("id", itemId).findFirst()
                record = it.copyFromRealm(temp)
            }, Realm.Transaction.OnSuccess {
                finderCallback(record)
            }, Realm.Transaction.OnError {
                finderCallback(null)
            })
        } finally {
            realm.close()
        }
    }
}