package com.neo.vault.presentation.ui.feature.homeVaults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neo.vault.R
import com.neo.vault.databinding.FragmentHomeVaultsBinding
import com.neo.vault.domain.model.CurrencySupport
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet

class HomeVaultsFragment : Fragment() {

    private var _binding: FragmentHomeVaultsBinding? = null
    private val binding get() = _binding!!

    private val mainActivity get() = activity as? MainActivity

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

    override fun onStart() {
        super.onStart()
        mainActivity?.setSummation(
            listOf(
                Summation.Total(
                    values = listOf(
                        Summation.Value(
                            value = 3000f,
                            currency = CurrencySupport.BRL,
                        )
                    ),
                    title = UiText.to("Guardado")
                ),
                Summation.SubTotal(
                    values = listOf(
                        Summation.Value(
                            value = 1500f,
                            currency = CurrencySupport.USD,
                        ),
                        Summation.Value(
                            value = 1500f,
                            currency = CurrencySupport.BRL,
                        )
                    ),
                    title = UiText.to("Metas"),
                    action = {

                    }
                ),
                Summation.SubTotal(
                    values = listOf(
                        Summation.Value(
                            value = 1500f,
                            currency = CurrencySupport.EUR,
                        )
                    ),
                    title = UiText.to("Cofrinhos"),
                    action = {
                        findNavController().navigate(R.id.action_homeVaults_to_piggyBankVaults)
                    }
                ),
            )
        )
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