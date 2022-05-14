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
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.model.UiText
import com.neo.vault.util.CurrencyUtil
import com.neo.vault.util.extension.removeFromParent

class SummationView(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var currentSummation: Summation? = null

    var isHidden: Boolean = false
        set(value) {
            field = value
            currentSummation?.let { setValue(it) }
        }

    private val tvTitle by lazy {
        TextView(context)
    }

    init {
        orientation = VERTICAL

        attrs?.setupAttr()
    }

    fun setValue(summation: Summation) {

         removeAllViews()

        setupTitle(summation)
        setupValues(summation)
    }

    private fun setupTitle(summation: Summation) = with(tvTitle) {
        val title = summation.title.resolve(context)

        text = title
        isVisible = title.isNotEmpty()

        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_ValueView_Title)

        tvTitle.removeFromParent()

        addView(
            tvTitle, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        )
    }

    private fun setupValues(summation: Summation) {
        currentSummation = summation

        for (value in summation.values) {
            val valueView = TextView(context)

            val formatted = CurrencyUtil.formatter(value.value, value.currency)

            valueView.text = if (isHidden)
                "${value.currency.currency.symbol} ${"-".repeat(4)}" else formatted

            val textAppearance = when (summation) {
                is Summation.Total -> R.style.TextAppearance_ValueView_Value_Total
                is Summation.SubTotal -> R.style.TextAppearance_ValueView_Value_SubTotal
            }

            TextViewCompat.setTextAppearance(valueView, textAppearance)

            addView(
                valueView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            )
        }
    }

    private fun AttributeSet.setupAttr() = context.withStyledAttributes(
        this,
        R.styleable.SummationView,
    ) {
        val value = getFloat(R.styleable.SummationView_value, 0f)

        val currency = when (getInt(R.styleable.SummationView_currency, 0)) {
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

        val title = getString(R.styleable.SummationView_title) ?: ""

        currentSummation = when (getInt(R.styleable.SummationView_type, 0)) {
            Type.TOTAL.code -> {
                Summation.Total(
                    values = listOf(
                        Summation.Value(
                            value = value,
                            currency = currency,
                        )
                    ),
                    title = UiText.to(title)
                )
            }

            Type.SUBTOTAL.code -> {
                Summation.SubTotal(
                    values = listOf(
                        Summation.Value(
                            value = value,
                            currency = currency,
                        )
                    ),
                    title = UiText.to(title)
                )
            }
            else -> throw IllegalArgumentException("invalid type")
        }

        isHidden = getBoolean(R.styleable.SummationView_hidden, false)
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