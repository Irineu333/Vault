package com.neo.vault.presentation.ui.feature.showValues

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.databinding.FragmentValuesBinding
import com.neo.vault.presentation.model.Value
import com.neo.vault.presentation.ui.adapter.ValuesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowValuesFragment : Fragment() {

    private var _binding: FragmentValuesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        valuesAdapter.isHidden = sharedPreferences.getBoolean("hidden_values", false)
    }

    private fun setupListeners() {
        binding.btnHideValues.setOnClickListener {
            valuesAdapter.isHidden = !valuesAdapter.isHidden

            sharedPreferences.edit()
                .putBoolean("hidden_values", valuesAdapter.isHidden)
                .apply()
        }
    }

    fun setValues(values: List<Value>) {
        valuesAdapter.values = values
    }
}