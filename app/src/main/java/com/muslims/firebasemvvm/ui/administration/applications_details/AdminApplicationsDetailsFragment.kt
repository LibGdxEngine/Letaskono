package com.muslims.firebasemvvm.ui.administration.applications_details

import android.R.attr.label
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentAdminApplicationsDetailsBinding
import com.muslims.firebasemvvm.models.GENERAL_STATUS
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.user_details.UpdatingStatus
import com.muslims.firebasemvvm.utils.ProfileBuilder


class AdminApplicationsDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AdminApplicationsDetailsFragment()
    }

    private var _binding: FragmentAdminApplicationsDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var userAlreadyInFavourites = false

    private lateinit var viewModel: AdminApplicationsDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminApplicationsDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(AdminApplicationsDetailsViewModel::class.java)

        val user = arguments?.getParcelable<User>("user")
        val signedInUser = arguments?.getParcelable<User>("signedInUser")

        val favouritesList = signedInUser?.favourites?.split(",")

        if (favouritesList != null) {
            if (favouritesList.contains(user?.id)) {
                binding.favourite.setImageResource(R.drawable.ic_baseline_bookmark_added_24)
                userAlreadyInFavourites = true
            } else {
                binding.favourite.setImageResource(R.drawable.ic_outline_bookmark_add_24)
                userAlreadyInFavourites = false
            }
        }

        binding.btnShowHideActions.setOnClickListener {
            val isShown = binding.actionsContainer.visibility == View.VISIBLE
            if (isShown) {
                binding.actionsContainer.visibility = View.GONE
            } else {
                binding.actionsContainer.visibility = View.VISIBLE
            }
        }

        if (user?.isBlocked == true) {
            binding.btnBlock.text = "إلغاء الحظر"
        }

        binding.btnBlock.setOnClickListener {
            showBlockMessage(context!!, user!!);
        }

        binding.btnHistory.setOnClickListener {
            showInfoDialog(user!!)
        }

        if (user?.generalStatus == GENERAL_STATUS.WANT_SEE.toString()) {
            binding.btnChangeStatus1.setText("تحويل الى راغب في الزواج")
        } else if (user?.generalStatus == GENERAL_STATUS.ALREADY_MARRIED.toString()) {
            binding.btnChangeStatus1.visibility = View.GONE
            binding.btnChangeStatus2.visibility = View.GONE
        }

        binding.btnChangeStatus1.setOnClickListener {
            if (user?.generalStatus == GENERAL_STATUS.WANT_SEE.toString()) {
                showConvertStatusToWantToMarryDialog(context!!, user)
            } else {
                showConvertStatusToWantToSeeDialog(user!!)
            }
        }

        binding.btnChangeStatus2.setOnClickListener {
            showConvertStatusToAlreadyMarriedDialog(user!!)
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

        viewModel.removingStatus.observe(viewLifecycleOwner, Observer {
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

        viewModel.status.observe(viewLifecycleOwner, Observer {
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
                    viewModel.removeFromFavourite(
                        signedInUser,
                        user?.id!!
                    )
                } else {
                    viewModel.addUserToFavourites(
                        signedInUser,
                        user?.id!!
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
                var message = """
                السلام عليكم ورحمة الله وبركاته 
            """.trimIndent()
                setClickToChat(it, "+20" + user!!.phone, message)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminApplicationsDetailsViewModel::class.java)
    }

    private fun showBlockMessage(ctx: Context, user: User) {
        if (user.isBlocked) {
            MaterialAlertDialogBuilder(ctx)
                .setTitle("هل تريد إلغاء حظر هذا الشخص فعلا ؟")
                .setMessage("")
                .setPositiveButton(
                    "تراجع"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .setNegativeButton(
                    "إلغاء الحظر"
                ) { dialogInterface, i ->
                    viewModel.blockUser(user.phone, user)
                }
                .show()
        } else {
            MaterialAlertDialogBuilder(ctx)
                .setTitle("هل تريد حظر هذا الشخص فعلا ؟")
                .setMessage("إذا حظرته فلن يتمكن من دخول التطبيق مجددا")
                .setPositiveButton(
                    "تراجع"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .setNegativeButton(
                    "حظر"
                ) { dialogInterface, i ->
                    viewModel.blockUser(user.phone, user)
                }
                .show()
        }

    }

    private fun showInfoDialog(user: User) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_user_info)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val btnUpdateHistory: Button = dialog.findViewById(R.id.btnUpdateHistory)

        val relatedWithText: TextView = dialog.findViewById(R.id.relatedWithText)
        val relatedWithCode: TextView = dialog.findViewById(R.id.relatedWithCode)

        if (user.relatedWith.isBlank() || user.relatedWith.trim().isEmpty()) {
            relatedWithText.visibility = View.GONE
            relatedWithCode.visibility = View.GONE
        } else {
            relatedWithText.visibility = View.VISIBLE
            relatedWithCode.visibility = View.VISIBLE
            if (user.generalStatus == GENERAL_STATUS.ALREADY_MARRIED.toString()) {
                relatedWithText.text = "كود الزوجة"
                relatedWithText.setOnClickListener {
                    val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", user.relatedWith)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(context, "تم نسخ الكود", Toast.LENGTH_SHORT).show()
                }
                relatedWithCode.setOnClickListener {
                    val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", user.relatedWith)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(context, "تم نسخ الكود", Toast.LENGTH_SHORT).show()
                }
            }
            relatedWithCode.setText(user.relatedWith)
        }

        val historyInfoEditText: EditText =
            (dialog.findViewById<View>(R.id.infoHistory) as EditText)
        historyInfoEditText.setText(user.historyInfo)
        historyInfoEditText.addTextChangedListener {
            if (it.toString().trim() != user.historyInfo) {
                btnUpdateHistory.visibility = View.VISIBLE
            } else {
                btnUpdateHistory.visibility = View.INVISIBLE
            }
        }

        btnUpdateHistory.setOnClickListener {
            var text: String = historyInfoEditText.text.toString()
            Toast.makeText(context, "${text}", Toast.LENGTH_SHORT).show()
            user.historyInfo = text
            viewModel.updateHistoryInfo(user)
            btnUpdateHistory.visibility = View.INVISIBLE
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showConvertStatusToWantToSeeDialog(currentUser: User) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_convert_status)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val btnSearch: Button = dialog.findViewById(R.id.btnSearch)
        val searchResult: TextView = dialog.findViewById(R.id.searchResult)
        val searchProgressBar: ProgressBar = dialog.findViewById(R.id.searchProgressBar)
        val searchView: SearchView =
            (dialog.findViewById<View>(R.id.searchView) as SearchView)

        btnSearch.setOnClickListener {
            val searchText = searchView.query
            viewModel.getUserByMobileOrCode(searchText.toString(), currentUser.gender)
        }

        viewModel.searchStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                SearchStatus.LOADING -> {
                    searchProgressBar.visibility = View.VISIBLE
                    btnSearch.isActivated = false
                }
                SearchStatus.ERROR -> {
                    searchProgressBar.visibility = View.GONE
                    btnSearch.isActivated = true
                }
                SearchStatus.DONE -> {
                    searchProgressBar.visibility = View.GONE
                    btnSearch.isActivated = true
                }
            }
        })

        viewModel.relateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                RelateStatus.LOADING -> {

                }
                RelateStatus.SAME_GENDER -> {
                    Toast.makeText(
                        context,
                        "لايمكنك ربط رجلين ببعض... أستغفر الله العظيم !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                RelateStatus.ALREADY_MARRIED -> {
                    Toast.makeText(context, "هذا الشخص تزوج بالفعل !!", Toast.LENGTH_SHORT).show()
                }
                RelateStatus.ALREADY_RELATED -> {
                    Toast.makeText(context, "هذا الحساب يتواصل مع شخص أخر الان", Toast.LENGTH_SHORT)
                        .show()
                }
                RelateStatus.ERROR -> {
                    Toast.makeText(context, "لا يمكن ربط هذين العروسين", Toast.LENGTH_SHORT).show()
                }
                RelateStatus.DONE -> {
                    dialog.dismiss()
                    Toast.makeText(context, "تم ربط العريسين بنجاح", Toast.LENGTH_SHORT).show()
                }

            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                searchResult.setOnClickListener {
                    viewModel.relate(currentUser, user)
                }
                var isMale = user.gender == "Male"
                var nikName = if (isMale) "عريس" else "عروسة"
                searchResult.visibility = View.VISIBLE
                searchResult.text = nikName + " " + " كود " + "${user.id}"
            }
        })

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showConvertStatusToWantToMarryDialog(context: Context, currentUser: User) {
        MaterialAlertDialogBuilder(context)
            .setTitle("هل تريد تحويل هذا الشخص الى راغب في الزواج؟")
            .setMessage("")
            .setOnDismissListener {
                Toast.makeText(
                    context,
                    "تم تحويل هذا الشخص الى راغب في الزواج والغاء تواصله مع العروسة",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setPositiveButton(
                "تراجع"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .setNegativeButton(
                "تحويل"
            ) { dialogInterface, i ->
                viewModel.convertToWantToMarry(currentUser)
            }
            .show()
    }

    private fun showConvertStatusToAlreadyMarriedDialog(currentUser: User) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_convert_status)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val btnSearch: Button = dialog.findViewById(R.id.btnSearch)
        val searchResult: TextView = dialog.findViewById(R.id.searchResult)
        val searchProgressBar: ProgressBar = dialog.findViewById(R.id.searchProgressBar)
        val searchView: SearchView =
            (dialog.findViewById<View>(R.id.searchView) as SearchView)
        searchView.queryHint = "من هي زوجته ؟"

        btnSearch.setOnClickListener {
            val searchText = searchView.query
            viewModel.getUserByMobileOrCode(searchText.toString(), currentUser.gender)
        }

        viewModel.searchStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                SearchStatus.LOADING -> {
                    searchProgressBar.visibility = View.VISIBLE
                    btnSearch.isActivated = false
                }
                SearchStatus.ERROR -> {
                    searchProgressBar.visibility = View.GONE
                    btnSearch.isActivated = true
                }
                SearchStatus.DONE -> {
                    searchProgressBar.visibility = View.GONE
                    btnSearch.isActivated = true
                }
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                searchResult.setOnClickListener {
                    viewModel.marry(currentUser, user)
                }
                var isMale = user.gender == "Male"
                var nikName = if (isMale) "عريس" else "عروسة"
                searchResult.visibility = View.VISIBLE
                searchResult.text = nikName + " " + " كود " + "${user.id}"
            }
        })

        dialog.show()
        dialog.window!!.attributes = lp
    }

}