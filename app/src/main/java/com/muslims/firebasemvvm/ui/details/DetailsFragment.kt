package com.muslims.firebasemvvm.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.DetailsFragmentBinding
import com.muslims.firebasemvvm.databinding.FragmentHomeBinding
import com.muslims.firebasemvvm.models.User

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel

    private var _binding: DetailsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        val user = arguments?.getParcelable<User>("user")

        return  root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}