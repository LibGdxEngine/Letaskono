package com.muslims.firebasemvvm.ui.advices


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
import com.muslims.firebasemvvm.databinding.FragmentAdvicesBinding
import com.muslims.firebasemvvm.models.Advice
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus


class AdvicesFragment : Fragment() {

    private lateinit var advicesViewModel: AdvicesViewModel
    private var _binding: FragmentAdvicesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var advicesAdapter: AdvicesRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        advicesViewModel =
            ViewModelProvider(this).get(AdvicesViewModel::class.java)

        _binding = FragmentAdvicesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        advicesAdapter = AdvicesRvAdapter(listOf(), AdvicesRvAdapter.Listener { advice ->
            onAdviceItemClicked(advice)
        })


        binding.advicesRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = advicesAdapter
        }

        advicesViewModel.advices.observe(viewLifecycleOwner, Observer { advicesList ->

            advicesAdapter.advicesList = advicesList.sortedBy { it.id }
            advicesAdapter.notifyDataSetChanged()
        })

        advicesViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                FireStoreStatus.LOADING -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.advicesRv.visibility = View.INVISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBar2.visibility = View.GONE
                    binding.advicesRv.visibility = View.VISIBLE
                }
                FireStoreStatus.ERROR -> {

                }
            }
        })

        return root
    }


    private fun onAdviceItemClicked(advice: Advice) {
        var adviceBundle = bundleOf("advice" to advice)
        this.findNavController().navigate(R.id.action_navigation_advices_to_adviceDetailsFragment,
            adviceBundle,
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