package com.muslims.firebasemvvm.ui.why_marry

import android.os.Bundle
import android.text.SpannableStringBuilder
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.ActivityWhyMarryBinding

class WhyMarryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhyMarryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWhyMarryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}