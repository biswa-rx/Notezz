package com.example.notezz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.notezz.adapter.MainAdapter
import com.example.notezz.databinding.ActivityMainBinding
import com.example.notezz.viewmodels.NoteViewModel
import com.example.notezz.viewmodels.NoteViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
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
        initRecyclerView()

        val noteRepository = (application as NotezzApplication).noteRepository
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(noteRepository)).get(
            NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer {
            mainAdapter.submitList(it.reversed())
        })
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                Toast.makeText(this,"Refreshed",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.isRefreshing = false
            },1000)
        }
    }

    private fun initRecyclerView() {
        mainAdapter = MainAdapter()
        recyclerViewMain.layoutManager = LinearLayoutManager(this)
        recyclerViewMain.setHasFixedSize(true)
        recyclerViewMain.adapter = mainAdapter
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
            else -> {

            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        noteViewModel.getAllNote()
    }
}