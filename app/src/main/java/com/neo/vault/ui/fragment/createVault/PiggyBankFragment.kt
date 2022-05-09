package com.neo.vault.ui.fragment.createVault

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.R
import com.neo.vault.databinding.FragmentPiggyBankBinding
import com.neo.vault.util.CurrencyUtil

class PiggyBankFragment : Fragment() {

    private var _binding: FragmentPiggyBankBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPiggyBankBinding.inflate(
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

    private fun setupView() {
        setupCurrency()
    }

    private fun setupCurrency() = with(binding.cgCurrency) {
        when (CurrencyUtil.currency.currencyCode) {
            "BRL" -> {
                check(R.id.chip_brl)
            }

            "USD" -> {
                check(R.id.chip_usd)
            }

            "EUR" -> {
                check(R.id.chip_eur)
            }
        }
    }

}