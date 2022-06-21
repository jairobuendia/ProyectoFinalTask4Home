package com.example.task4home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.task4home.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val UserFragment = com.example.task4home.UserFragment()
    private val TaskFragment = com.example.task4home.TaskFragment()
    private val PrizeFragment = com.example.task4home.PrizeFragment()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        replaceFragment(TaskFragment)

        myBottomNavigationView.selectedItemId = R.id.taskFragment



       myBottomNavigationView.setOnItemSelectedListener { item ->
           when (item.itemId) {
               R.id.userFragment -> {
                   replaceFragment(UserFragment)
                   item.title
               }
               R.id.taskFragment -> {
                   replaceFragment(TaskFragment)
                   item.title
               }
               R.id.prizeFragment -> {
                   replaceFragment(PrizeFragment)
                   item.title
               }
               else -> Log.i("Test", "Mensaje random")
           }
           true
       }
    }

    override fun onBackPressed() {}

    internal fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}