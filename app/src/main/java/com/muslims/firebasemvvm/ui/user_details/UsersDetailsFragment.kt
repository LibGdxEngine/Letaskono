package com.muslims.firebasemvvm.ui.user_details

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.DetailsFragmentBinding
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.utils.ProfileBuilder
import kotlin.math.sign


class UsersDetailsFragment : Fragment() {

    private lateinit var viewModelUsers: UsersDetailsViewModel

    private var _binding: DetailsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var userAlreadyInFavourites = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelUsers = ViewModelProvider(this).get(UsersDetailsViewModel::class.java)

        val user = arguments?.getParcelable<User>("user")
        val signedInUser = arguments?.getParcelable<User>("signedInUser")

        val favouritesList = signedInUser?.favourites?.split(",")

        if (favouritesList != null) {
            if (favouritesList.contains(user?.phone)) {
                binding.favourite.setImageResource(R.drawable.ic_baseline_bookmark_added_24)
                userAlreadyInFavourites = true
            } else {
                binding.favourite.setImageResource(R.drawable.ic_outline_bookmark_add_24)
                userAlreadyInFavourites = false
            }
        }

        binding.progressBar?.visibility = View.GONE
        binding.signedInUserContainesr?.visibility = View.VISIBLE
        binding.appBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.height / 1.5f < -verticalOffset) {
                binding.image?.setVisibility(View.GONE)
            } else {
                binding.image?.setVisibility(View.VISIBLE)
            }
        })
        bindUserInfo(user)


        viewModelUsers.removingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                UpdatingStatus.LOADING -> {

                }
                UpdatingStatus.ERROR -> {

                }
                UpdatingStatus.DONE -> {
                    binding.favourite.setImageResource(R.drawable.ic_outline_bookmark_add_24)
                    userAlreadyInFavourites = false
                }
            }
        })

        viewModelUsers.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                UpdatingStatus.LOADING -> {

                }
                UpdatingStatus.ERROR -> {

                }
                UpdatingStatus.DONE -> {
                    binding.favourite.setImageResource(R.drawable.ic_baseline_bookmark_added_24)
                    userAlreadyInFavourites = true
                }
            }
        })

        binding.favourite.setOnClickListener {
            if (signedInUser != null) {
                if (userAlreadyInFavourites) {
                    var list = signedInUser.favourites!!.split(",").toMutableList()
                    list.remove(user!!.phone)
                    val newFavourites = list.joinToString(",")
                    signedInUser.favourites = newFavourites
                    viewModelUsers.removeFromFavourite(
                        signedInUser.phone,
                        signedInUser
                    )
                } else {
                    signedInUser.favourites += user!!.phone + ","
                    viewModelUsers.addUserToFavourites(
                        signedInUser.phone,
                        signedInUser
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "قم بتسجيل حسابك حتى تستطيع حفظ الإستمارة",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.report.setOnClickListener {
            if (signedInUser == null) {
                Toast.makeText(
                    context,
                    "يجب عليك تسجيل استمارتك لتستطيع التبليغ عن هذه الاستمارة",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showReportDialog(context!!, user!!, signedInUser!!, (it as MaterialButton))
            }

        }

        binding.share.setOnClickListener {
            val profile = ProfileBuilder().build(user!!)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, profile.trimIndent()
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.action.setOnClickListener {
            if (signedInUser != null) {
                var gender = if (signedInUser!!.gender == "man") "العريس" else "العروسة"
                var otherGender = if (user!!.gender == "man") "العريس" else "العروسة"
                var message = """
                السلام عليكم ورحمة الله وبركاته 
                أنا ${gender} رقم ${signedInUser.id}
                أريد التواصل مع ${otherGender} رقم ${user.id}
            """.trimIndent()
                if(signedInUser.gender == "man"){
                    setClickToChat(it, getString(R.string.admin_phone_number), message)
                }else{
                    setClickToChat(it, getString(R.string.woman_admin_phone_number), message)
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "قم بتسجيل حسابك حتى تستطيع التواصل",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

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

    private fun showReportDialog(
        context: Context,
        reportedUserId: User,
        reporterId: User,
        view: MaterialButton
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("هل تريد التبليغ عن استمارة هذا الشخص بأنها مخالفة؟")
            .setMessage("")
            .setPositiveButton(
                "تراجع"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .setNegativeButton(
                "تبليغ"
            ) { dialogInterface, i ->
                viewModelUsers.report(reportedUserId, reporterId)
                Toast.makeText(
                    context,
                    "تم الابلاغ عن هذا الشخص وسيتم مراجعة استمارته ..شكرا لك",
                    Toast.LENGTH_SHORT
                ).show()
                dialogInterface.dismiss()
                view.visibility = View.GONE
            }
            .show()
    }

    private fun bindUserInfo(signedInUser: User?) {
        var title = if (signedInUser?.gender == "man") "عريس" else "عروسة"
        var userLe7ya: Map<String, String>? = null
        var userSport: Map<String, String>? = null
        var userSmoking: Map<String, String>? = null
        var userStatus: Map<String, String>? = null
        var userHijab: Map<String, String>? = null
        if (signedInUser?.gender == "man") {
            binding.profileBackground.setImageResource(R.drawable.man_background)
            if (signedInUser.questionsList?.first { it["id"] == "23" }?.get("answer") == "نعم") {
                binding.image.setImageResource(R.drawable.man_with_lehya)
            } else {
                binding.image.setImageResource(R.drawable.man_without_lehya)
            }
            title += " ${if (signedInUser?.questionsList?.first { it["id"] == "23" }?.get("answer") == "نعم") "ملتحي" else " "}"
            userLe7ya = signedInUser?.questionsList?.first { it["id"] == "23" }
//            userSport = signedInUser?.questionsList?.first { it["id"] == "20" }
            userSmoking = signedInUser?.questionsList?.first { it["id"] == "21" }
            userStatus = signedInUser?.questionsList?.first { it["id"] == "19" }
        } else {
            binding.profileBackground.setImageResource(R.drawable.woman_background)
            when (signedInUser?.questionsList?.first { it["id"] == "24" }?.get("answer")) {
                "منتقبة" -> {
                    binding.image.setImageResource(R.drawable.women_with_neqab)
                }
                "مختمرة" -> {
                    binding.image.setImageResource(R.drawable.women_with_khemar)
                }
                else -> {
                    binding.image.setImageResource(R.drawable.women_with_hejab)
                }
            }
            title += " ${if (signedInUser?.questionsList?.first { it["id"] == "24" }?.get("answer") == "منتقبة") "منتقبة" else " "}"
            userHijab = signedInUser?.questionsList?.first { it["id"] == "24" }
            userStatus = signedInUser?.questionsList?.first { it["id"] == "22" }
        }
        //General Info
//        val userCity = signedInUser?.questionsList?.first { it["id"] == "26" }
        val userAge = signedInUser?.questionsList?.first { it["id"] == "2" }
        val userNationality = signedInUser?.questionsList?.first { it["id"] == "3" }
        val userHeight = signedInUser?.questionsList?.first { it["id"] == "26" }
        val userWeight = signedInUser?.questionsList?.first { it["id"] == "6" }
        val userSkin = signedInUser?.questionsList?.first { it["id"] == "7" }
        val userStateAndLocation = signedInUser?.questionsList?.first { it["id"] == "11" }
        //Islamic Info
        val userPray = signedInUser?.questionsList?.first { it["id"] == "1" }
//        val userQuran = signedInUser?.questionsList?.first { it["id"] == "15" }
//        val userMusic = signedInUser?.questionsList?.first { it["id"] == "16" }
//        val userA7zab = signedInUser?.questionsList?.first { it["id"] == "17" }
        //Additional Info
        val userCertificate = signedInUser?.questionsList?.first { it["id"] == "8" }
        val userJob = signedInUser?.questionsList?.first { it["id"] == "9" }
        val userFathersJob = signedInUser?.questionsList?.first { it["id"] == "12" }
        val userMothersJob = signedInUser?.questionsList?.first { it["id"] == "13" }
//        val userBrothers = signedInUser?.questionsList?.first { it["id"] == "14" }
        //About you
        val userTalkAboutYourself = signedInUser?.questionsList?.first { it["id"] == "4" }
        val userYourPartner = signedInUser?.questionsList?.first { it["id"] == "5" }


        binding.title?.text = title
        binding.content?.userCode?.text = "كود: " + signedInUser?.id
        binding.age?.text = "${userAge?.get("answer")}"

        //General Info
        binding.content?.userAge?.text =
            "السن: " + "${userAge?.get("answer")}" + " " + if (userAge?.get("note").isNullOrEmpty()) "" else "(${userAge?.get("note")})"
        binding.content?.status?.text =
            "الحالة الاجتماعية: " + "${userStatus?.get("answer")}" + " " + if (userStatus?.get("note").isNullOrEmpty()) "" else "(${userAge?.get("note")})"
        binding.content?.nationality?.text =
            "الجنسية: " + "${userNationality?.get("answer")}" + " " + if (userNationality?.get("note").isNullOrEmpty()) "" else "(${userNationality?.get("note")})"

        binding.content?.height?.text =
            "الطول: " + "${userHeight?.get("answer")}" + " " + if (userHeight?.get("note").isNullOrEmpty()) "" else "(${userHeight?.get("note")})"
        binding.content?.weight?.text =
            "الوزن: " + "${userWeight?.get("answer")}" + " " + if (userWeight?.get("note").isNullOrEmpty()) "" else "(${userWeight?.get("note")})"
        binding.content?.skin?.text =
            "لون البشرة: " + "${userSkin?.get("answer")}" + " " + if (userSkin?.get("note").isNullOrEmpty()) "" else "(${userSkin?.get("note")})"
        binding.content?.state?.text =
            "المحافظة ومكان السكن: " + "${userStateAndLocation?.get("answer")}" + " " +
                    if (userStateAndLocation?.get("note").isNullOrEmpty()) "" else "(${userStateAndLocation?.get("note")})"
        //Islamic Info
        binding.content?.pray?.text =
            "مدى الالتزام بالصلاة: " + "${userPray?.get("answer")}" +
                    " " +
                    if (userPray?.get("note").isNullOrEmpty()) "" else "(${userPray?.get("note")})"
//        binding.content?.quran?.text =
//            "مقدار حفظ القران: " + "${userQuran?.get("answer")}" +
//                    " " +
//                    if (userQuran?.get("note").isNullOrEmpty()) "" else "(${userQuran?.get("note")})"
//
//        binding.content?.music?.text =
//            "هل تشاهد/ـي الأفلام او تستمع/ـين إلى الموسيقى أو الأغاني ؟: " + "${userMusic?.get("answer")}" +
//                    " " +
//                    if (userMusic?.get("note").isNullOrEmpty()) "" else "(${userMusic?.get("note")})"
//        binding.content?.a7zab?.text =
//            "هل لديك أي انتمائات دينية؟: " + "${userA7zab?.get("answer")}" +
//                    " " +
//                    if (userA7zab?.get("note").isNullOrEmpty()) "" else "(${userA7zab?.get("note")})"
        binding.content?.certificate?.text =
            "المؤهل/الدراسة: " + "${userCertificate?.get("answer")}" +
                    " " +
                    if (userCertificate?.get("note").isNullOrEmpty()) "" else "(${userCertificate?.get("note")})"
        //Additional Info
        binding.content?.job?.text =
            "الوظيفة: " + "${userJob?.get("answer")}" +
                    " " +
                    if (userJob?.get("note").isNullOrEmpty()) "" else "(${userJob?.get("note")})"
        binding.content?.fathersJob?.text =
            "عمل الوالد: " + "${userFathersJob?.get("answer")}" +
                    " " +
                    if (userFathersJob?.get("note").isNullOrEmpty()) "" else "(${userFathersJob?.get("note")})"
        binding.content?.mothersJob?.text =
            "عمل الوالدة: " + "${userMothersJob?.get("answer")}" +
                    " " +
                    if (userMothersJob?.get("note").isNullOrEmpty()) "" else "(${userMothersJob?.get("note")})"
//        binding.content?.brothers?.text =
//            "عدد الإخوة والأخوات وأعمارهم ومؤهلاتهم؟: " + "${userBrothers?.get("answer")}" +
//                    " " +
//                    if (userBrothers?.get("note").isNullOrEmpty()) "" else "(${userBrothers?.get("note")})"
        //About you
        binding.content?.talkAboutYourself?.text =
            "تكلم عن نفسك أو ما يقوله الناس عنك: " + "${userTalkAboutYourself?.get("answer")}" +
                    " " +
                    if (userTalkAboutYourself?.get("note").isNullOrEmpty()) "" else "(${userTalkAboutYourself?.get("note")})"
        binding.content?.yourPartner?.text =
            "ما هي المواصفات التي تريدها في شريك/ـة حياتك؟: " + "${userYourPartner?.get("answer")}" +
                    " " +
                    if (userYourPartner?.get("note").isNullOrEmpty()) "" else "(${userYourPartner?.get("note")})"

        if (signedInUser?.gender == "man") {
            binding.content?.sport?.text =
                "هل أنت رياضي؟: " + "${userSport?.get("answer")}" +
                        " " +
                        if (userSport?.get("note").isNullOrEmpty()) "" else "(${userSport?.get("note")})"
            binding.content?.smoking?.text =
                "هل أنت مدخن؟: " + "${userSmoking?.get("answer")}" +
                        " " +
                        if (userSmoking?.get("note").isNullOrEmpty()) "" else "(${userSmoking?.get("note")})"
            binding.content?.le7ya?.text =
                "هل أنت ملتحي؟: " + "${userLe7ya?.get("answer")}" +
                        " " +
                        if (userLe7ya?.get("note").isNullOrEmpty()) "" else "(${userLe7ya?.get("note")})"
        } else {
            binding.content?.le7ya?.text =
                "نوع الحجاب: " + "${userHijab?.get("answer")}" +
                        " " +
                        if (userHijab?.get("note").isNullOrEmpty()) "" else "(${userHijab?.get("note")})"
            binding.content?.sport?.text = ""
            binding.content?.smoking?.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}