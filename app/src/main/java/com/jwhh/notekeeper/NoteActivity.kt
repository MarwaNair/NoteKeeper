package com.jwhh.notekeeper

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class NoteActivity : AppCompatActivity() {

    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapterCourses = ArrayAdapter<CourseInfo>(this,android.R.layout.simple_spinner_item,
                DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCourses.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
                intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET)
        {
            displayNote()
        } else {

            createNewNote()
        }

        Log.d(tag,"OnCreate")
    }

    private fun createNewNote() {
        DataManager.notes.add(NoteInfo())
        notePosition = DataManager.notes.lastIndex
    }

    private fun displayNote() {
        if (notePosition > DataManager.notes.lastIndex) {
            showMessage("No more notes")
            Log.e(tag,"Invalid note position $notePosition , max valid position ${DataManager.notes.lastIndex}")
            return

        }
        Log.i(tag,"Displaying note for position $notePosition")
        val note = DataManager.notes[notePosition]
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {

                if (notePosition < DataManager.notes.lastIndex) {
                    moveNext()
                }else{
                    val message = "No more notes"
                    showMessage(message)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (notePosition >= DataManager.notes.lastIndex){

            val menuItem = menu?.findItem(R.id.action_next)
            menuItem?.let {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }
        }
       return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag,"OnPause")

    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putInt(NOTE_POSITION,notePosition)
    }
}
