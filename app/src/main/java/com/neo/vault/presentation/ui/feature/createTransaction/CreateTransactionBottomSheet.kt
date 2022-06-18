package com.neo.vault.presentation.ui.feature.createTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neo.vault.databinding.FragmentTransactionBinding
import com.neo.vault.utils.extension.behavior
import com.neo.vault.utils.extension.expanded

class CreateTransactionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(
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
        etValue.showSoftInputOnFocus = false
        etValue.isHorizontalScrollBarEnabled = false
    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener {
            val length = binding.etValue.length()

            if (length > 0) {

                if (length == 1) {
                    binding.etValue.text = "0,00"
                    return@setOnClickListener
                }

                binding.etValue.text =
                    binding.etValue.text.subSequence(0, length - 1)
            }
        }
    }

}