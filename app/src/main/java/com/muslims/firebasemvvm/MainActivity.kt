package com.muslims.firebasemvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.muslims.firebasemvvm.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.initialize(this)

        val binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        val viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.addUser()
            print("asd")
        }
        viewModel.userProfile.observe(this, Observer {
//            print("The name is ${it.name}")
//            binding.textHello.text = it.name ?: "null"
//            print("Value is " + (it == null))
        })
        viewModel.posts.observe(this, Observer {

        })
    }
}