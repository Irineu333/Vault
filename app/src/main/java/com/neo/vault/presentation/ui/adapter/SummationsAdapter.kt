package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.databinding.ItemSummationBinding
import com.neo.vault.presentation.model.Summation

class SummationsAdapter : RecyclerView.Adapter<SummationsAdapter.Holder>() {

    var isHidden: Boolean = false
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var values = emptyList<Summation>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = Holder(
        ItemSummationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val value = values[position]
        holder.bind(value)
        holder.isHidden = isHidden
    }

    override fun getItemCount() = values.size

    class Holder(
        private val binding: ItemSummationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var isHidden: Boolean = false
            set(value) {
                field = value
                binding.value.isHidden = value
            }

        fun bind(summation: Summation) {
            binding.value.setValue(summation)
            binding.ivArrow.isVisible = summation.action != null

            summation.action?.let { action ->
                itemView.setOnClickListener {
                    action()
                }
            } ?: run {
                itemView.setOnClickListener(null)
                itemView.isClickable = false
            }
        }
    }
}