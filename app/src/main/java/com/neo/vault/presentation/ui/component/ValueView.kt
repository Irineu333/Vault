package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.neo.vault.R
import com.neo.vault.presentation.model.Value
import com.neo.vault.util.CurrencyUtil

class ValueView(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val tvTitle by lazy {
        TextView(context)
    }

    private val tvValue by lazy {
        TextView(context)
    }

    init {
        orientation = VERTICAL
        addView(tvTitle)
        addView(tvValue)
    }

    fun setValue(value: Value) {
        setupTitle(value)
        setupValue(value)
    }

    private fun setupTitle(value: Value) = with(tvTitle) {
        text = when (value) {
            is Value.Total -> "Guardado"
            is Value.SubTotal -> value.title
        }

        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_ValueView_Title)
    }

    private fun setupValue(value: Value) = with(tvValue) {
        text = CurrencyUtil.toCurrency(value.value)

        val textAppearance = when (value) {
            is Value.Total -> R.style.TextAppearance_ValueView_Value_Total
            is Value.SubTotal -> R.style.TextAppearance_ValueView_Value_SubTotal
        }

        TextViewCompat.setTextAppearance(this, textAppearance)
    }
}