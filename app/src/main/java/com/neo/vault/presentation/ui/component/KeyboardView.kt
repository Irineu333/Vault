package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Space
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import com.neo.vault.R
import com.neo.vault.presentation.model.UiText
import com.neo.vault.utils.extension.dpToPx
import com.neo.vault.utils.extension.toRaw

class KeyboardView(
    context: Context,
    attr: AttributeSet? = null
) : FrameLayout(
    context,
    attr
) {

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
            is Item.Space -> {
                handleSpace(
                    item,
                    parent
                )
            }
        }
    }

    private fun handleSpace(space: Item.Space, parent: ViewGroup) {
        parent.addView(
            createSpace(space)
        )
    }

    private fun createSpace(space: Item.Space): View? {
        return Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                space.space,
                space.space,
            )
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
        val button = when (key.icon) {
            is Item.Icon.Image -> {
                IconView(context).apply {
                    setImageResource(key.icon.resId)
                }
            }
            is Item.Icon.Text -> {
                KeyView(context).apply {
                    text = key.icon.text.resolve(context)
                }
            }
        }

        button.setOnClickListener {
            key.action()
        }

        button.layoutParams = LinearLayout.LayoutParams(
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

        return button
    }
}

sealed class Item {

    data class Key(
        val icon: Icon,
        val action: () -> Unit = {}
    ) : Item() {
        constructor(
            text : String,
            action: () -> Unit = {}
        ) : this(Icon.Text(text.toRaw()), action)

        constructor(
            @DrawableRes
            icon : Int,
            action: () -> Unit = {}
        ) : this(Icon.Image(icon), action)
    }

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

    data class Space(
        @Px val space: Int
    ) : Item()

    sealed class Icon {
        data class Text(
            val text: UiText
        ) : Icon()

        data class Image(
            @DrawableRes
            val resId: Int
        ) : Icon()
    }
}