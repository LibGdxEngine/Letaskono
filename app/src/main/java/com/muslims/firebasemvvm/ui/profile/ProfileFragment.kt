package com.muslims.firebasemvvm.ui.profile

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentProfileBinding
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.main_questions_form.AuthenticationStatus
import com.muslims.firebasemvvm.utils.StoredAuthUser


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var signedInUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val currentUser = StoredAuthUser.getUser(requireContext())
        if (currentUser != null) {
            val userInfoCompleted = StoredAuthUser.getUserInfoCompleted(requireContext())
            if (!userInfoCompleted) {
                //Account info is not completed, prompt the use to complete it
                binding.signedInUserWithoutCompleteInfoContainer?.visibility = View.VISIBLE
                binding.btnCompleteInfo?.setOnClickListener {
                    goToQuestionsScreen()
                }
            } else {
                profileViewModel.getSignedInUser(currentUser)
            }
        } else {
            binding.noSignedInUserContainer?.visibility = View.VISIBLE
        }

        binding.editMyInfo?.setOnClickListener {
            setClickToChat(it, "+201019867911", "السلام عليكم ورحمة الله \n أريد تعديل بياناتي ")
        }

        binding.termsOfUse.setOnClickListener { showTermServicesDialog() }

        binding.btnCreateAccount.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_navigation_profile_to_registerationFragment,
                    null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
        }

        binding.btnGoToLogin.setOnClickListener {
            this.findNavController().navigate(R.id.action_navigation_profile_to_loginFragment,
                null,
                navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
        }

        profileViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            signedInUser = user
        })

        profileViewModel.getUserStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                AuthenticationStatus.LOADING -> {
                    binding.progressBar?.visibility = View.VISIBLE
                }
                AuthenticationStatus.DONE -> {
                    binding.progressBar?.visibility = View.GONE
                    binding.signedInUserContainesr?.visibility = View.VISIBLE
                    binding.appBar?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
                        if (appBarLayout.height / 1.5f < -verticalOffset) {
                            binding.image?.setVisibility(View.GONE)
                        } else {
                            binding.image?.setVisibility(View.VISIBLE)
                        }
                    })
                    bindUserInfo(signedInUser)
                }
                AuthenticationStatus.ERROR -> {
                    binding.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "حصل خطأ في الانترنت", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        )

        return root
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


    private fun bindUserInfo(signedInUser: User?) {
        var title = if (signedInUser?.gender == "man") "عريس" else "عروسة"
        var userLe7ya: Question? = null
        var userSport: Question? = null
        var userSmoking: Question? = null
        var userStatus: Question? = null
        var userHijab: Question? = null
        if (signedInUser?.gender == "man") {
            binding.profileBackground?.setImageResource(R.drawable.man_background)
            if (signedInUser.questionsList?.first { it.id == "23" }?.answer == "نعم") {
                binding.image?.setImageResource(R.drawable.man_with_lehya)
            } else {
                binding.image?.setImageResource(R.drawable.man_without_lehya)
            }
            title += " ${if (signedInUser?.questionsList?.first { it.id == "23" }?.answer == "نعم") "ملتحي" else " "}"
            userLe7ya = signedInUser?.questionsList?.first { it.id == "23" }
            userSport = signedInUser?.questionsList?.first { it.id == "20" }
            userSmoking = signedInUser?.questionsList?.first { it.id == "21" }
            userStatus = signedInUser?.questionsList?.first { it.id == "19" }
        } else {
            binding.profileBackground?.setImageResource(R.drawable.woman_background)
            when (signedInUser?.questionsList?.first { it.id == "24" }?.answer) {
                "منتقبة" -> {
                    binding.image?.setImageResource(R.drawable.women_with_neqab)
                }
                "مختمرة" -> {
                    binding.image?.setImageResource(R.drawable.women_with_khemar)
                }
                else -> {
                    binding.image?.setImageResource(R.drawable.women_with_hejab)
                }
            }
            title += " ${if (signedInUser?.questionsList?.first { it.id == "24" }?.answer == "منتقبة") "منتقبة" else " "}"
            userHijab = signedInUser?.questionsList?.first { it.id == "24" }
            userStatus = signedInUser?.questionsList?.first { it.id == "22" }
        }
        //General Info
        val userAge = signedInUser?.questionsList?.first { it.id == "2" }
        val userNationality = signedInUser?.questionsList?.first { it.id == "3" }
        val userHeight = signedInUser?.questionsList?.first { it.id == "26" }
        val userWeight = signedInUser?.questionsList?.first { it.id == "6" }
        val userSkin = signedInUser?.questionsList?.first { it.id == "7" }
        val userStateAndLocation = signedInUser?.questionsList?.first { it.id == "11" }
        //Islamic Info
        val userPray = signedInUser?.questionsList?.first { it.id == "1" }
        val userQuran = signedInUser?.questionsList?.first { it.id == "15" }
        val userMusic = signedInUser?.questionsList?.first { it.id == "16" }
        val userA7zab = signedInUser?.questionsList?.first { it.id == "17" }
        //Additional Info
        val userCertificate = signedInUser?.questionsList?.first { it.id == "8" }
        val userJob = signedInUser?.questionsList?.first { it.id == "9" }
        val userFathersJob = signedInUser?.questionsList?.first { it.id == "12" }
        val userMothersJob = signedInUser?.questionsList?.first { it.id == "13" }
        val userBrothers = signedInUser?.questionsList?.first { it.id == "14" }
        //About you
        val userTalkAboutYourself = signedInUser?.questionsList?.first { it.id == "4" }
        val userYourPartner = signedInUser?.questionsList?.first { it.id == "5" }


        binding.title?.text = title
        binding.content?.userCode?.text = "كود: " + signedInUser?.id
        binding.age?.text = "${userAge?.answer}"

        //General Info
        binding.content?.userAge?.text =
            "السن: " + "${userAge?.answer}" + " " + if (userAge?.note.isNullOrEmpty()) "" else "(${userAge?.note})"
        binding.content?.status?.text =
            "الحالة الاجتماعية: " + "${userStatus?.answer}" + " " + if (userStatus?.note.isNullOrEmpty()) "" else "(${userAge?.note})"
        binding.content?.nationality?.text =
            "الجنسية: " + "${userNationality?.answer}" + " " + if (userNationality?.note.isNullOrEmpty()) "" else "(${userNationality?.note})"

        binding.content?.height?.text =
            "الطول: " + "${userHeight?.answer}" + " " + if (userHeight?.note.isNullOrEmpty()) "" else "(${userHeight?.note})"
        binding.content?.weight?.text =
            "الوزن: " + "${userWeight?.answer}" + " " + if (userWeight?.note.isNullOrEmpty()) "" else "(${userWeight?.note})"
        binding.content?.skin?.text =
            "لون البشرة: " + "${userSkin?.answer}" + " " + if (userSkin?.note.isNullOrEmpty()) "" else "(${userSkin?.note})"
        binding.content?.state?.text =
            "المحافظة ومكان السكن: " + "${userStateAndLocation?.answer}" + " " + if (userStateAndLocation?.note.isNullOrEmpty()) "" else "(${userStateAndLocation?.note})"
        //Islamic Info
        binding.content?.pray?.text =
            "مدى الالتزام بالصلاة: " + "${userPray?.answer}" +
                    " " +
                    if (userPray?.note.isNullOrEmpty()) "" else "(${userPray?.note})"
        binding.content?.quran?.text =
            "مقدار حفظ القران: " + "${userQuran?.answer}" +
                    " " +
                    if (userQuran?.note.isNullOrEmpty()) "" else "(${userQuran?.note})"

        binding.content?.music?.text =
            "هل تشاهد/ـي الأفلام او تستمع/ـين إلى الموسيقى أو الأغاني ؟: " + "${userMusic?.answer}" +
                    " " +
                    if (userMusic?.note.isNullOrEmpty()) "" else "(${userMusic?.note})"
        binding.content?.a7zab?.text =
            "هل لديك أي انتمائات دينية؟: " + "${userA7zab?.answer}" +
                    " " +
                    if (userA7zab?.note.isNullOrEmpty()) "" else "(${userA7zab?.note})"
        binding.content?.certificate?.text =
            "المؤهل/الدراسة: " + "${userCertificate?.answer}" +
                    " " +
                    if (userCertificate?.note.isNullOrEmpty()) "" else "(${userCertificate?.note})"
        //Additional Info
        binding.content?.job?.text =
            "الوظيفة: " + "${userJob?.answer}" +
                    " " +
                    if (userJob?.note.isNullOrEmpty()) "" else "(${userJob?.note})"
        binding.content?.fathersJob?.text =
            "عمل الوالد: " + "${userFathersJob?.answer}" +
                    " " +
                    if (userFathersJob?.note.isNullOrEmpty()) "" else "(${userFathersJob?.note})"
        binding.content?.mothersJob?.text =
            "عمل الوالدة: " + "${userMothersJob?.answer}" +
                    " " +
                    if (userMothersJob?.note.isNullOrEmpty()) "" else "(${userMothersJob?.note})"
        binding.content?.brothers?.text =
            "عدد الإخوة والأخوات وأعمارهم ومؤهلاتهم؟: " + "${userBrothers?.answer}" +
                    " " +
                    if (userBrothers?.note.isNullOrEmpty()) "" else "(${userBrothers?.note})"
        //About you
        binding.content?.talkAboutYourself?.text =
            "تكلم عن نفسك أو ما يقوله الناس عنك: " + "${userTalkAboutYourself?.answer}" +
                    " " +
                    if (userTalkAboutYourself?.note.isNullOrEmpty()) "" else "(${userTalkAboutYourself?.note})"
        binding.content?.yourPartner?.text =
            "ما هي المواصفات التي تريدها في شريك/ـة حياتك؟: " + "${userYourPartner?.answer}" +
                    " " +
                    if (userYourPartner?.note.isNullOrEmpty()) "" else "(${userYourPartner?.note})"

        if (signedInUser?.gender == "man") {
            binding.content?.sport?.text =
                "هل أنت رياضي؟: " + "${userSport?.answer}" +
                        " " +
                        if (userSport?.note.isNullOrEmpty()) "" else "(${userSport?.note})"
            binding.content?.smoking?.text =
                "هل أنت مدخن؟: " + "${userSmoking?.answer}" +
                        " " +
                        if (userSmoking?.note.isNullOrEmpty()) "" else "(${userSmoking?.note})"
            binding.content?.le7ya?.text =
                "هل أنت ملتحي؟: " + "${userLe7ya?.answer}" +
                        " " +
                        if (userLe7ya?.note.isNullOrEmpty()) "" else "(${userLe7ya?.note})"
        } else {
            binding.content?.le7ya?.text =
                "نوع الحجاب: " + "${userHijab?.answer}" +
                        " " +
                        if (userHijab?.note.isNullOrEmpty()) "" else "(${userHijab?.note})"
            binding.content?.sport?.text = ""
            binding.content?.smoking?.text = ""
        }
    }

    private fun goToQuestionsScreen() {
        this.findNavController()
            .navigate(R.id.action_navigation_profile_to_questionsFragment,
                null,
                navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })

    }

    private fun showTermServicesDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_term_of_services)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<View>(R.id.bt_accept) as Button).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}