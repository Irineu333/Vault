package com.neo.vault.ui.feature.values

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.adapter.ValuesAdapter
import com.neo.vault.databinding.FragmentValuesBinding
import com.neo.vault.model.Value

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
                value = 1500.0
            ),
            Value.SubTotal(
                title = "Cofrinhos",
                value = 1500.0
            ),
        )
    }

    private fun setupListeners() {
        binding.btnHideValues.setOnClickListener {

        }
    }
}