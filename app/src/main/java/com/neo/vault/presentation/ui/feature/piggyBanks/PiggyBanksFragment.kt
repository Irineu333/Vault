package com.neo.vault.presentation.ui.feature.piggyBanks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Space
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.FragmentPiggyBanksBinding
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.adapter.PiggyBanksAdapter
import com.neo.vault.presentation.ui.adapter.genericAdapter
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet
import com.neo.vault.presentation.ui.feature.createVault.fragments.CreatePiggyBankFragment
import com.neo.vault.presentation.ui.feature.piggyBanks.viewModel.PiggyBanksViewModel
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PiggyBanksFragment : Fragment() {

    private var _binding: FragmentPiggyBanksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PiggyBanksViewModel by viewModels()

    private val mainActivity get() = activity as? MainActivity

    private var actionMode: ActionMode? = null

    private var selectionSummationJob: Job? = null

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
            launch @SuppressLint("NotifyDataSetChanged") {
                viewModel.selection.selectsState.collect { selects ->
                    if (selects.isNotEmpty()) {
                        updateActionMode(selects)
                        binding.fab.hideAnimated()
                    } else {
                        actionMode?.finish()
                        concatAdapter.notifyDataSetChanged()
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

    private fun updateActionMode(selects: List<Vault>) {

        if (actionMode == null)
            actionMode = mainActivity?.startSupportActionMode(
                object : ActionMode.Callback {
                    override fun onCreateActionMode(
                        mode: ActionMode,
                        menu: Menu
                    ): Boolean {
                        mode.menuInflater.inflate(R.menu.selection_vault_options, menu)
                        return true
                    }

                    override fun onPrepareActionMode(
                        mode: ActionMode?,
                        menu: Menu?
                    ): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(
                        mode: ActionMode?,
                        item: MenuItem?
                    ): Boolean {
                        return false
                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {
                        actionMode = null
                        viewModel.selection.removeAll()
                    }
                }
            )

        actionMode!!.menu.findItem(R.id.edit).isVisible = selects.size == 1

        actionMode!!.title = "${selects.size} selecionados"

        selectionSummationJob?.cancel()
        selectionSummationJob = viewLifecycleOwner.lifecycleScope.launch {
            val summation = viewModel.getValues(selects)

            actionMode?.subtitle = summation.joinToString(
                separator = ", "
            ) {
                CurrencyUtil.formatter(
                    it.value,
                    it.currency
                )
            }
        }
    }
}