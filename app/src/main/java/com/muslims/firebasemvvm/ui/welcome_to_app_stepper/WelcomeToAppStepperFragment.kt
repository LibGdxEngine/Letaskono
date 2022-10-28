package com.muslims.firebasemvvm.ui.welcome_to_app_stepper

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.RegistrationFragmentBinding

class WelcomeToAppStepperFragment : Fragment() {

    private lateinit var stepperViewModel: WelcomeToAppStepperViewModel
    private var _binding: RegistrationFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val MAX_STEP = 5
    val about_title_array = arrayOf(
        "مرحبا بكم في تطبيق لتسكنوا",
        "قم بادخال بيانات التواصل",
        "قم بالتعريف عن نفسك",
        "ابحث بدقة وتمهل في الاختيار",
        "اختر شريك حياتك بنفسك",

    )
    private val about_description_array = arrayOf(
        "لتسكنوا هو أفضل تطبيق للزواج الإسلامي الذي يرضي الله عز وجل.",
        "يجب عليك ان تدخل جميع البيانات حتى نتأكد من تفعيل حسابك بشكل دائم.",
        "أخبرنا بالمزيد عنك وعن شخصيتك حتى نستطيع التوفيق بينك وبين شريك حياتك.",
        "يمكنك البحث عن اي مواصفات تريدها وحسب المكان الذي تريده.",
        "اذا اعجبتك مواصفاته فلا تترد في التواصل معنا لنقوم بالتنسيق بينكما كما يجب.")

    private val about_images_array = intArrayOf(
        R.drawable.logo,
        R.drawable.enrollment,
        R.drawable.questionnaire,
        R.drawable.search,
        R.drawable.man_and_woman
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //TODO: add your code here
        initComponent()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stepperViewModel = ViewModelProvider(this).get(WelcomeToAppStepperViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun initComponent() {
        binding.btClose.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_home)
            navController.navigate(R.id.action_welcomeToAppStepperFragment_to_navigation_home)
        }
        // adding bottom dots
        bottomProgressDots(0)
        val myViewPagerAdapter = MyViewPagerAdapter()
        binding.viewPager.setAdapter(myViewPagerAdapter)
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding.btnNext.setOnClickListener(View.OnClickListener {
            val current: Int = binding.viewPager.getCurrentItem() + 1
            if (current < MAX_STEP) {
                // move to next screen
                binding.viewPager.setCurrentItem(current)
            } else {
                //happen when stepper finish
                val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_home)
                navController.navigate(R.id.action_welcomeToAppStepperFragment_to_navigation_home)
            }
        })
    }

    private fun bottomProgressDots(current_index: Int) {
        val dotsLayout = binding.layoutDots
        val dots =
            arrayOfNulls<ImageView>(MAX_STEP)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(context)
            val width_height = 15
            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.layoutParams = params
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(resources.getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!
                .setColorFilter(resources.getColor(R.color.orange_400), PorterDuff.Mode.SRC_IN)
        }
    }

    //  viewpager change listener
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            bottomProgressDots(position)
            if (position == about_title_array.size - 1) {
                binding.btnNext.setText("التالي")
                binding.btnNext.setBackgroundResource(R.color.orange_500)
                binding.btnNext.setTextColor(Color.WHITE)
            } else {
                binding.btnNext.setText("التالي")
                binding.btnNext.setBackgroundResource(R.color.colorPrimary)

            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context) as LayoutInflater?
            val view: View = layoutInflater!!.inflate(R.layout.item_stepper_registration, container, false)
            (view.findViewById<View>(R.id.title) as TextView).setText(about_title_array.get(position))
            (view.findViewById<View>(R.id.description) as TextView).setText(about_description_array.get(
                position))
            (view.findViewById<View>(R.id.image) as ImageView).setImageResource(about_images_array.get(
                position))
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return about_title_array.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}