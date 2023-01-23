package com.muslims.firebasemvvm.ui.administration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentAdminBinding
import com.muslims.firebasemvvm.models.GENERAL_STATUS
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.users_applications_home.HomeViewModel

class AdminFragment : Fragment() {

    companion object {
        fun newInstance() = AdminFragment()
    }

    private var _binding: FragmentAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(AdminViewModel::class.java)
        _binding = FragmentAdminBinding.inflate(inflater, container, false)

        binding.btnWantMarry.setOnClickListener {
            onBtnClicked(GENERAL_STATUS.WANT_MARRY.toString())
        }

        binding.btnSee.setOnClickListener {
            onBtnClicked(GENERAL_STATUS.WANT_SEE.toString())
        }

        binding.btnAlreadyMarried.setOnClickListener {
            onBtnClicked(GENERAL_STATUS.ALREADY_MARRIED.toString())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onBtnClicked(filter: String) {
        var userBundle = bundleOf("query" to filter)
        this.findNavController().currentDestination?.getAction(R.id.action_adminFragment_to_adminApplicationsListFragment)
            ?.let {
                this.findNavController().navigate(
                    R.id.action_adminFragment_to_adminApplicationsListFragment,
                    userBundle,
                    navOptions { // Use the Kotlin DSL for building NavOptions
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    }
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}