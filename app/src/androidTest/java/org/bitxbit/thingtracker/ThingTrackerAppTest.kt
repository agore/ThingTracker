package org.bitxbit.thingtracker

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import java.util.*

class ThingTrackerAppTest : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

//        val config = RealmConfiguration.Builder().
//                name("test.schema").
//                build();
//        Realm.setDefaultConfiguration(config);

        Realm.getDefaultInstance().close()
        Realm.deleteRealm(Realm.getDefaultConfiguration())
        val realm = Realm.getDefaultInstance()
        createThings(1000, realm)
    }

    private fun createThings(howMany: Int, realm: Realm) {
        val random = Random()
        val thingTypeVals = ThingType.values()

        for (i in 0..howMany) {
            val type = thingTypeVals[random.nextInt(3)]
            val thing =
                    when (type) {
                        ThingType.BOOL -> Thing(i.toLong(), "Thing ${i}", "", -1, random.nextBoolean(), Date(), ThingType.BOOL.name)
                        ThingType.INT -> Thing(i.toLong(), "Thing ${i}", "", i, false, Date(), ThingType.INT.name)
                        else -> Thing(i.toLong(), "Thing ${i}", "String ${i}", -1, false, Date(), ThingType.STRING.name)
                    }
            realm.executeTransaction {
                it.copyToRealm(thing)
            }
        }
    }

}
