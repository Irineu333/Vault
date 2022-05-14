package com.neo.vault.presentation.ui.feature.piggyBanks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.neo.vault.databinding.FragmentPiggyBankVaultsBinding
import com.neo.vault.domain.model.CurrencySupport
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.ui.acitivity.MainActivity
import com.neo.vault.presentation.ui.feature.piggyBanks.viewModel.PiggyBanksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PiggyBanksFragment : Fragment() {
    private var _binding: FragmentPiggyBankVaultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PiggyBanksViewModel by viewModels()

    private val mainActivity get() = activity as? MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPiggyBankVaultsBinding.inflate(
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

        viewModel.loadPiggyBanks()

        setupObservers()
    }

    private fun setupObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { state ->
            mainActivity?.setSummation(
               state.summations
            )
        }
    }
}