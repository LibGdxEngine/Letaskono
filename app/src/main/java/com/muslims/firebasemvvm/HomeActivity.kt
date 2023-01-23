package com.muslims.firebasemvvm

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.balysv.materialripple.MaterialRippleLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.muslims.firebasemvvm.databinding.DrawerLayoutBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.utils.DrawerLocker
import com.muslims.firebasemvvm.utils.StoredAuthUser


class HomeActivity : AppCompatActivity(), DrawerLocker {

    private lateinit var binding: DrawerLayoutBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private var mToast: Toast? = null
    private lateinit var observer: Observer<User>
    var filterDrawer: View? = null
    var mainDrawer: Drawer? = null
    var currentFragment = R.id.navigation_home
    var signedInUser: User? = null
    var dialog: Dialog? = null
    var fromAge: String? = null
    var toAge: String? = null

    //    var manSwitchChecked: Boolean? = true
//    var womanSwitchChecked: Boolean? = true
    var menStatus: String? = ""
    var womenStatus: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrawerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        binding.includedHome.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.includedHome.toolbar)
        homeActivityViewModel =
            ViewModelProvider(this).get(HomeActivityViewModel::class.java)


        val user = StoredAuthUser.getUser(applicationContext)
        if (user != null) {
            homeActivityViewModel.getSignedInUser(user)
        }

        homeActivityViewModel.status.observe(this, Observer { status ->
            when (status) {
                AuthenticationStatus.LOADING -> {

                }
                AuthenticationStatus.DONE -> {

                }
                AuthenticationStatus.ERROR -> {

                }
            }
        })

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
//        filterDrawer = initFilterDrawer()

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
            .withSelectedItem(-1)
            .withToolbar(binding.includedHome.toolbar)
            .addDrawerItems(
                PrimaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_home)
                    .withIcon(R.drawable.flowers).withIdentifier(1),
                PrimaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_free_play)
                    .withIcon(R.drawable.why),
                PrimaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_custom)
                    .withIcon(R.drawable.about_us).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_settings)
                    .withIcon(R.drawable.donate),
                SecondaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_help)
                    .withIcon(R.drawable.share_icon).withEnabled(true),
                SecondaryDrawerItem().withSelectable(false)
                    .withName(R.string.drawer_item_open_source).withIcon(
                        R.drawable.start
                    ),
                SecondaryDrawerItem().withSelectable(false).withName(R.string.drawer_item_contact)
                    .withIcon(R.drawable.social),
                SecondaryDrawerItem().withSelectable(false).withName("الادارة")
                    .withIcon(R.drawable.ic_baseline_admin_panel_settings_24)
            )
            .withStickyHeader(layoutInflater.inflate(R.layout.header, null))
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        0 -> {
                            if (mToast != null) mToast?.cancel()
                            mToast = Toast.makeText(
                                applicationContext,
                                "قريبا إن شاء الله",
                                Toast.LENGTH_LONG
                            )
                            mToast?.show()
                        }
                        1 -> {
                            if (mToast != null) mToast?.cancel()
                            mToast = Toast.makeText(
                                applicationContext,
                                "قريبا إن شاء الله",
                                Toast.LENGTH_LONG
                            )
                            mToast?.show()
//                            startActivity(Intent(this@HomeActivity, WhyMarryActivity::class.java))
                        }
                        2 -> {
                            if (mToast != null) mToast?.cancel()
                            mToast = Toast.makeText(
                                applicationContext,
                                "قريبا إن شاء الله",
                                Toast.LENGTH_LONG
                            )
                            mToast?.show()
                        }
                        3 -> {
                            if (mToast != null) mToast?.cancel()
                            mToast = Toast.makeText(
                                applicationContext,
                                "قريبا إن شاء الله",
                                Toast.LENGTH_LONG
                            )
                            mToast?.show()
                        }
                        4 -> {
                            if (mToast != null) mToast?.cancel()
                            mToast = Toast.makeText(
                                applicationContext,
                                "قريبا إن شاء الله",
                                Toast.LENGTH_LONG
                            )
                            mToast?.show()
                        }
                        5 -> {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                " حمل هذا التطبيق للزواج الشرعي الإسلامي " + " \n " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                            )
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                            return false
                        }
                        6 -> {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + getPackageName())
                                    )
                                )
                            } catch (e: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())
                                    )
                                )
                            }
                            return false
                        }
                        7 -> {
                            setClickToChat(
                                binding.root,
                                getString(R.string.admin_phone_number),
                                "السلام عليكم ورحمة الله \n "
                            )
                            return false
                        }
                        8 -> {
                            try {
                                val navController =
                                    findNavController(R.id.nav_host_fragment_activity_home)
                                navController.navigate(R.id.action_navigation_home_to_adminFragment)
                            } catch (exception: Exception) {
                                if (mToast != null) mToast?.cancel()
                                mToast = Toast.makeText(
                                    applicationContext,
                                    "أنت متواجد في صفحة الادارة بالفعل",
                                    Toast.LENGTH_LONG
                                )
                                mToast?.show()
                            }
//                            startActivity(Intent(this@HomeActivity, AdminActivity::class.java))
                            return false
                        }
                    }
                    return true
                }
            })
            .build()
        drawer1.actionBarDrawerToggle =
            return drawer1
    }

    fun setClickToChat(v: View, toNumber: String, message: String) {
        val url = "https://wa.me/$toNumber/?text=" + message
        try {
            val pm = v.context.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            v.context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            v.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun initFilterDrawer(): View {

//        val searchCode = binding.searchByCode.text.toString()
//
//        manSwitchChecked = binding.manSwitch.isChecked
//        womanSwitchChecked = binding.womanSwitch.isChecked
//
//        binding.searchByAge.setOnClickListener {
//            showSearchByAge()
//        }
//
//        binding.searchByStatus.setOnClickListener {
//            showSearchByStatus(manSwitchChecked!!, womanSwitchChecked!!)
//        }

//        binding.manSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            Toast.makeText(applicationContext, "${isChecked}", Toast.LENGTH_SHORT).show()
//        })
//
//        binding.womanSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            Toast.makeText(applicationContext, "${isChecked}", Toast.LENGTH_SHORT).show()
//        })
//
//        binding.btnApply.setOnClickListener {
//            binding.drawerLayout.closeDrawer(GravityCompat.END)
//        }
//
//        binding.btnCloseFilter.setOnClickListener {
//            binding.drawerLayout.closeDrawer(GravityCompat.END)
//        }
//
//        binding.includedHome.filterButton.setOnClickListener {
//            binding.drawerLayout.openDrawer(GravityCompat.END)
//        }
        return binding.drawerLayout
    }

    private fun showSearchByStatus(manSwitchChecked: Boolean, womanSwitchChecked: Boolean) {
        dialog = Dialog(this@HomeActivity)
        dialog!!.setContentView(R.layout.status_dialog_layout)
        dialog!!.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.setCancelable(true)
        dialog!!.getWindow()?.getAttributes()?.windowAnimations = R.style.animation
        if (manSwitchChecked) {
            dialog!!.findViewById<LinearLayout>(R.id.menStatus).visibility = View.VISIBLE
            menStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusSingle).isChecked) "أعزب" + "," else ""
            menStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusWife).isChecked) "متزوج" + "," else ""
            menStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusDivorced).isChecked) "مطلق" + "," else ""
            menStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusNoWife).isChecked) "أرمل" + "," else ""
        } else {
            womenStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusSingle).isChecked) "أعزب" + "," else ""
            womenStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusWife).isChecked) "متزوج" + "," else ""
            womenStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusDivorced).isChecked) "مطلق" + "," else ""
            womenStatus += if (dialog!!.findViewById<MaterialCheckBox>(R.id.menStatusNoWife).isChecked) "أرمل" + "," else ""
        }
        if (womanSwitchChecked) {
            dialog!!.findViewById<LinearLayout>(R.id.womenStatus).visibility = View.VISIBLE
        }
        dialog!!.show()
    }

    private fun showSearchByAge() {
        dialog = Dialog(this@HomeActivity)
        dialog!!.setContentView(R.layout.age_range_dialog_layout)
        dialog!!.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.setCancelable(true)
        dialog!!.getWindow()?.getAttributes()?.windowAnimations = R.style.animation

        val rangeSlider = dialog!!.findViewById<RangeSlider>(R.id.ageRangeSlider)
        val confirmBtn = dialog!!.findViewById<MaterialRippleLayout>(R.id.confirmBtn)

        confirmBtn.setOnClickListener {
            fromAge = rangeSlider.valueFrom.toInt().toString()
            toAge = rangeSlider.valueTo.toInt().toString()
            dialog!!.dismiss()
        }

        dialog!!.show()
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
        } else if (currentFragment == R.id.questionsFragment) {
            showMaterialQuestionsExitMessage(this)
        } else {
            super.onBackPressed()
        }
    }

    private fun showMaterialExitMessage(ctx: Context) {
        MaterialAlertDialogBuilder(ctx)
            .setTitle("هل تريد الخروج فعلا ؟")
            .setMessage("")
            .setPositiveButton(
                "البقاء"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .setNegativeButton(
                "خروج"
            ) { dialogInterface, i -> finish() }
            .show()
    }

    private fun showMaterialQuestionsExitMessage(ctx: Context) {
        MaterialAlertDialogBuilder(ctx)
            .setTitle("هل تريد الخروج فعلا ؟")
            .setMessage("إذا خرجت الأن فلن يتم تسجيل الإجابات التي أدخلتها")
            .setPositiveButton(
                "البقاء"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .setNegativeButton(
                "خروج"
            ) { dialogInterface, i ->
                val navController = findNavController(R.id.nav_host_fragment_activity_home)
                navController.navigate(R.id.navigation_home)
            }
            .show()
    }


}