package com.muslims.firebasemvvm.ui.administration.applications_list_preview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentAdminApplicationsListBinding
import com.muslims.firebasemvvm.databinding.FragmentAdminBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import com.muslims.firebasemvvm.ui.users_applications_home.UsersRvAdapter
import com.muslims.firebasemvvm.utils.StoredAuthUser

class AdminApplicationsListFragment : Fragment() {

    companion object {
        fun newInstance() = AdminApplicationsListFragment()
    }

    private var _binding: FragmentAdminApplicationsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminApplicationsListViewModel
    private lateinit var observer: Observer<List<User>>

    private var signedInUser: User? = null
    lateinit var usersRvAdapter: UsersRvAdapter
    var selectedUsersList: MutableList<User> = mutableListOf()
    var allUsersList: MutableList<User> = mutableListOf()
    var searchUsersList: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersRvAdapter = UsersRvAdapter(mutableListOf(), UsersRvAdapter.Listener { user ->
            onUserRvItemClicked(user, signedInUser)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AdminApplicationsListViewModel::class.java)
        _binding = FragmentAdminApplicationsListBinding.inflate(inflater, container, false)

        val filterQuery = arguments?.getString("query")

        binding.swipToRefreshLayout.setOnRefreshListener {
            viewModel.getAllUsers()
            binding.swipToRefreshLayout.isRefreshing = false
        }



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUsersList.clear()
                for (user: User in allUsersList) {
                    if (user.phone.contains(query.trim()) || user.id.contains(query.trim())) {
                        searchUsersList.add(user)
                    }
                }
                usersRvAdapter.users = searchUsersList
                usersRvAdapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersRvAdapter
        }

        val currentUserId = StoredAuthUser.getUser(requireContext())

        observer = Observer<List<User>> { usersList ->
            signedInUser = usersList.first { it.phone == currentUserId }
            allUsersList = usersList.filter { user -> user.generalStatus == filterQuery && user.questionsList!!.isNotEmpty() }
                .toMutableList()
            usersRvAdapter.users = allUsersList
            usersRvAdapter.notifyDataSetChanged()
        }

        viewModel.users.observe(viewLifecycleOwner, observer)

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminApplicationsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onUserRvItemClicked(clickedUser: User, signedInUser: User?) {
        var userBundle = bundleOf("user" to clickedUser, "signedInUser" to signedInUser)
        this.findNavController().currentDestination?.getAction(R.id.action_adminApplicationsListFragment_to_adminApplicationsDetailsFragment)
            ?.let {
                this.findNavController()
                    .navigate(R.id.action_adminApplicationsListFragment_to_adminApplicationsDetailsFragment,
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