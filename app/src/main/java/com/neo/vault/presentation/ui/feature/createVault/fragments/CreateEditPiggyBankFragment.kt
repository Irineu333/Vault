package com.neo.vault.presentation.ui.feature.createVault.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.neo.vault.R
import com.neo.vault.databinding.FragmentCreatePiggyBankBinding
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.VaultToEdit
import com.neo.vault.presentation.ui.feature.createVault.CreateVaultBottomSheet
import com.neo.vault.presentation.ui.feature.createVault.viewModel.CreateVaultUiEffect
import com.neo.vault.presentation.ui.feature.createVault.viewModel.CreateVaultViewModel
import com.neo.vault.utils.TimeUtils
import com.neo.vault.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class CreateEditPiggyBankFragment : Fragment() {

    private var _binding: FragmentCreatePiggyBankBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateVaultViewModel by viewModels()

    private val createVaultBottomSheet
        get() = parentFragment?.parentFragment as? CreateVaultBottomSheet

    private val vaultEdit get() = arguments?.getSerializable(VaultToEdit.TAG) as? Vault

    private var nameTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePiggyBankBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        nameTextWatcher?.let {
            binding.tilName.removeTextWatcher(it)
        }

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.editVaultId = vaultEdit?.id

        setupListeners()
        setupObservers()
        setupView()
    }

    override fun onStart() {
        super.onStart()

        val title = vaultEdit?.let { "Editar ${it.name}" } ?: "Criar cofrinho"

        createVaultBottomSheet?.updateTitle(title.toRaw())
    }

    private fun setupView() = with(binding) {

        tieName.requestFocus()

        vaultEdit?.let { setupVault(it) }

        btnConfirm.text = if (vaultEdit == null) {
            "Criar"
        } else {
            "Editar"
        }

        btnConfirm.setIconResource(
            if (vaultEdit == null) {
                R.drawable.ic_add_vector
            } else {
                R.drawable.ic_edit_vector
            }
        )

        binding.cgCurrency.setupCurrency()
    }

    private fun setupVault(vault: Vault) = with(binding) {
        if (!requireActivity().isChangingConfigurations) {
            tieName.setText(vault.name)

            if (vault.dateToBreak != null)
                viewModel.setDateToBreak(vault.dateToBreak)
        }
    }

    private fun setupListeners() {
        binding.btnDateToBreak.setOnClickListener {
            showDataPicker()
        }

        binding.btnConfirm.setOnClickListener {
            it.isEnabled = false
            createVault()
        }

        nameTextWatcher = binding.tilName.addValidationListener(
            validation = { value ->
                when {
                    value.trim().isBlank() -> {
                        ValidationResult.IsInvalid(
                            message = "Nome do cofre não pode ser vazio"
                        )
                    }

                    vaultEdit?.let { it.name == value } ?: false -> {
                        ValidationResult.IsValid
                    }

                    viewModel.hasVaultWithName(value) -> {
                        ValidationResult.IsInvalid(
                            message = "Já existe um cofre com esse nome"
                        )
                    }

                    else -> ValidationResult.IsValid
                }
            },
            isValid = {

                error = null
                isErrorEnabled = false

                val colorGreen = ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )

                boxStrokeColor = colorGreen
                hintTextColor = ColorStateList.valueOf(colorGreen)

                binding.btnConfirm.isEnabled = true
            },
            isInvalid = { textError ->

                error = textError
                isErrorEnabled = true

                binding.btnConfirm.isEnabled = false
            }
        )
    }

    private fun setupObservers() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            launch {
                viewModel.uiState.collect {
                    binding.tvDateToBreak.text =
                        it.dateToBreak?.formatted ?: "Indefinido"
                }
            }

            launch {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        CreateVaultUiEffect.Success -> {
                            binding.showSnackbar(
                                message = "Success".toRaw()
                            )

                            if (vaultEdit != null) {
                                createVaultBottomSheet?.setFragmentResult(
                                    Event.EDIT_VAULT.name,
                                    Bundle()
                                )
                            }

                            createVaultBottomSheet?.dismiss()
                        }
                        is CreateVaultUiEffect.Error -> {
                            binding.btnConfirm.isEnabled = true

                            binding.showSnackbar(
                                message = effect.error
                            )
                        }
                    }
                }
            }
        }
    }

    private fun ChipGroup.setupCurrency() {

        if (checkedChipId != View.NO_ID) return

        when (vaultEdit?.currency ?: CurrencyCompat.default()) {
            CurrencyCompat.BRL -> {
                check(R.id.chip_brl)
            }

            CurrencyCompat.USD -> {
                check(R.id.chip_usd)
            }

            CurrencyCompat.EUR -> {
                check(R.id.chip_eur)
            }
        }

        childrenIsEnable = Random.nextBoolean()
        binding.labelCurrency.alpha = if (vaultEdit == null) 1f else 0.5f
    }

    private fun createVault() {
        viewModel.createPiggyBank(
            name = binding.tilName.editText!!.text.toString().trim(),
            currency = getCurrency()
        )
    }

    private fun getCurrency(): CurrencyCompat {
        return when (binding.cgCurrency.checkedChipId) {
            R.id.chip_brl -> {
                CurrencyCompat.BRL
            }

            R.id.chip_usd -> {
                CurrencyCompat.USD
            }
            R.id.chip_eur -> {
                CurrencyCompat.EUR
            }

            else -> {
                throw IllegalStateException("invalid currency")
            }
        }
    }

    private fun showDataPicker() {
        MaterialDatePicker.Builder
            .datePicker()
            .setSelection(viewModel.dateToBreak?.time)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build().also { datePicker ->
                datePicker.addOnPositiveButtonClickListener { utcTimeMillis ->
                    viewModel.setDateToBreak(TimeUtils.utcToLocal(utcTimeMillis))
                }
                datePicker.show(
                    parentFragmentManager,
                    DATA_PICKER_TAG
                )
            }
    }

    companion object {
        const val DATA_PICKER_TAG = "DATA_PICKER"
    }

    enum class Event {
        EDIT_VAULT
    }
}