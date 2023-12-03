package com.example.vinyls_jetpack_application.ui.activities.TestAlbum

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls_jetpack_application.ui.MainActivity
import com.example.vinyls_jetpack_application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumDetailTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAlbumDetailFragmentDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.albumFragment))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.albumDetailFragment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.albumDetailFragment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.testText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}