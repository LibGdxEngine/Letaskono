package com.muslims.firebasemvvm.ui.users_applications_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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
import com.muslims.firebasemvvm.utils.AppRater
import com.muslims.firebasemvvm.utils.StoredAuthUser


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var usersRvAdapter: UsersRvAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var signedInUser: User? = null
    var selectedUsersList: MutableList<User> = mutableListOf()
    var allUsersList: MutableList<User> = mutableListOf()

    private lateinit var observer: Observer<List<User>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersRvAdapter = UsersRvAdapter(listOf(), UsersRvAdapter.Listener { user ->
            onUserRvItemClicked(user, signedInUser)
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

//        AppRater.app_launched(requireContext())

        binding.swipToRefreshLayout.setOnRefreshListener {
            homeViewModel.getAllUsers()
            binding.swipToRefreshLayout.isRefreshing = false
        }

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersRvAdapter
        }

        val currentUserId = StoredAuthUser.getUser(requireContext())

        observer = Observer<List<User>> { usersList ->
            if (!currentUserId.isNullOrEmpty()) {
                signedInUser = usersList.first { it.id == currentUserId }
                if (signedInUser!!.isBlocked) {
                    Toast.makeText(requireContext(), "أنت محظور", Toast.LENGTH_SHORT).show()
                    requireActivity().finishAndRemoveTask()
                }

                allUsersList = usersList.toMutableList()
                allUsersList.remove(signedInUser)

                if (signedInUser?.gender == "man") {
                    binding.chipWomen.isChecked = true
                    binding.chipWomen.isEnabled = false
                    binding.chipMen.isChecked = false
                    binding.chipMen.isEnabled = true
                } else {
                    binding.chipWomen.isChecked = false
                    binding.chipWomen.isEnabled = true
                    binding.chipMen.isChecked = true
                    binding.chipMen.isEnabled = false

                }
                if (binding.chipMen.isChecked && binding.chipWomen.isChecked) {
                    //no need to filter anything here
                } else if (binding.chipMen.isChecked) {
                    selectedUsersList =
                        allUsersList.filter { user -> user.gender == "man" }.toMutableList()
                } else if (binding.chipWomen.isChecked) {
                    selectedUsersList =
                        allUsersList.filter { user -> user.gender == "woman" }.toMutableList()
                }
            } else {
                selectedUsersList = usersList.toMutableList()
                binding.chipWomen.isChecked = true
                binding.chipMen.isChecked = true
                binding.chipWomen.isEnabled = false
                binding.chipMen.isEnabled = false
            }
            usersRvAdapter.users = selectedUsersList
            usersRvAdapter.notifyDataSetChanged()
        }

        binding.chipMen.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { chip, isChecked ->

            if (isChecked) {
                selectedUsersList.addAll(allUsersList.filter { user -> user.gender == "man" }
                    .toMutableList())
            } else {
                selectedUsersList.removeAll(allUsersList.filter { user -> user.gender == "man" }
                    .toMutableList())
            }
            usersRvAdapter.users = selectedUsersList
            usersRvAdapter.notifyDataSetChanged()
        })

        binding.chipWomen.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                selectedUsersList.addAll(allUsersList.filter { user -> user.gender == "woman" }
                    .toMutableList())
            } else {
                selectedUsersList.removeAll(allUsersList.filter { user -> user.gender == "woman" }
                    .toMutableList())
            }
            usersRvAdapter.users = selectedUsersList
            usersRvAdapter.notifyDataSetChanged()
        })

        homeViewModel.users.observe(viewLifecycleOwner, observer)

        homeViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                FireStoreStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                }
                FireStoreStatus.ERROR -> {
                    Toast.makeText(requireContext(), "حصل خطأ", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return root
    }


    private fun onUserRvItemClicked(clickedUser: User, signedInUser: User?) {
        var userBundle = bundleOf("user" to clickedUser, "signedInUser" to signedInUser)
        this.findNavController().currentDestination?.getAction(R.id.action_navigation_home_to_detailsFragment)
            ?.let {
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}