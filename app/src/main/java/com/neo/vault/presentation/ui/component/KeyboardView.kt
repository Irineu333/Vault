package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.updateMargins
import com.neo.vault.utils.extension.dpToPx

class KeyboardView(
    context: Context,
    attr: AttributeSet? = null
) : FrameLayout(
    context,
    attr
) {

    init {
        setKeys(
            Item.Key("A")
        )
    }

    fun setKeys(item: Item) {

        removeAllViews()

        handleItem(item, this)
    }

    private fun handleItem(item: Item, parent: ViewGroup) {
        when (item) {
            is Item.Group -> {
                handleGroup(
                    item,
                    parent
                )
            }
            is Item.Key -> {
                handleKey(
                    item,
                    parent
                )
            }
        }
    }

    private fun handleKey(
        key: Item.Key,
        parent: ViewGroup
    ) {
        parent.addView(
            createKey(key)
        )
    }

    private fun handleGroup(
        group: Item.Group,
        parent: ViewGroup
    ) {
        val viewGroup = createGroup(
            group.orientation
        ).also {
            parent.addView(it)
        }

        for (item in group.keys) {
            handleItem(item, viewGroup)
        }
    }

    private fun createGroup(
        orientation: Item.Group.Orientation
    ): ViewGroup {
        return LinearLayout(context).apply {
            this.orientation = when (orientation) {
                Item.Group.Orientation.VERTICAL -> {
                    layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        1.0f
                    )
                    LinearLayout.VERTICAL
                }
                Item.Group.Orientation.HORIZONTAL -> {
                    layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        1.0f
                    )
                    LinearLayout.HORIZONTAL
                }
            }
        }
    }

    private fun createKey(key: Item.Key): View {
        val text = KeyView(context)

        text.text = key.text
        text.gravity = Gravity.CENTER

        text.setOnClickListener {
            key.action()
        }

        text.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT,
            1.0f
        ).apply {
            val vertical = 4.dpToPx().toInt()
            val horizontal = 4.dpToPx().toInt()

            updateMargins(
                left = horizontal,
                right = horizontal,
                top = vertical,
                bottom = vertical,
            )
        }

        return text
    }
}

sealed class Item {

    data class Key(
        val text: String,
        val action: () -> Unit = {}
    ) : Item()

    data class Group(
        val orientation: Orientation = Orientation.HORIZONTAL,
        val keys: List<Item>
    ) : Item() {

        enum class Orientation {
            VERTICAL,
            HORIZONTAL
        }

        companion object {
            fun vertical(
                vararg keys: Item
            ) = Group(Orientation.VERTICAL, keys.toList())

            fun horizontal(
                vararg keys: Item
            ) = Group(Orientation.HORIZONTAL, keys.toList())
        }
    }
}