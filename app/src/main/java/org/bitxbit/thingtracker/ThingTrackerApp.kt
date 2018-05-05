package org.bitxbit.thingtracker

import android.app.Application
import io.realm.Realm

class ThingTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}