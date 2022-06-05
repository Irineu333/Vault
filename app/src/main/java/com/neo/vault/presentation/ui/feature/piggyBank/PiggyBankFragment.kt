package com.neo.vault.presentation.ui.feature.piggyBank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.vault.databinding.FragmentPiggyBankBinding
import com.neo.vault.domain.model.Transaction
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.Session
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.ui.activity.MainActivity
import com.neo.vault.presentation.ui.adapter.TransactionsAdapter
import com.neo.vault.utils.extension.toRaw

class PiggyBankFragment : Fragment() {

    private var _binding: FragmentPiggyBankBinding? = null
    private val binding get() = _binding!!

    private val mainActivity get() = activity as? MainActivity

    private val piggyBank: Vault
        get() = PiggyBankFragmentArgs.fromBundle(
            requireArguments()
        ).piggyBank

    private val transactionsAdapter by lazy {
        TransactionsAdapter(
            currency = piggyBank.currency
        )
    }

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

        binding.rvTransactions.adapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
        rvTransactions.adapter = transactionsAdapter
    }

    override fun onStart() {
        super.onStart()

        mainActivity?.setSummation(
            piggyBank.name.toRaw(),
            listOf(
                Summation.Total(
                    values = listOf(
                        Summation.Value(
                            value = 2.01f,
                            currency = piggyBank.currency,
                        ),
                    ),
                    title = UiText.to("Guardado")
                )
            )
        )

        transactionsAdapter.sessions = listOf(
            Session(
                date = System.currentTimeMillis(),
                listOf(
                    Transaction(
                        id = 0,
                        value = -8.01f,
                        date = System.currentTimeMillis(),
                        summation = 2.01f
                    ),
                    Transaction(
                        id = 0,
                        value = -9f,
                        date = System.currentTimeMillis(),
                        summation = 10.02f
                    ),
                )
            ),
            Session(
                date = System.currentTimeMillis() - 20000000,
                listOf(
                    Transaction(
                        id = 1,
                        value = 19.02f,
                        date = System.currentTimeMillis() - 20000000,
                        summation = 19.02f
                    )
                )
            )
        )
    }
}