package com.muslims.firebasemvvm

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.muslims.firebasemvvm.databinding.DrawerLayoutBinding
import com.muslims.firebasemvvm.utils.DrawerLocker


class HomeActivity : AppCompatActivity(), DrawerLocker {

    private lateinit var binding: DrawerLayoutBinding
    private lateinit var navView: BottomNavigationView
    var filterDrawer: View? = null
    var mainDrawer: Drawer? = null
    var currentFragment = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrawerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        binding.includedHome.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.includedHome.toolbar)


        navView = binding.includedHome.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_advices,
                R.id.navigation_profile,
                R.id.savedApplicationsFragment
            )
        )

        mainDrawer = initMainDrawer()
        filterDrawer = initFilterDrawer()

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragment = destination.id
            val fullScreenFragments = arrayOf(
                R.id.questionsFragment,
                R.id.welcomeToAppStepperFragment,
                R.id.loginFragment,
            )
            if (destination.id in fullScreenFragments) {
                enterFullScreen(true);
            } else {
                enterFullScreen(false)
            }
        }
    }

    private fun enterFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) supportActionBar?.hide() else supportActionBar?.show()
        if (isFullScreen) navView.visibility = View.GONE else navView.visibility = View.VISIBLE
    }

    private fun initMainDrawer(): Drawer {
        //create the drawer and remember the `Drawer` result object
        val drawer1 = DrawerBuilder()
            .withActivity(this)
            .withToolbar(binding.includedHome.toolbar)
            .addDrawerItems(
                PrimaryDrawerItem().withName(R.string.drawer_item_home)
                    .withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                PrimaryDrawerItem().withName(R.string.drawer_item_free_play)
                    .withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom)
                    .withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                    .withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help)
                    .withIcon(FontAwesome.Icon.faw_question).withEnabled(true),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(
                    FontAwesomeBrand.Icon.fab_github
                ),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact)
                    .withIcon(FontAwesome.Icon.faw_bullhorn)
            )
            .withStickyHeader(layoutInflater.inflate(R.layout.header, null))
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    Toast.makeText(applicationContext, "${position}", Toast.LENGTH_SHORT).show()
                    return false
                }
            })
            .build()
        return drawer1
    }

    private fun initFilterDrawer(): View {

        binding.btnApply.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.btnCloseFilter.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.includedHome.filterButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        return binding.drawerLayout
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding.drawerLayout.setDrawerLockMode(lockMode)
        mainDrawer?.drawerLayout?.setDrawerLockMode(lockMode)
    }

    override fun onBackPressed() {
        val mainFragments = arrayOf(
            R.id.navigation_advices,
            R.id.savedApplicationsFragment,
            R.id.navigation_profile,
        )
        if (currentFragment in mainFragments) {
            val navController = findNavController(R.id.nav_host_fragment_activity_home)
            navController.navigate(R.id.navigation_home)
        } else if (currentFragment == R.id.navigation_home) {
            showMaterialExitMessage(this)
        } else {
            super.onBackPressed()
        }
    }

    private fun showMaterialExitMessage(ctx: Context) {

        MaterialAlertDialogBuilder(ctx)
            .setTitle("???? ???????? ???????????? ???????? ??")
            .setMessage("")
            .setPositiveButton(
                "????????????"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .setNegativeButton(
                "????????"
            ) { dialogInterface, i -> finish() }
            .show()

    }

}