package com.muslims.firebasemvvm

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.muslims.firebasemvvm.databinding.DrawerLayoutBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: DrawerLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = DrawerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        binding.includedHome.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.includedHome.toolbar)
        
        val navView: BottomNavigationView = binding.includedHome.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_advices, R.id.navigation_profile, R.id.detailsFragment
            )
        )
        initMainDrawer()
        initFilterDrawer()

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> {
                    supportActionBar?.hide() // to hide
                } else -> {
                supportActionBar?.show() // to show
                }
            }
        }
    }

    private fun initMainDrawer() {

        //if you want to update the items at a later time it is recommended to keep it in a variable
        val item1 = PrimaryDrawerItem().withIdentifier(1).withName(R.string.app_name)
        val item2 = SecondaryDrawerItem().withIdentifier(2).withName(R.string.app_name)

        //create the drawer and remember the `Drawer` result object
        val drawer1 = DrawerBuilder()
            .withActivity(this)
            .withToolbar(binding.includedHome.toolbar)
            .addDrawerItems(
                item1,
                DividerDrawerItem(),
                item2,
                SecondaryDrawerItem().withName(R.string.app_name)
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    Toast.makeText(applicationContext, "${position}", Toast.LENGTH_SHORT).show()
                    return false
                }
            })
            .build()
    }

    private fun initFilterDrawer() {

        binding.btnApply.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.btnCloseFilter.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.includedHome.filterButton.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }


    }
}