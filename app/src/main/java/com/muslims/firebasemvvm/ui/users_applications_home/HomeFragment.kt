package com.muslims.firebasemvvm.ui.users_applications_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    lateinit var usersRvAdapter: UsersRvAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var observer: Observer<List<User>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersRvAdapter = UsersRvAdapter(listOf(), UsersRvAdapter.Listener { user ->
            onUserRvItemClicked(user)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.swipToRefreshLayout.setOnRefreshListener {
            homeViewModel.getAllUsers()
            binding.swipToRefreshLayout.isRefreshing = false
        }

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersRvAdapter
        }

        observer = Observer<List<User>> { usersList ->
            usersRvAdapter.users = usersList
            usersRvAdapter.notifyDataSetChanged()
        }

        homeViewModel.users.observe(viewLifecycleOwner , observer)

        homeViewModel.status.observe(viewLifecycleOwner, Observer {status ->
            when(status){
                FireStoreStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
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