package com.example.vinyls_jetpack_application.ui

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls_jetpack_application.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumAddTest {

    private lateinit var fragment: AlbumAddFragment

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val scenario = activityScenarioRule.toString()
        print(scenario)
    }

    @Test
    fun testAlbumNameEditTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextAlbumName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAlbumCoverEditTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextAlbumCover))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testReleaseDateEditTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextReleaseDate))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testDescriptionEditTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextDescription))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testGenreSpinnerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.spinnerGenre))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testRecordLabelSpinnerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.spinnerRecordLabel))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddAlbumButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btnAddAlbum))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAddAlbumClickSimulation() {
        Espresso.onView(ViewMatchers.withId(R.id.btnAddAlbum))
            .perform(ViewActions.click())

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            // Navega al fragmento deseado después del retraso
            print("Unavenged al fragment desperado")
        }, 2000)
    }
}
