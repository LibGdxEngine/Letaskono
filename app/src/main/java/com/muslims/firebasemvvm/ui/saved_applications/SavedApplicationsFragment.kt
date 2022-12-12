package com.muslims.firebasemvvm.ui.saved_applications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentSavedApplicationsBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import com.muslims.firebasemvvm.ui.users_applications_home.UsersRvAdapter
import com.muslims.firebasemvvm.utils.StoredAuthUser

class SavedApplicationsFragment : Fragment() {
    private lateinit var viewModel: SavedApplicationsViewModel
    private var _binding: FragmentSavedApplicationsBinding? = null
    lateinit var usersRvAdapter: UsersRvAdapter

    private var signedInUser: User? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var currentFavouriteUsersList: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersRvAdapter = UsersRvAdapter(listOf(), UsersRvAdapter.Listener { user ->
            onUserRvItemClicked(user, signedInUser)
        })
    }

    private fun onUserRvItemClicked(clickedUser: User, signedInUser: User?) {
        var userBundle = bundleOf("user" to clickedUser, "signedInUser" to signedInUser)
        this.findNavController().currentDestination?.getAction(R.id.action_savedApplicationsFragment_to_UsersDetailsFragment)
            ?.let {
                this.findNavController().navigate(
                    R.id.action_savedApplicationsFragment_to_UsersDetailsFragment,
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel =
            ViewModelProvider(this).get(SavedApplicationsViewModel::class.java)

        _binding = FragmentSavedApplicationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.favouritesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersRvAdapter
        }

        val currentUserId = StoredAuthUser.getUser(requireContext())

        viewModel.users.observe(viewLifecycleOwner, Observer { usersList ->
            if (!currentUserId.isNullOrEmpty()) {
                signedInUser = usersList.first { it.id == currentUserId }
                val favouriteUsersIds = signedInUser?.favourites?.split(",")
                if(favouriteUsersIds?.isNotEmpty()!!) {
                    currentFavouriteUsersList =
                        usersList.filter { user -> favouriteUsersIds?.contains(user.id)!! }
                    usersRvAdapter.users = currentFavouriteUsersList!!
                    usersRvAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                FireStoreStatus.LOADING -> {

                }
                FireStoreStatus.ERROR -> {
                    Toast.makeText(requireContext(), "حصل خطأ حاول مجددا", Toast.LENGTH_SHORT)
                        .show()
                }
                FireStoreStatus.DONE -> {
                    if (currentFavouriteUsersList == null || currentFavouriteUsersList?.isEmpty()!!) {
                        binding.coordinatorLyt.visibility = View.VISIBLE
                        binding.favouritesListContainer.visibility = View.GONE
                    }else{
                        binding.favouritesListContainer.visibility = View.VISIBLE
                        binding.coordinatorLyt.visibility = View.GONE
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}