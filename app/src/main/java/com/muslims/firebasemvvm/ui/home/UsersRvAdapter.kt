package com.muslims.firebasemvvm.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.databinding.UserViewItemBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.home.UsersRvAdapter.ViewHolder

class UsersRvAdapter(var users:List<User> , var listener : Listener) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = users.get(position)
        holder.itemView.setOnClickListener{
            listener.onClick(currentUser)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(val binding: UserViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    class Listener(val clickListener: (user:User) -> Unit) {
        fun onClick(user:User) = clickListener(user)
    }
}