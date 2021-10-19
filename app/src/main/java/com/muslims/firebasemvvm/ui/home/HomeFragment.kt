package com.muslims.firebasemvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentHomeBinding
import com.muslims.firebasemvvm.models.User

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val usersRvAdapter =
            UsersRvAdapter(listOf(), UsersRvAdapter.Listener { user ->
                onUserRvItemClicked(user)
            })

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersRvAdapter
        }

        homeViewModel.users.observe(viewLifecycleOwner, Observer { usersList ->
            usersRvAdapter.users = usersList
            usersRvAdapter.notifyDataSetChanged()
        })

        homeViewModel.status.observe(viewLifecycleOwner, Observer {status ->
            when(status){
                FireStoreStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.usersRecyclerView.visibility = View.INVISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.usersRecyclerView.visibility = View.VISIBLE
                }
                FireStoreStatus.ERROR -> {

                }
            }
        })

        return root
    }

    private fun onUserRvItemClicked(user:User) {
        var userBundle = bundleOf("user" to user)
        this.findNavController().navigate(R.id.action_navigation_home_to_detailsFragment,
            userBundle,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.anim.slide_in_left
                    exit = android.R.anim.slide_out_right
                }
            }
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}