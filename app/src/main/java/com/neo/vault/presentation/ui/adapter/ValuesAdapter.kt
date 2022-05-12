package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
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
    ) = Holder(ValueView(parent.context))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val value = values[position]
        holder.bind(value)
        holder.isFirst = position == 0
    }

    override fun getItemCount() = values.size

    class Holder(
        private val view: ValueView
    ) : RecyclerView.ViewHolder(view) {

        init {
            view.setBackgroundResource(R.drawable.ripple_rectangle)
        }

        var isFirst: Boolean = false
            set(value) {
                field = value
                view.updateMargins(
                    top = if (field) 0 else 8.dpToPx().toInt()
                )
            }

        fun bind(value: Value) {
            view.setValue(value)

            value.action?.let { action ->
                view.setOnClickListener {
                    action()
                }
            } ?: run {
                view.setOnClickListener(null)
                view.isClickable = false
            }
        }
    }
}