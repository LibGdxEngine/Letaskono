package com.muslims.firebasemvvm.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muslims.firebasemvvm.AuthenticationStatus
import com.muslims.firebasemvvm.HomeActivity
import com.muslims.firebasemvvm.HomeActivityViewModel
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.utils.AnimatedTextView

class SplashActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel =
            ViewModelProvider(this).get(SplashViewModel::class.java)


        setContentView(R.layout.activity_splash)

//        splashViewModel.getAllUsersFromFirebase()
//
//        splashViewModel.fireUsers.observe(this, Observer { users ->
//            Toast.makeText(this, "${users.size}", Toast.LENGTH_SHORT)
//                .show()
//            for (user: User in users) {
//                if(user.questionsList != null){
//                    splashViewModel.createNewUser(user)
//                    Log.d("USERS", user.phone)
//                }
//            }
//
//        })
//        splashViewModel.createNewUser(User("", name = "Loay fathy"))
//        splashViewModel.updateUser(
//            "01019867911",
//            User("", name = "ahmed fathy", isBlocked = true)
//        )
//        splashViewModel.getUserByPhone("01092203739")
//
//        splashViewModel.users.observe(this, Observer {
//            var myUsers = it.users!!.toMutableList()
//
//
//        })
//
//        splashViewModel.userModel.observe(this, Observer {
//
//            Toast.makeText(this, "${it.user!!.questionsList!!.isEmpty()}", Toast.LENGTH_SHORT).show()
//
//        })
//
//
//
//        splashViewModel.status.observe(this, Observer {
//            when (it) {
//                GettingUsersStatus.LOADING -> {
//                    Toast.makeText(this, "تحميل", Toast.LENGTH_SHORT).show()
//                }
//                GettingUsersStatus.DONE -> {
//                    Toast.makeText(this, "تم", Toast.LENGTH_SHORT).show()
//                }
//                GettingUsersStatus.ERROR -> {
//                    Toast.makeText(this, "خطأ", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val textViewLogo = findViewById<AnimatedTextView>(R.id.textViewLogo)

        textViewLogo.setCharacterDelay(150)
        textViewLogo.animateText(getString(R.string.app_name));

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}