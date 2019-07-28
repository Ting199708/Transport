package com.ting199708.transport

import android.content.SharedPreferences
import android.net.Uri
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*


class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        MainActivity.OnFragmentInteractionListener, ReservationActivity.OnFragmentInteractionListener,
        SettingActivity.OnFragmentInteractionListener, AboutActivity.OnFragmentInteractionListener{
    private lateinit var settings: SharedPreferences
    var currentFragment : Fragment? = null
    var header_text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        settings = getSharedPreferences("DATA", 0)
        if (nav_view.getHeaderCount() > 0) {
            val header = nav_view.getHeaderView(0)
            header_text = header.findViewById(R.id.textView) as TextView
            header_text!!.text = settings.getString("STUDENT","未命名")+"同學"
        }

        currentFragment = MainActivity()
        switch(MainActivity()).commit()
        //Load ad
        val adRequest = AdRequest.Builder().build()
        MobileAds.initialize(this)
        adView.loadAd(adRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        var fragment : Fragment? = null
        var fragmentClass : Class<*>? = null
        when(id) {
            R.id.start -> {
                switch(MainActivity()).commit()
                this.title = "乘車資訊"
            }
            R.id.reservation -> {
                switch(ReservationActivity()).commit()
                this.title = "末班車預約/檢視"
            }
            R.id.setting -> {
                switch(SettingActivity()).commit()
                this.title = "設定"
            }
            R.id.about -> {
                switch(AboutActivity()).commit()
                this.title = "關於"
            }
        }
        /*try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e : Exception) {
            e.printStackTrace()
        }
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.EmptyContent,fragment).commit()*/

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onFragmentInteraction(uri: Uri) {

    }

    fun switch(targetFragment : Fragment) : FragmentTransaction {
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            if (currentFragment!!.isAdded) {
                transaction.hide(currentFragment!!)
            }
            transaction.add(R.id.EmptyContent, targetFragment,targetFragment.javaClass.name)
        } else {
            transaction
                    .hide(currentFragment!!)
                    .show(targetFragment)
        }
        currentFragment = targetFragment

        return  transaction
    }

}
