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
import com.neo.vault.presentation.model.Session
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.dateFormatted
import java.util.*

class TransactionsAdapter(
    private val currency: CurrencyCompat
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sessions = emptyList<Session<Transaction>>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            updateCurrent()
            notifyDataSetChanged()
        }

    private var current = emptyList<Any>()

    private fun updateCurrent() {
        current = buildList {
            for (session in sessions) {
                add(session)
                if (!session.expanded) continue
                addAll(session.items)
            }
        }
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

        val item = current[position]

        when (holder) {
            is TransactionHolder -> {
                item as Transaction

                holder.bind(item)
            }
            is SessionHolder -> {
                item as Session<*>

                holder.bind(item)

                fun changeExpanded(item: Session<*>) {
                    item.expanded = !item.expanded

                    updateCurrent()

                    val startList = position + 1
                    val endList = position + item.size

                    if (item.expanded) {
                        notifyItemRangeInserted(
                            startList,
                            endList
                        )
                    } else {
                        notifyItemRangeRemoved(
                            startList,
                            endList
                        )
                    }
                }

                holder.itemView.setOnClickListener {
                    changeExpanded(item)
                    holder.expanded(item.expanded)
                }
            }
        }
    }

    override fun getItemCount() = current.size

    override fun getItemViewType(position: Int): Int {
        return when (val item = current[position]) {
            is Transaction -> Type.TRANSACTION.code
            is Session<*> -> Type.SESSION.code
            else -> throw IllegalStateException("invalid type ${item.javaClass}")
        }
    }

    inner class TransactionHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context get() = itemView.context

        fun bind(transaction: Transaction) = with(binding) {
            tvValue.text = CurrencyUtil.formatter(transaction.value, currency)
            tvDateToBreak.text = Date(transaction.date).dateFormatted

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

    inner class SessionHolder(
        private val binding: ItemVaultsTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(session: Session<*>) = with(binding) {
            tvTitle.text = Date(session.date).dateFormatted
            binding.tvIcon.rotation = if (session.expanded) 90f else 0f
        }

        fun expanded(expanded: Boolean) {
            binding.tvIcon.animate().rotation(
                if (expanded) 90f else 0f
            ).setDuration(100).start()
        }
    }

    enum class Type(val code: Int) {
        SESSION(code = 0),
        TRANSACTION(code = 1)
    }
}
