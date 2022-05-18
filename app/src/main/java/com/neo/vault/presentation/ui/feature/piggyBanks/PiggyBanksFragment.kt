package com.neo.vault.presentation.ui.feature.piggyBanks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.neo.vault.databinding.FragmentPiggyBanksBinding
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.adapter.PiggyBanksAdapter
import com.neo.vault.presentation.ui.adapter.genericAdapter
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet
import com.neo.vault.presentation.ui.feature.piggyBanks.viewModel.PiggyBanksViewModel
import com.neo.vault.util.extension.dpToPx
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

    private val concatAdapter by lazy {
        ConcatAdapter(
            toBreakPiggyBanksAdapter,
            joiningPiggyBanksAdapter,
            genericAdapter(
                inflater = { _, parent ->
                    Space(parent.context)
                },
                onBind = { _, view ->
                    view.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        70.dpToPx().toInt()
                    )
                },
                itemCount = { 1 }
            )
        )
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
        rvPiggyBanks.adapter = concatAdapter
        fab.setOnClickListener {
            showCreatePiggyBankBottomSheet()
        }
    }

    private fun showCreatePiggyBankBottomSheet() {
        val createVaultBottomSheet = CreateVaultBottomSheet {
            putSerializable(
                CreateVaultBottomSheet.Navigate.TAG,
                CreateVaultBottomSheet.Navigate.CreatePiggyBank
            )
        }
        createVaultBottomSheet.show(
            parentFragmentManager,
            "create_vault"
        )

        createVaultBottomSheet.setFragmentResultListener(
            "create_vault"
        ) { requestKey, bundle ->
            viewModel.loadPiggyBanks()
        }
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