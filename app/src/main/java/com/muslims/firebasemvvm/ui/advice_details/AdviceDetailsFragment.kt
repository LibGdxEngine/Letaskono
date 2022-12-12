package com.muslims.firebasemvvm.ui.advice_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muslims.firebasemvvm.databinding.AdviceDetailsFragmentBinding
import com.muslims.firebasemvvm.models.Advice

class AdviceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AdviceDetailsFragment()
    }

    private lateinit var viewModel: AdviceDetailsViewModel

    private var _binding: AdviceDetailsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = AdviceDetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val advice = arguments?.getParcelable<Advice>("advice")
        binding.apply {
            Glide.with(context)
                .load(advice?.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(advicesDetailsImage)
            adviceDetailsTitle.text = advice?.title
            advicesDetailsBody.text = advice?.body
            advicesDetailsCategory.text = advice?.category
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdviceDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}