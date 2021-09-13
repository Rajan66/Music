package com.example.musicplayer

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.musicplayer.fragments.FavoriteFragment
import com.example.musicplayer.fragments.HomeFragment
import com.example.musicplayer.fragments.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val favoriteFragment = FavoriteFragment()
    private val settingFragment = SettingFragment()

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermit()
        displayFragment()

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> changeFragment(homeFragment)
                R.id.menu_favorites -> changeFragment(favoriteFragment)
                R.id.menu_settings -> changeFragment(settingFragment)
            }
            true
        }


    }

    private fun changeFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    private fun displayFragment(){
        val transaction =  supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,homeFragment)
        transaction.commit()
    }

    private fun getPermit() {
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted:Boolean ->
            if (isGranted){
                Log.i(TAG, "getPermit: Granted")
            }else{
                Log.i(TAG,"getPermit: Failure")
            }

        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission
                    .READ_EXTERNAL_STORAGE
            ) -> {
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}