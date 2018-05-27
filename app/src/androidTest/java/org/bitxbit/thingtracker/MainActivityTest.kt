package org.bitxbit.thingtracker

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.realm.Realm
import io.realm.RealmConfiguration
import org.bitxbit.thingtracker.adapter.ThingViewHolder
import org.bitxbit.thingtracker.model.Thing
import org.bitxbit.thingtracker.model.ThingType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import android.support.v7.widget.RecyclerView
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.annotation.NonNull
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    val activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

//    @Test
    fun testLaunch() {
        onView(withId(R.id.recycler_things)).check(matches(isDisplayed()))
    }

//    @Test
    fun testCreateNew() {

        val config = RealmConfiguration.Builder().inMemory().name("test.realm").build()
        Realm.setDefaultConfiguration(config)
        Realm.getDefaultInstance().close()
        Realm.deleteRealm(config)

        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.edit_key_name)).check(matches(isDisplayed())).perform(typeText("testKey"))
        onView(withId(R.id.edit_key_val)).check(matches(isDisplayed())).perform(typeText("testValue"))
        onView(withId(R.id.radio_string)).perform(click())
        onView(withId(R.id.btn_save)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.recycler_things)).check(matches(isDisplayed()))

        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val things = it.where(Thing::class.java).findAll()
            assert(things.size == 1)
            val thing : Thing? = things[0]
            assert(thing?.name == "testKey")
            assert(thing?.strVal == "testVal")
        }

        realm.close()
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Test
    fun testCreateMany() {
//        val config = RealmConfiguration.Builder().inMemory().name("test.realm").build()
//        Realm.setDefaultConfiguration(config)
//        Realm.deleteRealm(config)


        val realm = Realm.getDefaultInstance()
        val recyclerViewMatcher = RecyclerViewMatcher(R.id.recycler_things)
        onView(withRecyclerView(R.id.recycler_things).atPosition(4)).check(matches(withText("Thing 0")))
//        createThings(1000, realm)

//        onView(withId(R.id.recycler_things)).check(matches(isDisplayed()))
//        onView(withId(R.id.fab)).perform(click())
//        onView(withId(R.id.edit_key_val)).check(matches(isDisplayed()))
//        pressBack()
//        onView(withId(R.id.recycler_things)).check(matches(atPosition(1000, withText("Thing 0"))))
        realm.close()
    }



    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
 }