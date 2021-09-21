package com.bombadu.techpop.ui.authentication


import androidx.test.espresso.DataInteraction
import androidx.test.espresso.ViewInteraction
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*

import com.bombadu.techpop.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthenticationActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(AuthenticationActivity::class.java)

    @Test
    fun authenticationActivityTest() {
        val gf = onView(
            allOf(
                withText("Sign in"),
                childAtPosition(
                    allOf(
                        withId(R.id.signInButton),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.motion.widget.MotionLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        gf.perform(click())

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
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
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
        bottomNavigationItemView2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
