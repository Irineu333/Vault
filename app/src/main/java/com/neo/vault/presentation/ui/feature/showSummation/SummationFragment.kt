package com.neo.vault.presentation.ui.feature.showSummation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.neo.vault.databinding.FragmentSummationBinding
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.ui.adapter.SummationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SummationFragment : Fragment() {

    private var _binding: FragmentSummationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val valuesAdapter by lazy {
        SummationsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummationBinding.inflate(
            inflater,
            container,
            false
        )
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

    fun setValues(summations: List<Summation>) {
        if (summations.isNotEmpty()) {
            valuesAdapter.values = summations
        }
    }
}