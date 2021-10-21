package com.muslims.firebasemvvm.ui.user_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muslims.firebasemvvm.databinding.DetailsFragmentBinding
import com.muslims.firebasemvvm.models.User

class UsersDetailsFragment : Fragment() {

    private lateinit var viewModelUsers: UsersDetailsViewModel

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
        viewModelUsers = ViewModelProvider(this).get(UsersDetailsViewModel::class.java)

        val user = arguments?.getParcelable<User>("user")

        return  root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}