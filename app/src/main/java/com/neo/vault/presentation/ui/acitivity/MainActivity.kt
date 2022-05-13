package com.neo.vault.presentation.ui.acitivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.neo.vault.R
import com.neo.vault.databinding.ActivityMainBinding
import com.neo.vault.domain.model.CurrencySupport
import com.neo.vault.presentation.model.UiText
import com.neo.vault.presentation.model.Value
import com.neo.vault.presentation.ui.feature.showValues.ShowValuesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment
        get() = supportFragmentManager
            .findFragmentById(R.id.vaults_host) as NavHostFragment

    private val navController
        get() = navHostFragment.navController

    private val valuesFragment
        get() = supportFragmentManager
            .findFragmentById(R.id.container_values) as? ShowValuesFragment

    private val NavDestination.isInitialFragment
        get() = id == R.id.homeVaults

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { controller, destination, _ ->

            binding.toolbar.navigationIcon =
                if (!destination.isInitialFragment)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_back, theme) else null

            binding.toolbar.title = destination.label

            valuesFragment?.setValues(
                when (destination.id) {
                    R.id.homeVaults -> {
                        listOf(
                            Value.Total(
                                value = 3000f,
                                currency = CurrencySupport.BRL,
                                title = UiText.to("Guardado")
                            ),
                            Value.SubTotal(
                                title = UiText.to("Metas"),
                                value = 1500f,
                                currency = CurrencySupport.USD,
                                action = {

                                }
                            ),
                            Value.SubTotal(
                                title = UiText.to("Cofrinhos"),
                                value = 1500f,
                                currency = CurrencySupport.EUR,
                                action = {
                                    controller.navigate(R.id.action_homeVaults_to_piggyBankVaults)
                                }
                            ),
                        )
                    }
                    else -> listOf(
                        Value.Total(
                            value = 0f,
                            currency = CurrencySupport.EUR,
                            title = UiText.to("Total")
                        )
                    )
                }
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}