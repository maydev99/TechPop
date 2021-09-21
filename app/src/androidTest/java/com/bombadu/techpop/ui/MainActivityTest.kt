package com.bombadu.techpop.ui

import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.bombadu.techpop.R
import com.bombadu.techpop.ui.home.HomeAdapter
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun isActivityInView() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnRecyclerViewItem() {



        onView(withId(R.id.home_recycler_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.home_recycler_view))
            .perform(actionOnItemAtPosition<HomeAdapter.ItemViewHolder>(0, click()))

        pressBack()
    }

    @Test
    fun navigateUsingBottomNavigationBar() {

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())




    }


/*
val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.savedFragment), withContentDescription("Saved"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nav),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
 */

}