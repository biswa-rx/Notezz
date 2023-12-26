package com.example.notezz.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityAddNoteBinding
import com.example.notezz.utils.CustomToast
import com.example.notezz.utils.convertColorToHexString
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import javax.inject.Inject


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var isNoteDiscarded : Boolean = false
    @Inject
    lateinit var noteViewModelFactory: NoteViewModelFactory
    private var noteColor: String = "#FFFFFF"
    private val TAG = "AddNoteActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as NotezzApplication).applicationComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        setSupportActionBar(binding.addNoteToolbar);
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(
            NoteViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //Save note here I have to implement later
                onBackPressed()
                return true
            }
            R.id.item_save -> {
                onBackPressed()
            }
            R.id.item_delete -> {
                Handler(Looper.getMainLooper()).post(Runnable {
                    CustomToast.makeToast(this,"This note discarded")
                })
                isNoteDiscarded = true
                onBackPressed()
            }
            R.id.item_color -> {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(!isNoteDiscarded) {
            createNote()
        }
    }

    private fun createNote() {
        val noteTitle = binding.edNoteTitle.text.toString()
        val noteText = binding.edNoteText.text.toString()
        if(noteTitle.trim().isEmpty() and noteText.trim().isEmpty()) {
            CustomToast.makeToast(this,"Empty note discarded")
            return
        }
        noteViewModel.createNote(noteTitle,noteText,noteColor)
    }

    fun openColorPicker() {
        val colorPicker = AmbilWarnaDialog(this, Color.RED,true, object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {

            }

            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                noteColor = convertColorToHexString(color)
                binding.addNoteParent.setBackgroundColor(Color.parseColor(noteColor))
                binding.addNoteToolbar.setBackgroundColor(Color.parseColor(noteColor))
            }
        })
        colorPicker.show()
    }
}