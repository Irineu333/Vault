package com.neo.vault.presentation.ui.feature.piggyBanks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neo.vault.R
import com.neo.vault.databinding.FragmentPiggyBanksBinding
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.contract.ActionModeOnClickListener
import com.neo.vault.presentation.model.VaultEdit
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PiggyBanksFragment : Fragment(), ActionModeOnClickListener {

    private var _binding: FragmentPiggyBanksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PiggyBanksViewModel by viewModels()

    private val mainActivity get() = activity as? MainActivity

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

        mainActivity?.actionModeEnabled = false
        selectionSummationJob?.cancel()

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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {

                @SuppressLint("NotifyDataSetChanged")
                override fun handleOnBackPressed() {
                    if (viewModel.selection.isActive) {
                        viewModel.selection.disableActionMode()
                        concatAdapter.notifyDataSetChanged()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })
    }

    private fun showDeletePiggyBankDialog(piggyBanks: List<Vault>) {

        if (piggyBanks.isEmpty()) return

        val title = if (piggyBanks.size == 1)
            "Excluir cofrinho"
        else
            "Excluir cofrinhos"

        val message = if (piggyBanks.size == 1)
            "Deseja realmente excluir o cofrinho ${piggyBanks[0].name}?"
        else
            "Deseja realmente excluir os ${piggyBanks.size} cofrinhos selecionados?"

        requireContext().showAlertDialog(
            title = title.toRaw(),
            message = message.toRaw()
        ) {

            setNegativeButton("Não") { _, _ -> }

            setPositiveButton("Sim") { _, _ ->
                viewModel.deleteSelected()
            }
        }
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
                CreateVaultBottomSheet.StartGraph.TAG,
                CreateVaultBottomSheet.StartGraph.CreatePiggyBank
            )
        }
        createVaultBottomSheet.checkToShow(
            parentFragmentManager,
            "create_piggy_bank"
        )
    }

    private fun showEditPiggyBankBottomSheet(piggyBank: Vault) {
        val createVaultBottomSheet = CreateVaultBottomSheet {
            putSerializable(
                CreateVaultBottomSheet.StartGraph.TAG,
                CreateVaultBottomSheet.StartGraph.CreatePiggyBank
            )
            putSerializable(
                VaultEdit.TAG,
                piggyBank
            )
        }

        if (
            createVaultBottomSheet.checkToShow(
                parentFragmentManager,
                "edit_piggy_bank"
            )
        ) {
            createVaultBottomSheet.setFragmentResultListener(
                CreatePiggyBankFragment.Event.EDIT_VAULT.name
            ) @SuppressLint("NotifyDataSetChanged") { _, _ ->
                concatAdapter.notifyDataSetChanged()
                viewModel.selection.disableActionMode()
            }
        }
    }

    private fun setupObservers() = viewLifecycleOwner.lifecycleScope.launch {

        viewLifecycleOwner.repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            launch {
                viewModel.selection.state.collectLatest { selects ->

                    updateActionMode(selects)

                    if (selects.isNotEmpty()) {
                        binding.fab.hideAnimated()
                    } else {
                        binding.fab.showAnimated()
                    }
                }
            }
            launch {
                viewModel.uiState.collectLatest { state ->

                    mainActivity?.setSummation(state.summations)

                    toBreakPiggyBanksAdapter.piggyBanks = state.toBreakPiggyBanks
                    joiningPiggyBanksAdapter.piggyBanks = state.joiningPiggyBanks
                }
            }
        }
    }

    private fun updateActionMode(selects: List<Vault>) = mainActivity?.let { activity ->

        if (selects.isNotEmpty()) {

            activity.actionModeEnabled = true

            activity.actionMode.menu.findItem(R.id.edit)?.isVisible = selects.size == 1

            activity.actionMode.title = "${selects.size} selecionados"

            selectionSummationJob?.cancel()
            selectionSummationJob = viewLifecycleOwner.lifecycleScope.launch {
                val summation = viewModel.getValues(selects)

                activity.actionMode.subtitle = summation.joinToString(
                    separator = " + "
                ) {
                    CurrencyUtil.formatter(
                        it.value,
                        it.currency
                    )
                }
            }
        } else {
            activity.actionModeEnabled = false
        }
    }

    override fun onClickMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {

                showDeletePiggyBankDialog(
                    piggyBanks = viewModel.selection.state.value
                )

                true
            }

            R.id.edit -> {

                showEditPiggyBankBottomSheet(
                    piggyBank = viewModel.selection.state.value[0]
                )

                true
            }
            else -> false
        }
    }
}