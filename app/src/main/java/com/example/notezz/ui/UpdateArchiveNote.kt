package com.example.notezz.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.notezz.NotezzApplication
import com.example.notezz.R
import com.example.notezz.databinding.ActivityAddNoteBinding
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.utils.CustomToast
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory

class UpdateArchiveNote : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteId: Int = 0
    private var noteTitle: String = ""
    private var noteText: String = ""
    private var noteColor: String = "#FFFFFF"
    private var isDeletedInUi: Boolean = false
    private val TAG = "AddNoteActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        setSupportActionBar(binding.addNoteToolbar);
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        noteId = intent.getIntExtra("noteId",0)
        noteTitle = intent.getStringExtra("noteTitle").toString()
        noteText = intent.getStringExtra("noteText").toString()
        noteColor = intent.getStringExtra("noteColor").toString()
        updateUi()

        val noteRepository = (application as NotezzApplication).noteRepository
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)

    }

    private fun updateUi() {
        binding.edNoteTitle.text = Editable.Factory.getInstance().newEditable(noteTitle)
        binding.edNoteText.text = Editable.Factory.getInstance().newEditable(noteText)
        binding.addNoteParent.setBackgroundColor(Color.parseColor(noteColor))
        binding.addNoteToolbar.setBackgroundColor(Color.parseColor(noteColor))
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
                isDeletedInUi = true
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateNote() {
        val noteTitle = binding.edNoteTitle.text.toString()
        val noteText = binding.edNoteText.text.toString()
        if(noteTitle.trim().isEmpty() and noteText.trim().isEmpty()) {
            CustomToast.makeToast(this,"Empty note discarded")
            return
        }
        val note = ArchiveModelDB(noteId,noteTitle,noteText,noteColor,isDeletedInUi)
        noteViewModel.updateArchiveNote(note)
    }

    override fun onBackPressed() {
        updateNote()
        super.onBackPressed()
    }
}