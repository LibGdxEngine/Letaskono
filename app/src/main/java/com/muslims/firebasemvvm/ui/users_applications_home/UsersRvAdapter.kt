package com.muslims.firebasemvvm.ui.users_applications_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.UserViewItemBinding
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.users_applications_home.UsersRvAdapter.ViewHolder
import com.muslims.firebasemvvm.utils.ItemAnimation

class UsersRvAdapter(
    var users: List<User>,
    var listener: Listener,
    private val animationType: Int = 2
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mLastClickTime = System.currentTimeMillis();
        val CLICK_TIME_INTERVAL = 1;
        val user = users.get(position)
        var name = ""
        var description = ""
        val userAge = user?.questionsList?.first { it.id == "2" }
        val userNationality = user?.questionsList?.first { it.id == "3" }
        val userHeight = user?.questionsList?.first { it.id == "26" }
        val userWeight = user?.questionsList?.first { it.id == "6" }
        val userSkin = user?.questionsList?.first { it.id == "7" }
        val userStateAndLocation = user?.questionsList?.first { it.id == "11" }
        var userStatus: Question? = null

        when (user.gender) {
            "man" -> {
                userStatus = user?.questionsList?.first { it.id == "19" }
                name += "عريس "
                if (user.questionsList?.first { it.id == "23" }?.answer == "نعم") {
                    name += " ملتحي"
                    holder.binding.image.setImageResource(R.drawable.man_with_lehya)
                } else {
                    holder.binding.image.setImageResource(R.drawable.man_without_lehya)
                }

            }
            "woman" -> {
                userStatus = user?.questionsList?.first { it.id == "22" }
                name += "عروسة "
                when (user.questionsList?.first { it.id == "24" }?.answer) {
                    "منتقبة" -> {
                        name += " منتقبة "
                        holder.binding.image.setImageResource(R.drawable.women_with_neqab)
                    }
                    "مختمرة" -> {
                        holder.binding.image.setImageResource(R.drawable.women_with_khemar)
                    }
                    else -> {
                        holder.binding.image.setImageResource(R.drawable.women_with_hejab)
                    }
                }

            }
        }
        holder.binding.name.text = name + " - " + " كود " + "${user.id}"
        holder.binding.description.text = "السن: " + "${userAge?.answer}" +
                " - " + "${userNationality?.answer}" +
                " - " + "${userStatus?.answer}" +
                " - " + "${userStateAndLocation?.answer}" +
                " "

        holder.itemView.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                println("Can't click more!")
                return@setOnClickListener
            }
            mLastClickTime = now
            listener.onClick(user)
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(val binding: UserViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    class Listener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) = clickListener(user)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private var lastPosition = -1
    private var on_attach = true

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (on_attach) position else -1, animationType)
            lastPosition = position
        }
    }


}