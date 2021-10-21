package com.muslims.firebasemvvm.ui.users_applications_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.UserViewItemBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.users_applications_home.UsersRvAdapter.ViewHolder
import com.muslims.firebasemvvm.utils.ItemAnimation

class UsersRvAdapter(var users:List<User> , var listener : Listener,private val animationType : Int = 2) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users.get(position)
        when(user.sex){
            "Male" ->{ holder.binding.image.setImageResource(R.drawable.man_with_lehya) }
            "Female" ->{ holder.binding.image.setImageResource(R.drawable.man_without_lehya) }
        }
        holder.itemView.setOnClickListener{
            listener.onClick(user)
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(val binding: UserViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    class Listener(val clickListener: (user:User) -> Unit) {
        fun onClick(user:User) = clickListener(user)
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