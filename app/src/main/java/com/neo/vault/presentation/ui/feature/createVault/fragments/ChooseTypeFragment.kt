package com.neo.vault.presentation.ui.feature.createVault.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.neo.vault.R
import com.neo.vault.databinding.FragmentChooseTypeBinding
import com.neo.vault.presentation.model.UiVaultType
import com.neo.vault.presentation.ui.adapter.VaultTypesAdapter
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet
import com.neo.vault.utils.extension.toRaw

class ChooseTypeFragment : Fragment() {

    private var _binding: FragmentChooseTypeBinding? = null
    private val binding get() = _binding!!

    private val createVaultBottomSheet
        get() = parentFragment?.parentFragment as? CreateVaultBottomSheet

    private val optionsAdapter by lazy {
        VaultTypesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTypeBinding.inflate(
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
    }

    override fun onStart() {
        super.onStart()

        createVaultBottomSheet?.updateTitle("Escolha o tipo".toRaw())
    }

    private fun setupView() = with(binding) {
        rvOptions.adapter = optionsAdapter

        rvOptions.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        optionsAdapter.types = listOf(
            UiVaultType(
                icon = R.drawable.ic_piggy_bank,
                title = "Cofrinho",
                description = "Poupe sem metas"
            ) {
                findNavController().navigate(
                    R.id.action_chooseTypeFragment_to_create_piggy_bank_graph
                )
            },
            UiVaultType(
                icon = R.drawable.ic_goal,
                title = "Meta",
                description = "Crie metas para seus sonhos"
            ) {
                Toast.makeText(
                    requireContext(),
                    "Em breve",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}