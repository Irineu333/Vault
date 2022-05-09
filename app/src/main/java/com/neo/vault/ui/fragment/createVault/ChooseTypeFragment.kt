package com.neo.vault.ui.fragment.createVault

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.neo.vault.R
import com.neo.vault.adapter.VaultTypesAdapter
import com.neo.vault.databinding.FragmentChooseTypeBinding
import com.neo.vault.model.Type

class ChooseTypeFragment : Fragment() {
    private var _binding: FragmentChooseTypeBinding? = null
    private val binding get() = _binding!!

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

    private fun setupView() = with(binding) {
        rvOptions.adapter = optionsAdapter

        rvOptions.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        optionsAdapter.types = listOf(
            Type(
                icon = R.drawable.ic_piggy_bank,
                title = "Cofrinho",
                description = "Poupe sem metas"
            ),
            Type(
                icon = R.drawable.ic_goal,
                title = "Meta",
                description = "Crie metas para seus sonhos"
            )
        )
    }
}