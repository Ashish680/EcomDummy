package com.ashish.e_comdummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.ashish.e_comdummy.databinding.ActivityMainBinding
import com.ashish.e_comdummy.fragment.ListFragment
import com.ashish.e_comdummy.roomDB.AppDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)



        val homeFragment: Fragment = ListFragment()
        val fm = this.supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(0, 0, 0, 0)
        fragmentTransaction.add(R.id.container, homeFragment)
        fragmentTransaction.addToBackStack("ListFragment")
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}