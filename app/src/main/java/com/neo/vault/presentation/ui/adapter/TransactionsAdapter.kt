package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.ItemTransactionBinding
import com.neo.vault.databinding.ItemVaultsTitleBinding
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Transaction
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.dateFormatted
import com.neo.vault.utils.extension.timeFormatted
import java.util.*

class TransactionsAdapter(
    private val currency: CurrencyCompat
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = emptyList<Any>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Type.SESSION.code -> {
                SessionHolder(
                    ItemVaultsTitleBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ),
                        parent,
                        false
                    )
                )
            }

            Type.TRANSACTION.code -> TransactionHolder(
                ItemTransactionBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException("invalid viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]

        when (holder) {
            is TransactionHolder -> {
                item as Transaction

                holder.bind(item)
            }
            is SessionHolder -> {
                item as Long

                holder.bind(item)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is Transaction -> Type.TRANSACTION.code
            is Long -> Type.SESSION.code
            else -> throw IllegalStateException("invalid type ${item.javaClass}")
        }
    }

    inner class TransactionHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context get() = itemView.context

        fun bind(transaction: Transaction) = with(binding) {
            tvValue.text = CurrencyUtil.formatter(transaction.value, currency)
            tvDateToBreak.text = Date(transaction.date).timeFormatted

            tvNewValue.text = CurrencyUtil.formatter(transaction.summation, currency)
            tvOldValue.text = CurrencyUtil.formatter(transaction.oldSummation, currency)

            if (transaction.isPositive) {
                ivOscillation.rotationX = 0f
                ivOscillation.setColorFilter(ContextCompat.getColor(context, R.color.green))
            } else {
                ivOscillation.rotationX = 180f
                ivOscillation.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    class SessionHolder(
        private val binding: ItemVaultsTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvIcon.isVisible = false
        }

        fun bind(dateMillis: Long) = with(binding) {
            tvTitle.text = Date(dateMillis).dateFormatted
        }
    }

    enum class Type(val code: Int) {
        SESSION(code = 0),
        TRANSACTION(code = 1)
    }
}
