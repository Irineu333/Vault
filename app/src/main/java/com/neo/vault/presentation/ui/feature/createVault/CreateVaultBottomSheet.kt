package com.neo.vault.presentation.ui.feature.createVault

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neo.vault.R
import com.neo.vault.databinding.FragmentCreateVaultBinding
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.model.VaultToEdit
import com.neo.vault.utils.extension.behavior
import com.neo.vault.utils.extension.expanded
import java.io.Serializable

class CreateVaultBottomSheet(
    arguments: (Bundle.() -> Unit)? = null
) : BottomSheetDialogFragment() {

    constructor(
        startGraph: StartGraph = StartGraph.ChooseVault,
        vaultToEdit: Vault? = null
    ) : this(
        arguments = {
            putSerializable(
                StartGraph.TAG,
                startGraph
            )
            putSerializable(
                VaultToEdit.TAG,
                vaultToEdit
            )
        }
    )

    init {
        if (arguments != null) {
            this.arguments = Bundle().apply {
                arguments.invoke(this)
            }
        }
    }

    private var _binding: FragmentCreateVaultBinding? = null
    private val binding get() = _binding!!

    private val navHostFragment
        get() = childFragmentManager
            .findFragmentById(R.id.create_vault_host) as NavHostFragment

    private val navController
        get() = navHostFragment.navController

    private val NavDestination.isInitialFragment
        get() = id == initialDestinationId

    private val initialDestinationId
        get() = when (startGraph) {
            StartGraph.CreatePiggyBank -> R.id.createEditPiggyBankFragment
            StartGraph.ChooseVault -> R.id.chooseTypeFragment
        }

    private val startNavGraphId
        get() = when (startGraph) {
            StartGraph.CreatePiggyBank -> R.navigation.create_piggy_bank_graph
            StartGraph.ChooseVault -> R.navigation.create_vault_graph
        }

    private val startGraph
        get() = arguments?.getSerializable(
            StartGraph.TAG
        ) as? StartGraph ?: StartGraph.ChooseVault

    private val vaultEdit: Vault?
        get() = arguments?.getSerializable(VaultToEdit.TAG) as? Vault

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateVaultBinding.inflate(
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

        behavior?.expanded()
        behavior?.skipCollapsed = true

        setupView()
        setupListeners()
    }

    private fun setupView() = with(binding) {

        navController.setGraph(
            startNavGraphId,
            arguments?.let {
                bundleOf(
                    VaultToEdit.TAG to vaultEdit
                )
            }
        )

        btnToBack.setOnClickListener {
            navController.popBackStack()
        }

        btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.btnToBack.isVisible = !destination.isInitialFragment
        }
    }

    fun updateTitle(title : UiText) {
        binding.tvTitle.text = title.resolve(requireContext())
    }

    sealed class StartGraph : Serializable {
        object CreatePiggyBank : StartGraph()
        object ChooseVault : StartGraph()

        companion object {
            const val TAG = "start_graph"
        }
    }
}