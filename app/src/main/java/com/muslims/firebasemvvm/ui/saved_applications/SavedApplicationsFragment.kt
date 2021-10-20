package com.muslims.firebasemvvm.ui.saved_applications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentDashboardBinding
import com.muslims.firebasemvvm.databinding.FragmentSavedApplicationsBinding
import com.muslims.firebasemvvm.ui.profile.ProfileViewModel

class SavedApplicationsFragment : Fragment() {
    private lateinit var viewModel: SavedApplicationsViewModel
    private var _binding: FragmentSavedApplicationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel =
            ViewModelProvider(this).get(SavedApplicationsViewModel::class.java)

        _binding = FragmentSavedApplicationsBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }


}