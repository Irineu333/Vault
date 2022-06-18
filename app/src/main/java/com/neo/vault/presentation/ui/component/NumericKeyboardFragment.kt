package com.neo.vault.presentation.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.databinding.FragmentNumericKeyboardBinding

class NumericKeyboardFragment : Fragment() {

    private var _binding: FragmentNumericKeyboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumericKeyboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
        keyboard.setKeys(
            Item.Group.vertical(
                Item.Group.horizontal(
                    Item.Key(
                        "1"
                    ),
                    Item.Key(
                        "2"
                    ),
                    Item.Key(
                        "3"
                    ),
                    Item.Key(
                        "+"
                    ),
                ),
                Item.Group.horizontal(
                    Item.Key(
                        "4"
                    ),
                    Item.Key(
                        "5"
                    ),
                    Item.Key(
                        "6"
                    ),
                    Item.Key(
                        "-"
                    ),
                ),
                Item.Group.horizontal(
                    Item.Key(
                        "7"
                    ),
                    Item.Key(
                        "8"
                    ),
                    Item.Key(
                        "9"
                    ),
                    Item.Key(
                        "*"
                    )

                ),
                Item.Group.horizontal(
                    Item.Key(
                        "."
                    ),
                    Item.Key(
                        "0"
                    ),
                    Item.Key(
                        "="
                    ),
                    Item.Key(
                        "/"
                    )
                )
            ),
        )
    }
}