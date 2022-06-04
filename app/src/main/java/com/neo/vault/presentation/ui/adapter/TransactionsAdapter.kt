package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.databinding.ItemTransactionBinding
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Transaction
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.formatted
import java.util.*

class TransactionsAdapter(
    private val currency: CurrencyCompat
) : RecyclerView.Adapter<TransactionsAdapter.Holder>() {

    var transactions = emptyList<Transaction>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val transaction = transactions[position]

        holder.bind(transaction)
    }

    override fun getItemCount() = transactions.size

    inner class Holder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) = with(binding) {
            tvValue.text = CurrencyUtil.formatter(transaction.value, currency)
            tvDateToBreak.text = Date(transaction.date).formatted
        }
    }
}
