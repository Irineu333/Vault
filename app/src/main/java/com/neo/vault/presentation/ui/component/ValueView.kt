package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import com.neo.vault.R
import com.neo.vault.domain.model.CurrencySupport
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.model.Value
import com.neo.vault.util.CurrencyUtil

class ValueView(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var currentValue: Value? = null

    var isHidden: Boolean = false
        set(value) {
            field = value
            currentValue?.let { setValue(it) }
        }

    private val tvTitle by lazy {
        TextView(context)
    }

    private val tvValue by lazy {
        TextView(context)
    }

    init {
        orientation = VERTICAL

        addView(
            tvTitle, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        )
        addView(
            tvValue, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        )

        attrs?.setupAttr()
    }

    fun setValue(value: Value) {
        setupTitle(value)
        setupValue(value)
    }

    private fun setupTitle(value: Value) = with(tvTitle) {
        val title = value.title.resolve(context)

        text = title
        isVisible = title.isNotEmpty()

        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_ValueView_Title)
    }

    private fun setupValue(value: Value) = with(tvValue) {
        currentValue = value

        val formatted = CurrencyUtil.formatter(value.value, value.currency)

        text = if (isHidden)
            "${value.currency.currency.symbol} ${"-".repeat(4)}" else formatted

        val textAppearance = when (value) {
            is Value.Total -> R.style.TextAppearance_ValueView_Value_Total
            is Value.SubTotal -> R.style.TextAppearance_ValueView_Value_SubTotal
        }

        TextViewCompat.setTextAppearance(this, textAppearance)
    }

    private fun AttributeSet.setupAttr() = context.withStyledAttributes(
        this,
        R.styleable.ValueView,
    ) {
        val value = getFloat(R.styleable.ValueView_value, 0f)

        val currency = when (getInt(R.styleable.ValueView_currency, 0)) {
            Currency.BRL.code -> {
                CurrencySupport.BRL
            }

            Currency.USD.code -> {
                CurrencySupport.USD
            }

            Currency.EUR.code -> {
                CurrencySupport.EUR
            }

            else -> throw IllegalArgumentException("invalid currency")
        }

        val title = getString(R.styleable.ValueView_title) ?: ""

        setValue(
            when (getInt(R.styleable.ValueView_type, 0)) {
                Type.TOTAL.code -> {
                    Value.Total(
                        value = value,
                        currency = currency,
                        title = UiText.to(title)
                    )
                }

                Type.SUBTOTAL.code -> {

                    Value.SubTotal(
                        value = value,
                        currency = currency,
                        title = UiText.to(title)
                    )
                }
                else -> throw IllegalArgumentException("invalid type")
            }
        )

        isHidden = getBoolean( R.styleable.ValueView_hidden, false)
    }

    enum class Type(val code: Int) {
        TOTAL(0),
        SUBTOTAL(1)
    }

    enum class Currency(val code: Int) {
        BRL(0),
        USD(1),
        EUR(2)
    }

}