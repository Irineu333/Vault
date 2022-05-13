package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.ItemValueBinding
import com.neo.vault.presentation.model.Value
import com.neo.vault.presentation.ui.component.ValueView
import com.neo.vault.util.extension.dpToPx
import com.neo.vault.util.extension.updateMargins


class ValuesAdapter : RecyclerView.Adapter<ValuesAdapter.Holder>() {

    var values = emptyList<Value>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = Holder(
        ItemValueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val value = values[position]
        holder.bind(value)
    }

    override fun getItemCount() = values.size

    class Holder(
        private val binding: ItemValueBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(value: Value) {
            binding.value.setValue(value)
            binding.ivArrow.isVisible = value.action != null

            value.action?.let { action ->
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