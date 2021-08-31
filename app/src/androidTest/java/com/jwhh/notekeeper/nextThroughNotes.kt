package com.jwhh.notekeeper
import android.provider.ContactsContract
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.After
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule.*

import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions.matches

@RunWith(AndroidJUnit4::class)

class nextThroughNotes {
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun nextThroughNotes(){

        onData(allOf(instanceOf(NoteInfo::class.java), equalTo(DataManager.notes[0]))).perform(click())

        for (index in 0..DataManager.notes.lastIndex){
            val note = DataManager.notes[index]

            onView(withId(R.id.spinnerCourses)).check(matches(withSpinnerText(note.course?.title)))
            onView(withId(R.id.textNoteTitle)).check(matches(withText(note.title)))
            onView(withId(R.id.textNoteText)).check(matches(withText(note.text)))

            if ( index != DataManager.notes.lastIndex )
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click())

        }
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())))


    }
}