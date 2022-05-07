package com.neo.vault.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.component.ValueView
import com.neo.vault.model.Value
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

        var isFirst: Boolean = false
            set(value) {
                field = value
                view.updateMargins(
                    top = if (field) 0 else 8.dpToPx().toInt()
                )
            }

        fun bind(value: Value) {
            view.setValue(value)
        }
    }
}