package com.muslims.firebasemvvm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.muslims.firebasemvvm.databinding.DrawerLayoutBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: DrawerLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.id.navigation_home, R.id.navigation_advices, R.id.navigation_profile, R.id.savedApplicationsFragment
            )
        )
        initMainDrawer()
        initFilterDrawer()

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
//                R.id.UsersDetailsFragment -> {
//                    supportActionBar?.hide() // to hide
//                    navView.visibility = View.GONE
//                }
                R.id.registrationStepperFragment -> {
                    supportActionBar?.hide() // to hide
                    navView.visibility = View.GONE
                }
                R.id.loginFragment -> {
                    supportActionBar?.hide() // to hide
                    navView.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show() // to show
                    navView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initMainDrawer() {
        //create the drawer and remember the `Drawer` result object
        val drawer1 = DrawerBuilder()
            .withActivity(this)
            .withToolbar(binding.includedHome.toolbar)
            .addDrawerItems(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(true),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(
                    FontAwesomeBrand.Icon.fab_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
            )
            .withStickyHeader(layoutInflater.inflate(R.layout.header, null))
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