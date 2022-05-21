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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.databinding.FragmentPiggyBanksBinding
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.adapter.PiggyBanksAdapter
import com.neo.vault.presentation.ui.adapter.genericAdapter
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet
import com.neo.vault.presentation.ui.feature.createVault.fragments.CreatePiggyBankFragment
import com.neo.vault.presentation.ui.feature.piggyBanks.viewModel.PiggyBanksViewModel
import com.neo.vault.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PiggyBanksFragment : Fragment() {

    private var _binding: FragmentPiggyBanksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PiggyBanksViewModel by viewModels()

    private val mainActivity get() = activity as? MainActivity

    private val toBreakPiggyBanksAdapter by lazy {
        PiggyBanksAdapter(
            title = "Para quebrar".toRaw(),
            selection = viewModel.selection
        )
    }

    private val joiningPiggyBanksAdapter by lazy {
        PiggyBanksAdapter(
            title = "Juntando".toRaw(),
            selection = viewModel.selection
        )
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
                        74.dpToPx().toInt()
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
        setupListeners()
    }

    private fun setupListeners() {
        binding.rvPiggyBanks.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (recyclerView.canScrollVertically(1)) {
                        binding.fab.shrink()
                    } else {
                        binding.fab.extend()
                    }
                }
            }
        )
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

        if (
            createVaultBottomSheet.checkToShow(
                parentFragmentManager,
                "create_piggy_bank"
            )
        ) {
            createVaultBottomSheet.setFragmentResultListener(
                CreatePiggyBankFragment.Event.CREATED_VAULT.name
            ) { _, _ ->
                viewModel.loadPiggyBanks()
            }
        }
    }

    private fun setupObservers() = viewLifecycleOwner.lifecycleScope.launch {

        viewLifecycleOwner.repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            launch {
                viewModel.selection.state.collect { isActive ->
                    if (isActive) {
                        binding.fab.hideAnimated()
                    } else {
                        binding.fab.showAnimated()
                    }
                }
            }
            launch {
                viewModel.uiState.collect { state ->
                    mainActivity?.setSummation(
                        state.summations
                    )

                    toBreakPiggyBanksAdapter.piggyBanks = state.toBreakPiggyBanks
                    joiningPiggyBanksAdapter.piggyBanks = state.joiningPiggyBanks
                }
            }


        }
    }
}