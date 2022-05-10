package com.neo.vault.ui.feature.vaults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.databinding.FragmentHomeVaultsBinding
import com.neo.vault.ui.feature.createVault.CreateVaultBottomSheet

class HomeVaultsFragment : Fragment() {

    private var _binding: FragmentHomeVaultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeVaultsBinding.inflate(
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
        btnCreateVault.setOnClickListener {
            CreateVaultBottomSheet().show(
                parentFragmentManager,
                "create_vault"
            )
        }
    }
}