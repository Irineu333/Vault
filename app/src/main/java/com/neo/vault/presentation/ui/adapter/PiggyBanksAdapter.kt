package com.neo.vault.presentation.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.ItemPiggyBankBinding
import com.neo.vault.databinding.ItemVaultsTitleBinding
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.Selection
import com.neo.vault.presentation.model.UiText
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.formatted
import com.neo.vault.utils.extension.modify
import java.util.*

class PiggyBanksAdapter(
    private val title: UiText,
    private val selection: Selection<Vault>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var piggyBanks = emptyList<Vault>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var isCollapsed: Boolean = false
        set(value) {
            field = value
            if (value) {
                notifyItemRangeRemoved(1, piggyBanks.size)
            } else {
                notifyItemRangeInserted(1, piggyBanks.size)
            }
        }

    private var Vault.selected
        get() = selection.selected(this)
        set(value) {
            if (value) {
                selection.add(this)
            } else {
                selection.remove(this)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Type.TITLE.code -> {
                TitleHolder(
                    ItemVaultsTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            Type.ITEM.code -> {
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

                fun updateItem() {
                    piggyBank.selected = !piggyBank.selected
                    holder.bind(piggyBank)
                }

                holder.itemView.setOnLongClickListener {
                    updateItem()
                    true
                }

                holder.itemView.setOnClickListener {
                    if (selection.isActive) {
                        updateItem()
                    }
                }
            }

            is TitleHolder -> {
                holder.bind(title)

                holder.itemView.setOnClickListener {
                    isCollapsed = !isCollapsed
                    holder.collapse(isCollapsed)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) Type.TITLE.code else Type.ITEM.code
    }

    override fun getItemCount() = when {
        piggyBanks.isEmpty() -> 0
        isCollapsed -> 1
        else -> piggyBanks.size + 1
    }

    enum class Type(val code: Int) {
        TITLE(0),
        ITEM(1)
    }

    inner class PiggyBankHolder(
        private val binding: ItemPiggyBankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context get() = itemView.context

        fun bind(piggyBank: Vault) = with(binding) {
            tvName.text = piggyBank.name

            tvSummation.text = CurrencyUtil.formatter(
                piggyBank.summation,
                piggyBank.currency
            )

            tvDateToBreak.isVisible =
                piggyBank.dateToBreak?.also {
                    tvDateToBreak.text = Date(it).formatted
                } != null

            flRoot.background = if (piggyBank.selected) {
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        R.color.blue_light
                    ).modify(
                        alpha = (255 * 0.5).toInt()
                    )
                )
            } else null
        }
    }

    inner class TitleHolder(
        private val biding: ItemVaultsTitleBinding
    ) : RecyclerView.ViewHolder(biding.root) {

        fun bind(title: UiText) {
            biding.tvTitle.text = title.resolve(itemView.context)
            biding.tvIcon.rotation = if (isCollapsed) 0f else 90f
        }

        fun collapse(collapsed: Boolean) {
            biding.tvIcon.animate().rotation(
                if (collapsed) 0f else 90f
            ).setDuration(100).start()
        }
    }
}
