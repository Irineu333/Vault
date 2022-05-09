package com.neo.vault.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.databinding.ItemVaultTypeBinding
import com.neo.vault.model.Type

class VaultTypesAdapter : RecyclerView.Adapter<VaultTypesAdapter.Holder>() {

    var types = emptyList<Type>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemVaultTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val type = types[position]
        holder.bind(type)
    }

    override fun getItemCount() = types.size

    class Holder(
        private val binding: ItemVaultTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: Type) = with(binding) {
            ivIcon.setImageResource(type.icon)
            tvTitle.text = type.title
            tvDescription.text = type.description
        }
    }

}
