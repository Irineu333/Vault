package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.ItemPiggyBankBinding
import com.neo.vault.databinding.ItemVaultsTitleBinding
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.UiText
import com.neo.vault.util.CurrencyUtil
import com.neo.vault.util.extension.formatted
import java.util.*

class PiggyBanksAdapter(
    private val title: UiText
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var collapsed: Boolean = false
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            if (value) {
                notifyItemRangeRemoved(1, piggyBanks.size)
            } else {
                notifyItemRangeInserted(1, piggyBanks.size)
            }
        }

    var piggyBanks = emptyList<Vault>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                TitleHolder(
                    ItemVaultsTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            1 -> {
                PiggyBankHolder(
                    ItemPiggyBankBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PiggyBankHolder -> {
                val piggyBank = piggyBanks[position - 1]
                holder.bind(piggyBank)
            }

            is TitleHolder -> {
                holder.setTitle(title)
                holder.itemView.setOnClickListener {
                    collapsed = !collapsed
                    holder.isCollapsed = collapsed
                }
                holder.isCollapsed = collapsed
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount() = when {
        piggyBanks.isEmpty() -> 0
        collapsed -> 1
        else -> piggyBanks.size + 1
    }

    class PiggyBankHolder(
        private val binding: ItemPiggyBankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(piggyBank: Vault) = with(binding) {
            tvName.text = piggyBank.name
            tvSummation.text = CurrencyUtil.formatter(
                piggyBank.summation,
                piggyBank.currency
            )

            tvData.text =
                piggyBank.dateToBreak?.let { Date(it).formatted }
                    ?: "Indefinido"
        }
    }

    class TitleHolder(
        private val biding: ItemVaultsTitleBinding
    ) : RecyclerView.ViewHolder(biding.root) {

        var isCollapsed: Boolean = false
            set(value) {
                field = value
                biding.tvIcon.animate().rotation(
                    if (value) 0f else 90f
                ).setDuration(100).start()
            }

        fun setTitle(title: UiText) {
            biding.tvTitle.text = title.resolve(itemView.context)
        }
    }
}
