package com.zipstored.com.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.fragment.HomeFragment
import com.zipstored.com.mFragmentManager
import com.zipstored.com.utils.AdvanceDrawerLayout
import com.zipstored.utils.OnBackPressed
import kotlinx.android.synthetic.main.activity_advance3.*
import kotlinx.android.synthetic.main.app_bar_default.*
import kotlinx.android.synthetic.main.app_bar_search_layout.*
import kotlinx.android.synthetic.main.content_default.*
import com.zipstored.com.iinterface.FragmentCommunicator
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.content_details.*
import org.json.JSONException
import org.json.JSONObject


class DashActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var custom_fragment_manager: mFragmentManager
    lateinit var fragmentTransaction: androidx.fragment.app.FragmentTransaction
    lateinit var currentFragment: androidx.fragment.app.Fragment
    var mDrawerToggle: ActionBarDrawerToggle? = null
    var fragmentCommunicator: FragmentCommunicator? = null
    var toggle: ActionBarDrawerToggle? = null


    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        drawer!!.closeDrawer(GravityCompat.START)

        println("onNavigationItemSelected")
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance3)
        setSupportActionBar(toolbar)

        mySharedPreferance = MySharedPreferance(this)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(this, supportFragmentManager)


        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle!!)
        toggle!!.syncState()

        /*iv_menu.setOnClickListener {
            drawer!!.openDrawer(Gravity.START)
        }*/

        nav_view.invalidate()
        toggle!!.isDrawerIndicatorEnabled = true
        nav_view.setNavigationItemSelectedListener(this)

        toolbar.setNavigationOnClickListener {
            println("DashActivity toolbar click =======>>>>>> " + getSupportFragmentManager().getBackStackEntryCount())

            println("DashActivity toolbar click current fragment =======>>>>>> "+getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().toString())

            println("DashActivity toolbar fragment tag =======>>>>>> " + getSupportFragmentManager().findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().toString()))

            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                toggle!!.isDrawerIndicatorEnabled = true
                getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false);
                drawer.setDrawerLockMode(
                    androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED,
                    GravityCompat.START
                );
                drawer.openDrawer(GravityCompat.START);
                drawer.addDrawerListener(toggle!!);
            }

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                toggle!!.isDrawerIndicatorEnabled = true
                getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false);
                println("DashActivity toolbar click 3")
                drawer.setDrawerLockMode(
                    androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED,
                    GravityCompat.START
                );
                drawer.openDrawer(GravityCompat.START);
                drawer.addDrawerListener(toggle!!);
            }

            if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
                //getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
                //getSupportFragmentManager().popBackStackImmediate()
                toggle = ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
                )
                drawer!!.addDrawerListener(toggle!!)
                toggle!!.syncState()
                System.out.println("Back click ====>>" + getSupportFragmentManager().getBackStackEntryCount());

                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false);
                    toggle!!.isDrawerIndicatorEnabled = true
                    toggle!!.syncState()
                    println("DashActivity toolbar click test")
                    /*drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
                    drawer.openDrawer(GravityCompat.START);
                    drawer.addDrawerListener(toggle);*/
                }

            }

            if (getSupportFragmentManager().getBackStackEntryCount() >2)
            {
                getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
                getSupportFragmentManager().popBackStackImmediate()
            }


        }

        drawer!!.setViewScale(Gravity.START, 0.9f)
        drawer!!.setViewElevation(Gravity.START, 20F)

        drawer!!.useCustomBehavior(Gravity.END)

        custom_fragment_manager = mFragmentManager()
        //clearAllFragment()
        //callFragment(mFragmentManager.HOME_FRAGMENT)


        var homeFragment = HomeFragment()
        callFragment(homeFragment, false)

        /* iv_back.setOnClickListener {

             if (drawer.isDrawerOpen(GravityCompat.START)) {
                 drawer.closeDrawer(GravityCompat.START)
             } else {
                 if (currentFragment is OnBackPressed) {
                     (currentFragment as OnBackPressed).onBackPressed()
                 } else {
                     callBackPressed()
                 }
             }
         }*/


        tv_search.setOnClickListener {
            fragmentCommunicator!!.passData("Not implement yet");
        }

        nav_view.setNavigationItemSelectedListener(this)

        ll_my_profile.setOnClickListener {
            println("onNavigationItemSelected my profile")
        }

        ll_log_out.setOnClickListener {
            log_out()
        }

    }

    fun passVal(fragmentCommunicator: FragmentCommunicator) {
        this.fragmentCommunicator = fragmentCommunicator

    }


    override fun onBackPressed() {
        println("onBackPressed 3 ======>>>>>> " + getSupportFragmentManager().getBackStackEntryCount())
        /* if (drawer!!.isDrawerOpen(GravityCompat.START)) {
             drawer!!.closeDrawer(GravityCompat.START)
         } else {
             println("onBackPressed 1")
             if (currentFragment is OnBackPressed) {
                 (currentFragment as OnBackPressed).onBackPressed()
             } else {
                 println("onBackPressed 2")
                 callBackPressed()
             }
             super.onBackPressed()
         }*/

        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            Toast.makeText(this, "Are You Sure Want to Exit!", Toast.LENGTH_SHORT).show()
        }

    }


    fun callFragment(tagFragment: androidx.fragment.app.Fragment,isNew: Boolean = false) {
        /*currentFragment = custom_fragment_manager.openFragment(tagFragment, isNew)
        currentFragment.arguments = bundle
        custom_fragment_manager.addStackFragment(tagFragment)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        fragmentTransaction.replace(R.id.container, currentFragment)
        fragmentTransaction.commit()*/

        println("tag fragment ====>>>> "+tagFragment)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack("" + tagFragment)
        fragmentTransaction.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        fragmentTransaction.replace(R.id.container, tagFragment)
        fragmentTransaction.commit()
    }

    fun callFragment(tagFragment: androidx.fragment.app.Fragment,bundle: Bundle,isNew: Boolean = false) {
        /*currentFragment = custom_fragment_manager.openFragment(tagFragment, isNew)
        currentFragment.arguments = bundle
        custom_fragment_manager.addStackFragment(tagFragment)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        fragmentTransaction.replace(R.id.container, currentFragment)
        fragmentTransaction.commit()*/

        tagFragment.arguments = bundle
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack("" + tagFragment)
        fragmentTransaction.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        fragmentTransaction.replace(R.id.container, tagFragment)
        fragmentTransaction.commit()
    }


    fun callFragment(tagFragment: Int ,isNew: Boolean = false) {
        currentFragment = custom_fragment_manager.openFragment(tagFragment, isNew)
        custom_fragment_manager.addStackFragment(tagFragment)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        fragmentTransaction.replace(R.id.container, currentFragment)
        fragmentTransaction.commit()
    }




    fun callBackPressed(isNew: Boolean = false) {
        try {
            if (custom_fragment_manager.hasFragmentInStack()) {
                currentFragment = custom_fragment_manager.openPreviousFragment(isNew)
                if (currentFragment is HomeFragment) {
                    custom_fragment_manager.clearFragmentStack()
                    callFragment(mFragmentManager.HOME_FRAGMENT)
                } else {
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.disallowAddToBackStack()
                    fragmentTransaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_right_exit
                    )
                    fragmentTransaction.replace(R.id.container, currentFragment)
                    fragmentTransaction.commit()
                }

            } else {
                val dialog = AlertDialog.Builder(this@DashActivity)
                dialog.setTitle("Do you want to exit?")
                dialog.setPositiveButton("Yes") { dialogInterface, i -> finish() }
                dialog.setNegativeButton("Cancel", null)
                dialog.create().show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun removeFragment(fragmentTag: Int) {
        custom_fragment_manager.removeThisFragment(fragmentTag)
    }

    fun chnageicon(status: String) {
        if (status.equals("0")) {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false);
            toggle!!.syncState()
        }
    }

    fun clearAllFragment() {
        custom_fragment_manager.clearFragmentStack()
    }

    /*fun drawerimageset(status: String) {
        if (status.equals("1", ignoreCase = true)) {
            iv_menu.visibility = View.VISIBLE
            iv_back.visibility = View.GONE
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else if (status.equals("0", ignoreCase = true)) {
            iv_menu.visibility = View.GONE
            iv_back.visibility = View.VISIBLE
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        else if (status.equals("2", ignoreCase = true)) {
            iv_menu.visibility = View.GONE
            iv_back.visibility = View.GONE
        }
    }*/

    fun titletext(title: String, callfrom: String) {
        if (callfrom.equals("HomeFrag")) {
            supportActionBar!!.title = title
            toolbar.subtitle = "Please set your preferences"
            search_layout.visibility = View.GONE
        }
        if (callfrom.equals("ProviderListingFrag")) {
            supportActionBar!!.title = title
            toolbar.subtitle = ""
            search_layout.visibility = View.VISIBLE
        }
        if (callfrom.equals("StorageDetails"))
        {
            supportActionBar!!.title = title
            toolbar.subtitle = ""
            search_layout.visibility = View.GONE
        }
    }

    fun getSearchText(): String {
        return et_search.text.toString()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        println("backkk")
        when (item!!.getItemId()) {
            android.R.id.home -> {
                println("backkk")
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    fun log_out() {
        val hashMap = HashMap<String, String>()
        hashMap["device_type"] = "4"
        hashMap["request_access_token"] = mySharedPreferance.getPreferancceString(mySharedPreferance.access_token).toString()
        hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()

        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getLogOutResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("logout | RetrofitResponse ========>>>>>>" + jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            //showMessagePopup(jsonObject.getString("response_message"))
                            val i = Intent(this@DashActivity, SignInActivity::class.java)
                            startActivity(i)
                            finish()

                        } else {
                            showMessagePopup(jsonObject.getString("response_message"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
    }

    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(this)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }

}
