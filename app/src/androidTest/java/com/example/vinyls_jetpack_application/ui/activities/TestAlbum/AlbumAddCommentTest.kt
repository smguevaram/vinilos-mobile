package com.example.vinyls_jetpack_application.ui.activities.TestAlbum

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.ui.AlbumAddCommentFragment
import com.example.vinyls_jetpack_application.ui.AlbumAddCommentFragmentDirections
import com.example.vinyls_jetpack_application.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AlbumAddCommentTest {

    private lateinit var fragment: AlbumAddCommentFragment

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val bundle = Bundle()
        bundle.putInt("albumId", 1)
        bundle.putString("albumName", "Album Name")

        val scenario = activityScenarioRule.scenario

        print(scenario)
    }

    @Test
    fun testAddCommentButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btnAddComment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testDescriptionEditTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.description))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testRatingSpinnerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.spinnerRating))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddCommentClickSimulation() {
        Espresso.onView(ViewMatchers.withId(R.id.btnAddComment))
            .perform(ViewActions.click())

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val action = AlbumAddCommentFragmentDirections.actionAlbumCommentFragmentToAlbumDetailFragment(1)
            print(action)
        }, 2000)
    }


}

