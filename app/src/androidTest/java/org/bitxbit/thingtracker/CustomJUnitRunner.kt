package org.bitxbit.thingtracker

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class CustomJUnitRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, ThingTrackerAppTest::class.java.canonicalName, context)
    }
}