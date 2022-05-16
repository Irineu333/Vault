package com.neo.vault.presentation.ui.feature.piggyBanks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.neo.vault.databinding.FragmentPiggyBanksBinding
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.adapter.PiggyBanksAdapter
import com.neo.vault.presentation.ui.feature.piggyBanks.viewModel.PiggyBanksViewModel
import com.neo.vault.util.extension.toRaw
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PiggyBanksFragment : Fragment() {
    private var _binding: FragmentPiggyBanksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PiggyBanksViewModel by viewModels()

    private val mainActivity get() = activity as? MainActivity

    private val toBreakPiggyBanksAdapter by lazy {
        PiggyBanksAdapter("Para quebrar".toRaw())
    }

    private val joiningPiggyBanksAdapter by lazy {
        PiggyBanksAdapter("Juntando".toRaw())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPiggyBanksBinding.inflate(
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

        setupView()
        setupObservers()
    }

    private fun setupView() = with(binding) {
        rvPiggyBanks.adapter = ConcatAdapter(
            toBreakPiggyBanksAdapter,
            joiningPiggyBanksAdapter
        )
    }

    private fun setupObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { state ->
            mainActivity?.setSummation(
                state.summation
            )

            toBreakPiggyBanksAdapter.piggyBanks = state.toBreakPiggyBanks
            joiningPiggyBanksAdapter.piggyBanks = state.joiningPiggyBanks
        }
    }
}