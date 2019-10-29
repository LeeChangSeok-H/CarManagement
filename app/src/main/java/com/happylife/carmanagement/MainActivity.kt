package com.happylife.carmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.happylife.carmanagement.home.HomeFragment
import com.happylife.carmanagement.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()

        mainBottomBar.setNavigationChangeListener { view, position ->
            when(view){
                bottomBar_item_home -> {
                    tv_main_toolbar_title.setText(R.string.title_home)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()

                }
                bottomBar_item_search -> {
                    tv_main_toolbar_title.setText(R.string.title_search)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, SearchFragment()).commit()

                }
            }

        }

        ib_main_toolbar_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_setting -> {

                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)

                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

     */

}