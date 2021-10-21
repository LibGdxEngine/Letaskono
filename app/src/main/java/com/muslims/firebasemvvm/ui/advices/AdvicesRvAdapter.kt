package com.muslims.firebasemvvm.ui.advices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muslims.firebasemvvm.databinding.AdviceViewItemBinding
import com.muslims.firebasemvvm.models.Advice
import io.github.ponnamkarthik.richlinkpreview.ViewListener

class AdvicesRvAdapter(var advicesList:List<Advice>, val listener:Listener): RecyclerView.Adapter<AdvicesRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdviceViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = advicesList.get(position)
        val image = holder.binding.image
        holder.apply {
            binding.date.text =  currentItem.time
            binding.subtitle.text = currentItem.category
            binding.adviceTitle.text = currentItem.title
            Glide.with(holder.binding.image.context)
                .load(currentItem.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image)

            itemView.setOnClickListener { listener.onClick(currentItem) }
        }
    }

    override fun getItemCount(): Int {
        return advicesList.size
    }

    class ViewHolder(val binding:AdviceViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    class Listener(val clickListener: (advice: Advice) -> Unit) {
        fun onClick(advice: Advice) = clickListener(advice)
    }

}