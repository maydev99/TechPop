package com.bombadu.techpop

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bombadu.techpop.ui.MainActivity
import com.bombadu.techpop.ui.home.HomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.anyOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@ExperimentalCoroutinesApi
class AppNavigationTest {


    private lateinit var application: TechPopApplication

   @get:Rule
    var mainCoroutineRule = AndroidMainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun navigateFromHome_toSaved() {
       // val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.Theme_TechPop)

    }



}