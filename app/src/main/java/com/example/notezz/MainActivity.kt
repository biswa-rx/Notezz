package com.example.notezz

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.notezz.adapter.MainAdapter
import com.example.notezz.callback.ItemClickListener
import com.example.notezz.callback.SwipeToDeleteCallback
import com.example.notezz.databinding.ActivityMainBinding
import com.example.notezz.model.note_model.NoteModelDB
import com.example.notezz.utils.CreateVibrationEffect
import com.example.notezz.utils.CustomToast
import com.example.notezz.utils.NetworkUtils
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,ItemClickListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewMain: RecyclerView
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var mainAdapter: MainAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolbar
        recyclerViewMain = binding.recyclerNote
        swipeRefreshLayout = binding.swipeRefreshLayoutMain
        setSupportActionBar(toolbar)
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)

        handleOnClickEvent()

        val noteRepository = (application as NotezzApplication).noteRepository
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer {
            mainAdapter.submitList(applyNoteFilter(it))
        })
        swipeRefreshLayout.setOnRefreshListener {
            if (NetworkUtils.isInternetAvailable(applicationContext)) {
                noteViewModel.syncNote()
            }else {
                CustomToast.makeToast(this,"No internet\n Refresh failed")
            }
            swipeRefreshLayout.isRefreshing = false
        }
        initRecyclerView()
        noteViewModel.getAllNote()
    }

    private fun applyNoteFilter(list:List<NoteModelDB>):List<NoteModelDB> {
        val listFiltered = list.filter { !it.isDeleted }
        return listFiltered.reversed()
    }

    private fun initRecyclerView() {
        mainAdapter = MainAdapter(this)
//        val layoutManager = LinearLayoutManager(this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewMain.layoutManager = layoutManager
        recyclerViewMain.setHasFixedSize(true)
        recyclerViewMain.adapter = mainAdapter
        // For swipe effect
        val swipeToDeleteCallback = SwipeToDeleteCallback(mainAdapter,noteViewModel,this)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewMain)
    }

    private fun handleOnClickEvent() {
        binding.tvSearchNotes.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        })
        binding.addNoteFloatingButton.setOnClickListener {
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.item_notes -> {
                println("Hello Bro have an fun")
            }
            R.id.item_reminder -> {
                println("NO FUN")
            }
            R.id.item_archive -> {
                gotoArchiveActivity()
            }
            else -> {

            }
        }
        return true
    }

    private fun gotoArchiveActivity() {
        startActivity(Intent(this,ArchiveActivity::class.java))
    }


    override fun onItemClick(position: Int) {
        val note = mainAdapter.currentList.get(position)

        val intent = Intent(this,UpdateNoteActivity::class.java)
        intent.putExtra("noteId",note.noteId)
        intent.putExtra("id",note.id)
        intent.putExtra("userId",note.userId)
        intent.putExtra("noteTitle",note.name)
        intent.putExtra("noteText",note.text)
        intent.putExtra("noteColor",note.color)
        intent.putExtra("isCreated",note.isCreated);
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int) {
        CreateVibrationEffect(this)

    }
}