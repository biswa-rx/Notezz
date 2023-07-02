package com.example.notezz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notezz.adapter.ArchiveAdapter
import com.example.notezz.adapter.MainAdapter
import com.example.notezz.callback.ArchiveItemClickListener
import com.example.notezz.callback.ArchiveSwipeToDeleteCallback
import com.example.notezz.callback.ItemClickListener
import com.example.notezz.callback.SwipeToDeleteCallback
import com.example.notezz.databinding.ActivityArchiveBinding
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.model.note_model.NoteModelDB
import com.example.notezz.utils.CustomToast
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory

class ArchiveActivity : AppCompatActivity(),ArchiveItemClickListener {
    lateinit var binding: ActivityArchiveBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerViewArchive:RecyclerView
    private lateinit var archiveAdapter: ArchiveAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_archive)
        recyclerViewArchive = binding.archiveRecyclerview
        archiveAdapter = ArchiveAdapter(this)
        setSupportActionBar(binding.archiveToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back) // Replace with your back arrow icon
        }
        binding.archiveToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val noteRepository = (application as NotezzApplication).noteRepository
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)
        noteViewModel.allArchiveNotes.observe(this, Observer {
            archiveAdapter.submitList(applyNoteFilter(it))
        })
        initRecyclerView()
        noteViewModel.getAllArchiveNote()
    }

    private fun applyNoteFilter(list:List<ArchiveModelDB>):List<ArchiveModelDB> {
        val listFiltered = list.filter { !it.isDeleted }
        return listFiltered.reversed()
    }

    private fun initRecyclerView() {
//        val layoutManager = LinearLayoutManager(this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewArchive.layoutManager = layoutManager
        recyclerViewArchive.setHasFixedSize(true)
        recyclerViewArchive.adapter = archiveAdapter
        // For swipe effect
        val swipeToDeleteCallback = ArchiveSwipeToDeleteCallback(archiveAdapter,noteViewModel,this)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewArchive)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this,UpdateArchiveNote::class.java)
        val archivedNote = archiveAdapter.currentList.get(position)
        intent.putExtra("noteId",archivedNote.noteId)
        intent.putExtra("noteTitle",archivedNote.name)
        intent.putExtra("noteText",archivedNote.text)
        intent.putExtra("noteColor",archivedNote.color)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int) {
        println("Long clicked")
    }
}