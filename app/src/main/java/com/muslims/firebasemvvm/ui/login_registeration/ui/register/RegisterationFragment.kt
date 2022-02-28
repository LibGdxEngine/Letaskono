package com.muslims.firebasemvvm.ui.login_registeration.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.AdviceDetailsFragmentBinding
import com.muslims.firebasemvvm.databinding.RegisterationFragmentBinding

class RegisterationFragment : Fragment() {

    private lateinit var viewModel: RegisterationViewModel
    private var _binding: RegisterationFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterationFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}