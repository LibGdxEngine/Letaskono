package com.muslims.firebasemvvm.ui.advices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muslims.firebasemvvm.databinding.FragmentAdvicesBinding
import com.muslims.firebasemvvm.models.Advice
import com.muslims.firebasemvvm.ui.home.FireStoreStatus

class AdvicesFragment : Fragment() {

    private lateinit var advicesViewModel: AdvicesViewModel
    private var _binding: FragmentAdvicesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        advicesViewModel =
            ViewModelProvider(this).get(AdvicesViewModel::class.java)

        _binding = FragmentAdvicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.floatingActionButton.setOnClickListener{
            advicesViewModel.addAdvice(Advice(id="1", url = ""))
        }

        val advicesAdapter = AdvicesRvAdapter(listOf(), AdvicesRvAdapter.Listener { advice ->
            onAdviceItemClicked(advice)
        })

        binding.advicesRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = advicesAdapter
        }

        advicesViewModel.advices.observe(viewLifecycleOwner , Observer {advicesList ->
            advicesAdapter.advicesList = advicesList
            advicesAdapter.notifyDataSetChanged()
        })

        advicesViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            when(status){
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
        Toast.makeText(context, "Hello ${advice.url}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}