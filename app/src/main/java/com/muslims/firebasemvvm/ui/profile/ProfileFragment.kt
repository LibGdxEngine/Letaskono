package com.muslims.firebasemvvm.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var advicesViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        advicesViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.termsOfUse.setOnClickListener{ showTermServicesDialog() }

        binding.btnCreateAccount.setOnClickListener {
            this.findNavController().navigate(R.id.action_navigation_profile_to_questionsFragment,
                null,
                navOptions {
                    anim{
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
        }

        binding.btnGoToLogin.setOnClickListener {
            this.findNavController().navigate(R.id.action_navigation_profile_to_loginFragment,
                null,
                navOptions {
                    anim{
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
        }

        return root
    }

    private fun showTermServicesDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_term_of_services)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<View>(R.id.bt_accept) as Button).setOnClickListener {
            dialog.dismiss()
        }
        (dialog.findViewById<View>(R.id.bt_decline) as Button).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}