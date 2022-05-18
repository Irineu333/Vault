package com.neo.vault.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T>(
    private val inflater: (LayoutInflater, ViewGroup) -> T,
    private val onBind: (Int, T) -> Unit,
    private val itemCount: () -> Int
) : RecyclerView.Adapter<GenericAdapter.Holder<T>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder<T> {
        return Holder(
            inflater(
                LayoutInflater.from(
                    parent.context
                ),
                parent
            )
        )
    }

    override fun onBindViewHolder(
        holder: Holder<T>,
        position: Int
    ) = onBind(
        position,
        holder.binding
    )

    override fun getItemCount() = itemCount()

    class Holder<T>(
        val binding: T
    ) : RecyclerView.ViewHolder(
        getView(binding)
    ) {
        companion object {
            fun <T> getView(binding: T): View {
                return when (binding) {
                    is ViewBinding -> {
                        binding.root
                    }
                    is View -> binding
                    else -> throw IllegalArgumentException("binding must be view or view binding")
                }
            }
        }
    }
}

fun <T> genericAdapter(
    inflater: (LayoutInflater, ViewGroup) -> T,
    onBind: (Int, T) -> Unit,
    itemCount: () -> Int
): GenericAdapter<T> {
    return GenericAdapter(
        inflater = inflater,
        onBind = onBind,
        itemCount = itemCount
    )
}