package com.example.vinyls_jetpack_application.ui.activities.TestArtist

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
class ArtistDetailTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testArtistDetailFragmentDisplayed() {

        Espresso.onView(ViewMatchers.withId(R.id.artistFragment))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.artistDetailFragment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.testArtistDetailRv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.testText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}