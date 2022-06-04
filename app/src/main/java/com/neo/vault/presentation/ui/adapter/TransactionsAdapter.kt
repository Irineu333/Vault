package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.ItemTransactionBinding
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Transaction
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.dateFormatted
import com.neo.vault.utils.extension.dateTimeFormatted
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

        private val context get() = itemView.context

        fun bind(transaction: Transaction) = with(binding) {
            tvValue.text = CurrencyUtil.formatter(transaction.value, currency)
            tvDateToBreak.text = Date(transaction.date).dateTimeFormatted

            tvNewValue.text = CurrencyUtil.formatter(transaction.summation, currency)
            tvOldValue.text = CurrencyUtil.formatter(transaction.oldSummation, currency)

            if(transaction.isPositive) {
                ivOscillation.rotationX = 0f
                ivOscillation.setColorFilter(ContextCompat.getColor(context, R.color.green))
            } else {
                ivOscillation.rotationX = 180f
                ivOscillation.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
        }
    }
}
