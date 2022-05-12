package com.neo.vault.presentation.ui.feature.listVault

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.neo.vault.R
import com.neo.vault.presentation.ui.adapter.ValuesAdapter
import com.neo.vault.databinding.FragmentValuesBinding
import com.neo.vault.presentation.model.Value

class ValuesFragment : Fragment() {

    private var _binding: FragmentValuesBinding? = null
    private val binding get() = _binding!!

    private val valuesAdapter by lazy {
        ValuesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentValuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    private fun setupView() = with(binding.rvValues) {
        adapter = valuesAdapter

        valuesAdapter.values = listOf(
            Value.Total(
                value = 3000.0
            ),
            Value.SubTotal(
                title = "Metas",
                value = 1500.0,
                action = {

                }
            ),
            Value.SubTotal(
                title = "Cofrinhos",
                value = 1500.0,
                action = {

                }
            ),
        )
    }

    private fun setupListeners() {
        binding.btnHideValues.setOnClickListener {

        }
    }
}